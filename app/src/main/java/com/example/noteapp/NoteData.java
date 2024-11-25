package com.example.noteapp;

public class NoteData {

    int id;
    String title;
    String content;
    String date;



    public NoteData(int id, String title, String content) {
        this.content = content;
        this.title = title;
        this.id = id;
    }

    public NoteData() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
