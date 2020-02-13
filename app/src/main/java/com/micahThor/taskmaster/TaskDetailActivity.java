package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // retrieve task title from Intent
        String headerText = getIntent().getStringExtra("title");


        // get header obj and set text
        TextView taskHeader = findViewById(R.id.taskHeaderTextView);
        taskHeader.setText(headerText);
    }
}
