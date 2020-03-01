package com.micahThor.taskmaster;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Task.class}, version = 3)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
