package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
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

        // retrieve task TITLE from Intent
        String headerText = getIntent().getStringExtra("title");
        // get header obj and set text
        TextView taskHeader = findViewById(R.id.taskHeaderTextView);
        taskHeader.setText(headerText);

        // retrieve task STATE from Intent
        String stateText = getIntent().getStringExtra("state");
        // get header obj and set text
        TextView stateTextV = findViewById(R.id.stateTextView);
        stateTextV.setText(stateText);

        // retrieve task DESCRIPTION from Intent
        String descriptionText = getIntent().getStringExtra("description");
        // get header obj and set text
        EditText stateDescription = findViewById(R.id.taskDescriptionEditText);
        stateDescription.setText(descriptionText);

    }
}
