package com.example.mangalist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MangaResSingle {
    @SerializedName("data")
    @Expose
    private Manga data = null;

    public Manga getData() {
        return data;
    }

    public void setData(Manga data) {
        this.data = data;
    }
}
