package com.example.mangalist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mangalist.adapter.MangaAdapter;
import com.example.mangalist.model.Manga;
import com.example.mangalist.model.MangaRes;
import com.example.mangalist.services.api.ApiClient;
import com.example.mangalist.services.api.IApi;
import com.example.mangalist.utils.PaginatedScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvManga;
    private MangaAdapter mangaAdapter;
    private List<Manga> mangaList = new ArrayList<>();
    private IApi mangaService;
    private ImageButton myLibrary;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private Integer currentPage = PAGE_START;
    private static final String TAG = "MainActivity";
    private static int TOTAL_PAGES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buat object yang berhubungan sama apinya
        mangaService = ApiClient.getInstance().create(IApi.class);

        // Recycleview data dari api & recycleview nya sudah bersifat infinite scroll
        rvManga = findViewById(R.id.mangaRView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvManga.setLayoutManager(layoutManager);
        mangaAdapter = new MangaAdapter(this, mangaList);
        rvManga.setAdapter(mangaAdapter);

        myLibrary = this.findViewById(R.id.myLibrary);
        myLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MangaLibraryActivity.class);
                startActivity(intent);
            }
        });

        rvManga.addOnScrollListener(new PaginatedScrollListener(layoutManager) {
            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadNextPage();
//                    }
//                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }
        });
        loadFirstPage();


    }

    private Call<MangaRes> mangaResCall() {
        return mangaService.getMangas(10, null, currentPage);
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");
        mangaResCall().enqueue(new Callback<MangaRes>() {
            @Override
            public void onResponse(Call<MangaRes> call, Response<MangaRes> response) {
                List<Manga> mangas = response.body().getData();
                mangaAdapter.addAll(mangas);
                if (currentPage <= TOTAL_PAGES) {
                    mangaAdapter.addLoading();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<MangaRes> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: ");
        mangaResCall().enqueue(new Callback<MangaRes>() {
            @Override
            public void onResponse(Call<MangaRes> call, Response<MangaRes> response) {
                mangaAdapter.removeLoading();
                isLoading = false;
                List<Manga> mangas = response.body().getData();
                mangaAdapter.addAll(mangas);
                Log.d(TAG, "onResponse: " + TOTAL_PAGES);
                if (currentPage != TOTAL_PAGES) {
                    mangaAdapter.addLoading();
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<MangaRes> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}