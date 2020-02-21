package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class MainActivity extends AppCompatActivity implements MyTaskRecyclerViewAdapter.taskOnClickListener {

    List<Task> taskList;
    TaskDatabase taskDb;

    private AWSAppSyncClient mAWSAppSyncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();
        taskList = taskDb.taskDao().getAllTasks();

        RecyclerView recyclerView = findViewById(R.id.taskFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(this.taskList, null, this));

        // wire up ADD TASKS BUTTON
        Button goToAddTasksButton = findViewById(R.id.goToTaskActivityButton);
        goToAddTasksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAddTaskActivity = new Intent(MainActivity.this, AddTaskActivity.class);
                MainActivity.this.startActivity(goToAddTaskActivity);
            }
        });

        // wire up ALL TASKS BUTTON
        Button goToAllTasksButton = findViewById(R.id.allTasksButton);
        goToAllTasksButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToAllTaskActivity = new Intent(MainActivity.this, AllTasksActivity.class);
                MainActivity.this.startActivity(goToAllTaskActivity);
            }
        });

        // wire up SETTINGS BUTTON
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent goToSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(goToSettingsActivity);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView userNameTextView = findViewById(R.id.userNameTextView);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userName = sharedPrefs.getString("userName", "User Name");
        userNameTextView.setText(userName + "'s Tasks");
    }

    @Override
    public void onClick(Task t) {
        Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
        intent.putExtra("title", t.getTitle());
        intent.putExtra("description", t.getBody());
        intent.putExtra("state", t.getState());
        this.startActivity(intent);
    }

//    public void runTaskCreatMutation() {
//        CreateTaskInput taskInput = CreateTaskInput.builder()
//                .title("testinput")
//                .body("testinput")
//                .state("testinput")
//                .build();
//        mAWSAppSyncClient.mutate(CreateTaskMutation.builder().input(taskInput).build())
//                .enqueue(addMutationCallback);
//    }
//
//    private GraphQLCall.Callback<CreateTaskMutation.Data> addMutationCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
//        @Override
//        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
//            Log.i("Results", "Added task");
//        }
//
//        @Override
//        public void onFailure(@Nonnull ApolloException e) {
//            Log.e("Error", e.toString());
//        }
//    };


}
