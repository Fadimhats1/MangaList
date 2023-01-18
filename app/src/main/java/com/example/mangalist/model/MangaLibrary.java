package com.example.mangalist.model;

public class MangaLibrary {
    private Integer mangaId;
    private String mangaTitle;
    private String mangaStatus;
    private String mangaImageUrl;


    public Integer getMangaId() {
        return mangaId;
    }

    public void setMangaId(Integer mangaId) {
        this.mangaId = mangaId;
    }

    public String getMangaTitle() {
        return mangaTitle;
    }

    public void setMangaTitle(String mangaTitle) {
        this.mangaTitle = mangaTitle;
    }

    public String getMangaStatus() {
        return mangaStatus;
    }

    public void setMangaStatus(String mangaStatus) {
        this.mangaStatus = mangaStatus;
    }

    public String getMangaImageUrl() {
        return mangaImageUrl;
    }

    public void setMangaImageUrl(String mangaImageUrl) {
        this.mangaImageUrl = mangaImageUrl;
    }

}
