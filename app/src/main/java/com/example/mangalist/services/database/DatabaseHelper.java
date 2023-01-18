package com.example.mangalist.services.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mangalist.model.MangaLibrary;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "manga_title";
    private static final String COLUMN_STATUS = "manga_status";
    private static final String COLUMN_IMAGE_URL = "manga_image_url";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addBook(Integer id, String title, String author, String image_url) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_STATUS, author);
        contentValues.put(COLUMN_IMAGE_URL, image_url);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        return true;
    }

    public List<MangaLibrary> getAllData() {
        List<MangaLibrary> temp = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
            if(cursor.moveToFirst()){
                do {
                    MangaLibrary mangaLibrary = new MangaLibrary();
                    mangaLibrary.setMangaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    mangaLibrary.setMangaTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    mangaLibrary.setMangaStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                    mangaLibrary.setMangaImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
                    temp.add(mangaLibrary);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        return temp;
    }

    public void updateData(Integer id, String status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);

        long result = sqLiteDatabase.update(TABLE_NAME, contentValues, "_id=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Failed to Update.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRow(Integer id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public MangaLibrary isExist(Integer id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            boolean _isExist = cursor.getCount() > 0;
            MangaLibrary mangaLibrary = new MangaLibrary();
            if(_isExist && cursor.moveToFirst()){
                mangaLibrary.setMangaId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                mangaLibrary.setMangaTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                mangaLibrary.setMangaStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                mangaLibrary.setMangaImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)));
                cursor.close();
                return mangaLibrary;
            }
            cursor.close();
        }
        return null;
    }

    public void deleteAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
