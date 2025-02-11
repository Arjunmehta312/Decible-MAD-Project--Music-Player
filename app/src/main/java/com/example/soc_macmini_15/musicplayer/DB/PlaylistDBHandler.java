package com.example.soc_macmini_15.musicplayer.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "playlists.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PLAYLISTS = "playlists";
    public static final String COLUMN_PLAYLIST_ID = "playlistId";
    public static final String COLUMN_PLAYLIST_NAME = "playlistName";
    
    public static final String TABLE_PLAYLIST_SONGS = "playlist_songs";
    public static final String COLUMN_SONG_ID = "songId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SUBTITLE = "subtitle";
    public static final String COLUMN_PATH = "songpath";

    private static final String CREATE_PLAYLISTS_TABLE = "CREATE TABLE " + TABLE_PLAYLISTS + " ("
            + COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PLAYLIST_NAME + " TEXT UNIQUE" + ")";

    private static final String CREATE_PLAYLIST_SONGS_TABLE = "CREATE TABLE " + TABLE_PLAYLIST_SONGS + " ("
            + COLUMN_PLAYLIST_ID + " INTEGER, "
            + COLUMN_SONG_ID + " INTEGER, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_SUBTITLE + " TEXT, "
            + COLUMN_PATH + " TEXT, "
            + "PRIMARY KEY (" + COLUMN_PLAYLIST_ID + ", " + COLUMN_PATH + "), "
            + "FOREIGN KEY (" + COLUMN_PLAYLIST_ID + ") REFERENCES " + TABLE_PLAYLISTS + "(" + COLUMN_PLAYLIST_ID + ") ON DELETE CASCADE)";

    public PlaylistDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PLAYLISTS_TABLE);
        db.execSQL(CREATE_PLAYLIST_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST_SONGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        onCreate(db);
    }
} 