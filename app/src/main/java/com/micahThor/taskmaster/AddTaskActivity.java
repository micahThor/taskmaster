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

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();

        // wire up ADD TASK button
        Button addTaskButton = findViewById(R.id.addATaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                // get task title
//                EditText titleTextEdit = findViewById(R.id.taskTextInput);
//                String taskTitle = titleTextEdit.getText().toString();
//
//                // get task description
//                EditText descriptionTextEdit = findViewById(R.id.taskDescriptionInput);
//                String taskDescription = descriptionTextEdit.getText().toString();
//
//                // get task state
//                RadioGroup stateRadioGroup  = findViewById(R.id.taskStateRadioGroup);
//                int checkedState = stateRadioGroup.getCheckedRadioButtonId();
//                RadioButton selectedStateButton = findViewById(checkedState);
//                String taskState = selectedStateButton.getText().toString();

                runTaskCreateMutation();
                //taskDb.taskDao().saveTask(new Task(taskTitle, taskDescription, taskState));
//
                Intent goToMainActivity = new Intent(AddTaskActivity.this, MainActivity.class);
                AddTaskActivity.this.startActivity(goToMainActivity);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        taskList = taskDb.taskDao().getAllTasks();
        TextView taskCounter= findViewById(R.id.totalTaskCountTextView);
        taskCounter.setText("Total tasks: " + taskList.size());

    }

    public void runTaskCreateMutation() {
        CreateTaskInput taskInput = CreateTaskInput.builder()
                .title("testinput")
                .body("testinput")
                .state("testinput")
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
