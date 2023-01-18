package com.example.mangalist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MangaImage {

    @SerializedName("jpg")
    @Expose
    private Jpg jpg;

    public Jpg getJpg() {
        return jpg;
    }

    public void setJpg(Jpg jpg) {
        this.jpg = jpg;
    }


}
