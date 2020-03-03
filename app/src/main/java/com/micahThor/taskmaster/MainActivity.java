package com.micahThor.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity implements MyTaskRecyclerViewAdapter.taskOnClickListener {

    private TaskDatabase taskDb;
    private List<Task> taskList;
    private static final String TAG = "micah.mainactivity";
    private static PinpointManager pinpointManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "created now");

        getPinpointManager(getApplicationContext());

        taskList = new ArrayList<>();
        // Get data from AWS database and store in array
        runQuery();
        // Set user name
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        String username = AWSMobileClient.getInstance().getUsername();
        userNameTextView.setText(username + "'s Tasks");

        // wire up ADD TASKS BUTTON
        final Button goToAddTasksButton = findViewById(R.id.goToTaskActivityButton);
        goToAddTasksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAddTaskActivity = new Intent(MainActivity.this, AddTaskActivity.class);
                MainActivity.this.startActivity(goToAddTaskActivity);
            }
        });
        // wire up ALL TASKS BUTTON
        final Button goToAllTasksButton = findViewById(R.id.allTasksButton);
        goToAllTasksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAllTaskActivity = new Intent(MainActivity.this, AllTasksActivity.class);
                MainActivity.this.startActivity(goToAllTaskActivity);
            }
        });
        // wire up LOGOUT BUTTON
        final ImageButton signOutButton = findViewById(R.id.settingsButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AWSMobileClient.getInstance().signOut(SignOutOptions.builder().signOutGlobally(true).build(), new Callback<Void>() {
                    @Override
                    public void onResult(final Void result) {
                        AWSMobileClient.getInstance().showSignIn(MainActivity.this, new Callback<UserStateDetails>() {
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

        // log in on page load (if user is not already logged in)
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                if (userStateDetails.getUserState().equals(UserState.SIGNED_OUT)) {
                    // 'this' refers the the current active activity
                    AWSMobileClient.getInstance().showSignIn(MainActivity.this, new Callback<UserStateDetails>() {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "resumed now");

        // Get data from AWS database and store in array
        runQuery();
        // Set user name
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        String username = AWSMobileClient.getInstance().getUsername();
        userNameTextView.setText(username + "'s Tasks");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Get data from AWS database and store in array
        runQuery();
        // Set user name
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        String username = AWSMobileClient.getInstance().getUsername();
        userNameTextView.setText(username + "'s Tasks");
        Log.i(TAG, "restarted now");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "stopped now");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroyed now");
    }

    public void runQuery() {
        AWSAppSyncClient mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        mAWSAppSyncClient.query(ListTasksQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(tasksCallback);
        RecyclerView recyclerView = findViewById(R.id.taskFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(this.taskList, null, MainActivity.this));
    }

    private GraphQLCall.Callback<ListTasksQuery.Data> tasksCallback = new GraphQLCall.Callback<ListTasksQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListTasksQuery.Data> response) {
            if (taskList.size() == 0 || response.data().listTasks().items().size() != taskList.size()) {

                taskList.clear();

                for (ListTasksQuery.Item item : response.data().listTasks().items()) {
                    Task t = new Task(item.title(), item.body(), item.state(), item.imageFileName());
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
        intent.putExtra("imageFileName", t.getImageFileName());
        this.startActivity(intent);
    }

    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            final String token = task.getResult().getToken();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }
}
