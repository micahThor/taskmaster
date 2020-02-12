package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        /*
                THREE HARD-CODED TASK BUTTONS
         */
        final Button washDishesButton = findViewById(R.id.task1Button);
        washDishesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get text of button to pass into shared preferences
                String taskTitle = washDishesButton.getText().toString();
                saveTitleToSharedPrefs(taskTitle);
            }
        });

        final Button walkMolly = findViewById(R.id.task2Button);
        walkMolly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get text of button to pass into shared preferences
                String taskTitle = walkMolly.getText().toString();
                saveTitleToSharedPrefs(taskTitle);
            }
        });

        final Button buyFlowers = findViewById(R.id.task3Button);
        buyFlowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get text of button to pass into shared preferences
                String taskTitle = buyFlowers.getText().toString();
                saveTitleToSharedPrefs(taskTitle);
            }
        });

    }

    private void saveTitleToSharedPrefs(String title) {

        SharedPreferences sharedPrefers = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefers.edit();
        editor.putString("taskTitle", title);
        editor.commit();
        Intent goToDetailActivity = new Intent(MainActivity.this, TaskDetailActivity.class);
        MainActivity.this.startActivity(goToDetailActivity);
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
