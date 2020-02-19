package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Task> taskList;
    TaskDatabase taskDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();
        taskList = taskDb.taskDao().getAllTasks();

        RecyclerView recyclerView = findViewById(R.id.taskFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(this.taskList, null));

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
}
