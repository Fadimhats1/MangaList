package com.example.mangalist.adapter;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mangalist.MangaLibraryActivity;
import com.example.mangalist.R;
import com.example.mangalist.model.MangaLibrary;
import com.example.mangalist.services.database.DatabaseHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Arrays;
import java.util.List;

public class MangaLibraryAdapter extends ListAdapter<MangaLibrary,MangaLibraryAdapter.ViewHolder> {
    private static final String TAG = "MangaLibraryAdapter";
    private MangaLibraryActivity activity;
    private DatabaseHelper  databaseHelper;
    private static final DiffUtil.ItemCallback<MangaLibrary> DIFF_CALLBACK = new DiffUtil.ItemCallback<MangaLibrary>() {
        @Override
        public boolean areItemsTheSame(@NonNull MangaLibrary oldItem, @NonNull MangaLibrary newItem) {
            return oldItem.getMangaId().equals(newItem.getMangaId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MangaLibrary oldItem, @NonNull MangaLibrary newItem) {
            return oldItem.getMangaStatus().equals(newItem.getMangaStatus()) && oldItem.getMangaTitle().equals(newItem.getMangaTitle()) && oldItem.getMangaImageUrl().equals(newItem.getMangaImageUrl());
        }
    };


    public MangaLibraryAdapter( MangaLibraryActivity activity, DatabaseHelper databaseHelper) {
        super(DIFF_CALLBACK);
        this.activity = activity;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public MangaLibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manga_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaLibraryAdapter.ViewHolder holder, int position) {
        MangaLibrary mangaLibrary = getItem(position);
        holder.bind(mangaLibrary);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final RoundedImageView mangaImageLibrary;
        private final TextView mangaTitleLibrary;
        private final TextView mangaStatusLibrary;
        private final Button mangaButtonEditLibrary;
        private final Button mangaButtonDeleteLibrary;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mangaTitleLibrary = itemView.findViewById(R.id.mangaTitleLibrary);
            mangaStatusLibrary = itemView.findViewById(R.id.mangaStatusLibrary);
            mangaImageLibrary = itemView.findViewById(R.id.mangaImageLibrary);
            mangaButtonEditLibrary =itemView.findViewById(R.id.mangaButtonEditLibrary);
            mangaButtonDeleteLibrary = itemView.findViewById(R.id.mangaButtonDeleteLibrary);

        }

        public void bind(MangaLibrary mangaLibrary){
            this.mangaTitleLibrary.setText(mangaLibrary.getMangaTitle());
            this.mangaStatusLibrary.setText(mangaLibrary.getMangaStatus());
            Glide.with(itemView.getContext()).load(mangaLibrary.getMangaImageUrl()).into(this.mangaImageLibrary);

            this.mangaButtonDeleteLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHelper.deleteOneRow(mangaLibrary.getMangaId());
                    List<MangaLibrary> newMangaLibrary = databaseHelper.getAllData();
                    submitList(newMangaLibrary);
                    if(newMangaLibrary.size() == 0){
                        activity.findViewById(R.id.mangaLibraryRView).setVisibility(View.GONE);
                        activity.findViewById(R.id.labelNoData).setVisibility(View.VISIBLE);
                    }
                }
            });

            this.mangaButtonEditLibrary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
                    builder.setTitle("Change Status");

                    final String[] status = {"Finished", "Plan to read", "Reading"};
                    Log.d(TAG, "onClick: " + Arrays.asList(status).indexOf(databaseHelper.isExist(mangaLibrary.getMangaId()) == null ? "Plan to read" : databaseHelper.isExist(mangaLibrary.getMangaId())));
                    final int[] selectedValue = {Arrays.asList(status).indexOf(databaseHelper.isExist(mangaLibrary.getMangaId()) == null ? "Plan to read" : databaseHelper.isExist(mangaLibrary.getMangaId()).getMangaStatus())};
                    builder.setSingleChoiceItems(status, selectedValue[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selectedValue[0] = i;
                        }
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    } );
                    builder.setPositiveButton("save",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseHelper.updateData(mangaLibrary.getMangaId(), status[selectedValue[0]]);
                            submitList(databaseHelper.getAllData());
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            });

        }

    }
}
