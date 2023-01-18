package com.example.mangalist.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MangaRes {
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("data")
    @Expose
    private List<Manga> data = null;

    public List<Manga> getData() {
        return data;
    }

    public void setData(List<Manga> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public class Pagination {
        @SerializedName("last_visible_page")
        @Expose
        private Integer totalPage;

        public Integer getTotalPage() {
            return totalPage;
        }
    }
}
