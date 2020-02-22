package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class AddTaskActivity extends AppCompatActivity {

    TaskDatabase taskDb;
    List<Task> taskList;
    private AWSAppSyncClient mAWSAppSyncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // PAGE ELEMENTS
        final Button addTaskButton = findViewById(R.id.addATaskButton);
        final EditText titleTextEdit = findViewById(R.id.taskTextInput);
        final EditText descriptionTextEdit = findViewById(R.id.taskDescriptionInput);
        final RadioGroup stateRadioGroup = findViewById(R.id.taskStateRadioGroup);

        final Toast resultToast = Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_SHORT);

        // initialize AWS dynamo db and local databases
        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();

        // wire up ADD TASK button
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Validate inputs
                if (titleTextEdit.getText().toString().length() == 0) {
                    titleTextEdit.setError("Title Required");
                } else if (descriptionTextEdit.getText().toString().length() == 0) {
                    descriptionTextEdit.setError("Description Required");
                } else if (stateRadioGroup.getCheckedRadioButtonId() == -1) {
                    resultToast.setText("Task State Required");
                    resultToast.show();
                } else {
                    // get task title
                    String taskTitle = titleTextEdit.getText().toString();

                    // get task description
                    String taskDescription = descriptionTextEdit.getText().toString();

                    // get task state
                    int checkedState = stateRadioGroup.getCheckedRadioButtonId();
                    RadioButton selectedStateButton = findViewById(checkedState);
                    String taskState = selectedStateButton.getText().toString();

                    // add Task to databases
                    runTaskCreateMutation(taskTitle, taskDescription, taskState);
                    taskDb.taskDao().saveTask(new Task(taskTitle, taskDescription, taskState));

                    // empty inputs and display successful message
                    titleTextEdit.setText("");
                    descriptionTextEdit.setText("");
                    selectedStateButton.setChecked(false);
                    resultToast.setText("Saved Task: " + taskTitle);
                    resultToast.show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        taskList = taskDb.taskDao().getAllTasks();
        TextView taskCounter = findViewById(R.id.totalTaskCountTextView);
        taskCounter.setText("Total tasks: " + taskList.size());
    }

    public void runTaskCreateMutation(String taskTitle, String taskBody, String taskState) {
        CreateTaskInput taskInput = CreateTaskInput.builder()
                .title(taskTitle)
                .body(taskBody)
                .state(taskState)
                .build();

        mAWSAppSyncClient.mutate(CreateTaskMutation.builder().input(taskInput).build())
                .enqueue(addMutationCallback);
    }

    private GraphQLCall.Callback<CreateTaskMutation.Data> addMutationCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
            Log.i("micah", "Added task");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("micah", e.toString());
        }
    };
}
