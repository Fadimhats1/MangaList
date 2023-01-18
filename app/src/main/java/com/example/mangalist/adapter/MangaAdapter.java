package com.example.mangalist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangalist.MangaDetailActivity;
import com.example.mangalist.R;
import com.example.mangalist.model.Manga;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class MangaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MangaAdapter";
    private List<Manga> mangaList;
    private Context context;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public MangaAdapter(Context context, List<Manga> mangaList) {
        this.mangaList = new ArrayList<>();
        this.context = context;
    }

    public List<Manga> getMangaList() {
        return mangaList;
    }

    public void setMangaList(List<Manga> mangaList) {
        this.mangaList = mangaList;
    }

    public void add(Manga manga){
        this.mangaList.add(manga);
        notifyItemInserted(mangaList.size() - 1);
    }

    public void addAll(List<Manga> mangas){
        for (Manga manga : mangas) {
            this.add(manga);
        }
    }

    public void remove(Manga manga){
        int pos = mangaList.indexOf(manga);
        if(pos > -1){
            this.mangaList.remove(manga);
            notifyItemRemoved(pos);
        }
    }

    public void addLoading(){
        isLoadingAdded = true;
        add(new Manga());
    }

    public void removeLoading(){
        isLoadingAdded = false;
        int pos = mangaList.size() - 1;
        Manga manga = mangaList.get(pos);
        if(manga !=  null){
            mangaList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if(viewType == ITEM){
            view = layoutInflater.inflate(R.layout.manga_row, parent, false);
            viewHolder = new MangaViewHolder(view);
        }else if(viewType == LOADING){
            view = layoutInflater.inflate(R.layout.item_progress, parent, false);
            viewHolder = new LoadingViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Manga manga = mangaList.get(position);
        if(getItemViewType(position) == ITEM){
            final MangaViewHolder mangaVH = (MangaViewHolder) holder;
            mangaVH.bind(manga);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mangaList.size() -1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public static class MangaViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mangaImage;
        private TextView mangaTitle;
        private TextView mangaStatus;
        private TextView mangaAuthor;
        private TextView mangaDescription;

        public MangaViewHolder(@NonNull View itemView) {
            super(itemView);
            mangaImage = itemView.findViewById(R.id.mangaImage);
            mangaTitle = itemView.findViewById(R.id.mangaTitle);
            mangaStatus = itemView.findViewById(R.id.mangaStatus);
            mangaAuthor = itemView.findViewById(R.id.mangaAuthor);
            mangaDescription = itemView.findViewById(R.id.mangaDescription);
        }

        public void bind(Manga manga) {
            String authors = manga.getAuthors().stream()
                    .reduce("", ((s, author) -> {
                        return s + (s.isEmpty() ? "" : ", ") + author.getName().replace(",", "");
                    }), String::concat);
            this.mangaTitle.setText(manga.getTitle());
            this.mangaAuthor.setText(authors);
            this.mangaStatus.setText(manga.getStatus());
            this.mangaDescription.setText(manga.getSynopsis());
            Glide.with(itemView.getContext()).load(manga.getImages().getJpg().getImageUrl()).into(this.mangaImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toDetailPage = new Intent(view.getContext(), MangaDetailActivity.class);
                    toDetailPage.putExtra("id", manga.getMalId());
                    view.getContext().startActivity(toDetailPage);
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View view) {
            super(view);
        }
    }
}
