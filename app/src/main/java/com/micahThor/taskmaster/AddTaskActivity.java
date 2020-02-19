package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    TaskDatabase taskDb;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();

        // wire up ADD TASK button
        Button addTaskButton = findViewById(R.id.addATaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // get task title
                EditText titleTextEdit = findViewById(R.id.taskTextInput);
                String taskTitle = titleTextEdit.getText().toString();

                // get task description
                EditText descriptionTextEdit = findViewById(R.id.taskDescriptionInput);
                String taskDescription = descriptionTextEdit.getText().toString();

                // get task state
                RadioGroup stateRadioGroup  = findViewById(R.id.taskStateRadioGroup);
                int checkedState = stateRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedStateButton = findViewById(checkedState);
                String taskState = selectedStateButton.getText().toString();


                taskDb.taskDao().saveTask(new Task(taskTitle, taskDescription, taskState));

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
}
