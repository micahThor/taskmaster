package com.micahThor.taskmaster;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String body;
    private String state;
    private String imageFileName;

    @Ignore
    public Task(String title, String body, String state, String imageFileName) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.imageFileName = imageFileName;
    }

    public Task() {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setId(long id) { this.id = id; }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public long getId() { return id; }
}
