package com.example.ricecropkeeptwo;

public class firebasemodel {
    private String title;
    private String notecontent;

    public firebasemodel(){
    }

    public firebasemodel(String title, String notecontent){
        this.title = title;
        this.notecontent = notecontent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotecontent() {
        return notecontent;
    }

    public void setNotecontent(String notecontent) {
        this.notecontent = notecontent;
    }
}


