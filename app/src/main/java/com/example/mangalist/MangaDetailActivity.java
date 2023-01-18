package com.example.mangalist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mangalist.model.Manga;
import com.example.mangalist.model.MangaResSingle;
import com.example.mangalist.services.api.ApiClient;
import com.example.mangalist.services.api.IApi;
import com.example.mangalist.services.database.DatabaseHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MangaDetailActivity extends AppCompatActivity {
    private TextView mangaTitle;
    private TextView mangaAuthor;
    private TextView mangaStatus;
    private TextView mangaDescription;
    private RoundedImageView mangaImage;
    private Manga mangaData;
    private ImageView mangaBackdrop;
    private DatabaseHelper databaseHelper;
    private Button buttonAddToLibrary;
    private String imageUrl;
    private ImageButton backButton;
    private Integer mangaId;
    private RatingBar ratingBar;
    private TextView ratingDescription;
    private TextView genreValue;

    private static final String TAG = "MangaDetail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_detail);

        databaseHelper = new DatabaseHelper(this);

        // Bind xml element dengan variable java
        bind();

        // Cari data di api menggunakan id yang dapat dari activity sebelumnya
        mangaId = getIntent().getExtras().getInt("id");
        Retrofit retrofit = ApiClient.getInstance();
        IApi api = retrofit.create(IApi.class);

        // Set button biar tampilannya sesuai state dari manganya
        if(databaseHelper.isExist(mangaId) != null){
            buttonAddToLibrary.setText("View my Library");
        }

        // Ambil data dari api
        api.getMangaById(mangaId).enqueue(new Callback<MangaResSingle>() {
            @Override
            public void onResponse(Call<MangaResSingle> call, Response<MangaResSingle> response) {
                if(response.isSuccessful() && response.body() != null){
                    mangaData = response.body().getData();
                    Glide.with(getApplicationContext()).load(mangaData.getImages().getJpg().getImageUrl()).into(mangaBackdrop);
                    String authors = mangaData.getAuthors().stream()
                            .reduce("", ((s, author) -> {
                                return s + (s.isEmpty() ? "" : ", ") + author.getName().replace(",", "");
                            }), String::concat);
                    String genres = mangaData.getGenres().stream()
                            .reduce("", ((s, genre) -> {
                                return s + (s.isEmpty() ? "" : ", ") + genre.getName().replace(",", "");
                            }), String::concat);
                    mangaTitle.setText(mangaData.getTitle());
                    mangaAuthor.setText(authors);
                    mangaStatus.setText(mangaData.getStatus());
                    mangaDescription.setText(mangaData.getSynopsis());
                    Glide.with(getApplicationContext()).load(mangaData.getImages().getJpg().getImageUrl()).into(mangaImage);
                    imageUrl = mangaData.getImages().getJpg().getImageUrl();
                    ratingBar.setRating((float) (mangaData.getScore()/2));
                    ratingDescription.setText("Scored by " + mangaData.getScoredBy() + " peoples");
                    genreValue.setText(genres);
                }
            }

            @Override
            public void onFailure(Call<MangaResSingle> call, Throwable t) {
                System.out.println("Ada Error Ges");
            }
        });

        buttonAddToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(databaseHelper.isExist(mangaId) != null){
                    Intent intent = new Intent(view.getContext(), MangaLibraryActivity.class);
                    startActivity(intent);
                }else{
                    boolean isAddSucces = databaseHelper.addBook(mangaId,(String) mangaTitle.getText(), "Plan to read", imageUrl);
                    if(isAddSucces){
                        buttonAddToLibrary.setText("View my Library");
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(databaseHelper.isExist(mangaId) != null){
            buttonAddToLibrary.setText("View my Library");
        }else{
            buttonAddToLibrary.setText("Save to my Library");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(databaseHelper.isExist(mangaId) != null){
            buttonAddToLibrary.setText("View my Library");
        }else{
            buttonAddToLibrary.setText("Save to my Library");
        }
    }

    private void bind(){
        this.mangaTitle = this.findViewById(R.id.mangaTitleLibrary);
        this.mangaAuthor = this.findViewById(R.id.mangaAuthor);
        this.mangaStatus = this.findViewById(R.id.mangaStatusLibrary);
        this.mangaDescription = this.findViewById(R.id.mangaDescription);
        this.mangaImage = this.findViewById(R.id.mangaImageLibrary);
        this.mangaBackdrop = this.findViewById(R.id.mangaBackdrop);
        this.buttonAddToLibrary = this.findViewById(R.id.buttonAddToLibrary);
        this.backButton = this.findViewById(R.id.mangaDetailBackButton);
        this.ratingBar = this.findViewById(R.id.valueRating);
        this.ratingDescription = this.findViewById(R.id.detailRating);
        this.genreValue = this.findViewById(R.id.genreValue);
   }
}