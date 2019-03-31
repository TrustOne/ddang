package com.example.administrator.secondlogin.activities;

import com.google.firebase.storage.StorageReference;

public class CardItem {
    private String comment,name,time;
    Float ratingbar;
    private StorageReference img;
//    private  int ratingbar;


    public CardItem(String comment, String name, String time, StorageReference img, Float ratingbar) {
        this.comment = comment;
        this.name = name;
        this.time = time;
        this.img = img;
        this.ratingbar = ratingbar;
    }

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public void setImg(StorageReference img) {
        this.img = img;
    }

    public void setRatingbar(Float ratingbar) {
        this.ratingbar = ratingbar;
    }

    public StorageReference getImg() {
        return img;
    }

    public Float getRatingbar() {
        return ratingbar;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
