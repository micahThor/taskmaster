package com.micahThor.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class AllTasksActivity extends AppCompatActivity {

    List<Task> taskList;
    TaskDatabase taskDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        taskDb = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks").allowMainThreadQueries().build();
        taskList = taskDb.taskDao().getAllTasks();

        RecyclerView recyclerView = findViewById(R.id.taskFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyTaskRecyclerViewAdapter(this.taskList, null));
    }

}
