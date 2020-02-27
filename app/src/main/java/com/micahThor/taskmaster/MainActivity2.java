package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignOutOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class MainActivity2 extends AppCompatActivity implements MyTaskRecyclerViewAdapter.taskOnClickListener {

    private TaskDatabase taskDb;
    private AWSAppSyncClient mAWSAppSyncClient;
    final List<Task> taskList = new ArrayList<>();
    final String TAG = "micah.mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // page ELEMENTS
        final RecyclerView recyclerView = findViewById(R.id.taskFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        final Button goToAddTasksButton = findViewById(R.id.goToTaskActivityButton);
        final Button goToAllTasksButton = findViewById(R.id.allTasksButton);
        final ImageButton signOutButton = findViewById(R.id.settingsButton);

        // wire up ADD TASKS BUTTON
        goToAddTasksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAddTaskActivity = new Intent(MainActivity2.this, AddTaskActivity.class);
                MainActivity2.this.startActivity(goToAddTaskActivity);
            }
        });
        // wire up ALL TASKS BUTTON
        goToAllTasksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAllTaskActivity = new Intent(MainActivity2.this, AllTasksActivity.class);
                MainActivity2.this.startActivity(goToAllTaskActivity);
            }
        });
        // wire up LOGOUT BUTTON
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AWSMobileClient.getInstance().signOut(SignOutOptions.builder().signOutGlobally(true).build(), new Callback<Void>() {
                    @Override
                    public void onResult(final Void result) {
                        AWSMobileClient.getInstance().showSignIn(MainActivity2.this, new Callback<UserStateDetails>() {
                            @Override
                            public void onResult(UserStateDetails result) {
                                Log.d(TAG, "onResult: " + result.getUserState());
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "onError: ", e);
                            }
                        });
                        Log.d(TAG, "signed-out");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "sign-out error", e);
                    }
                });
            }
        });

        // log in on page load
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                if (userStateDetails.getUserState().equals(UserState.SIGNED_OUT)) {
                    // 'this' refers the the current active activity
                    AWSMobileClient.getInstance().showSignIn(MainActivity2.this, new Callback<UserStateDetails>() {
                        @Override
                        public void onResult(UserStateDetails result) {
                            Log.d(TAG, "onResult: " + result.getUserState());
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "onError: ", e);
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Initialization error.", e);
            }
        });


        // Get data from AWS database and store in array
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        runQuery();
        // set the tasks to the page
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(this.taskList, null, MainActivity2.this));

        TextView userNameTextView = findViewById(R.id.userNameTextView);
        String username = AWSMobileClient.getInstance().getUsername();
        userNameTextView.setText(username + "'s Tasks");
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView userNameTextView = findViewById(R.id.userNameTextView);
        String username = AWSMobileClient.getInstance().getUsername();
        userNameTextView.setText(username + "'s Tasks");

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

        runQuery();
        final RecyclerView recyclerView = findViewById(R.id.taskFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(this.taskList, null, this));
    }

    public void runQuery() {
        mAWSAppSyncClient.query(ListTasksQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(tasksCallback);
    }

    private GraphQLCall.Callback<ListTasksQuery.Data> tasksCallback = new GraphQLCall.Callback<ListTasksQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTasksQuery.Data> response) {
            if (taskList.size() == 0 || response.data().listTasks().items().size() != taskList.size()) {

                taskList.clear();

                for (ListTasksQuery.Item item : response.data().listTasks().items()) {
                    Task t = new Task(item.title(), item.body(), item.state(), "dugg");
                    taskList.add(t);
                }
            }
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

    @Override
    public void onClick(Task t) {
        Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
        intent.putExtra("title", t.getTitle());
        intent.putExtra("description", t.getBody());
        intent.putExtra("state", t.getState());
        this.startActivity(intent);
    }
}
