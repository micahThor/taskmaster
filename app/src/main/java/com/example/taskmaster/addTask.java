package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class addTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // wire up add task button
        Button addTaskButton = findViewById(R.id.addATaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView submittedTask = findViewById(R.id.submittedTaskPopup);
                submittedTask.setVisibility(View.VISIBLE);
            }
        });

    }
}
