package com.example.mangalist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mangalist.adapter.MangaLibraryAdapter;
import com.example.mangalist.model.MangaLibrary;
import com.example.mangalist.services.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MangaLibraryActivity extends AppCompatActivity {

    private List<MangaLibrary> mangaLibraries;
    private DatabaseHelper databaseHelper;
    private RecyclerView rvMangaLibrary;
    private MangaLibraryAdapter mangaLibraryAdapter;
    private static final String TAG = "MangaLibraryActivity";
    private ImageButton myLibraryBackButton;
    private TextView labelNoData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_library);
        mangaLibraries = new ArrayList<>();
        this.myLibraryBackButton = this.findViewById(R.id.myLibraryBackButton);
        this.labelNoData = this.findViewById(R.id.labelNoData);

        databaseHelper = new DatabaseHelper(this);

        mangaLibraries = databaseHelper.getAllData();



        rvMangaLibrary = findViewById(R.id.mangaLibraryRView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMangaLibrary.setLayoutManager(layoutManager);
        mangaLibraryAdapter = new MangaLibraryAdapter( this, new DatabaseHelper(this));
        rvMangaLibrary.setAdapter(mangaLibraryAdapter);
        mangaLibraryAdapter.submitList(mangaLibraries);
        if(mangaLibraries.size() > 0){
            rvMangaLibrary.setVisibility(View.VISIBLE);
        }else{
            labelNoData.setVisibility(View.VISIBLE);
        }
        myLibraryBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}