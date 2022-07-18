package com.example.marcin.learnenglishapp.reminder;

// Klasa Model jest używana aby ustawić i otrzymać dane z bazy danych
public class Model {

    String time;
    String date;
    String title;

    public Model (){

    }

    public Model(String time, String date, String title) {
        this.time = time;
        this.date = date;
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
