package com.example.mangalist.services.api;

import com.example.mangalist.model.Manga;
import com.example.mangalist.model.MangaRes;
import com.example.mangalist.model.MangaResSingle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApi {
    @GET("manga")
    Call<MangaRes> getMangas(@Query("limit") Integer limit, @Query("q") String string, @Query("page") Integer page);

    @GET("manga/{id}")
    Call<MangaResSingle> getMangaById(@Path("id") Integer id);
}
