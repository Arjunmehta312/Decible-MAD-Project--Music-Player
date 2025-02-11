package com.example.soc_macmini_15.musicplayer.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.soc_macmini_15.musicplayer.Model.Playlist;
import com.example.soc_macmini_15.musicplayer.Model.SongsList;

import java.util.ArrayList;

public class PlaylistOperations {
    private PlaylistDBHandler dbHandler;
    private SQLiteDatabase database;

    public PlaylistOperations(Context context) {
        dbHandler = new PlaylistDBHandler(context);
    }

    public void open() {
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        dbHandler.close();
    }

    public void createPlaylist(String playlistName) {
        open();
        ContentValues values = new ContentValues();
        values.put(PlaylistDBHandler.COLUMN_PLAYLIST_NAME, playlistName);
        database.insert(PlaylistDBHandler.TABLE_PLAYLISTS, null, values);
        close();
    }

    public void addSongToPlaylist(long playlistId, SongsList song) {
        open();
        ContentValues values = new ContentValues();
        values.put(PlaylistDBHandler.COLUMN_PLAYLIST_ID, playlistId);
        values.put(PlaylistDBHandler.COLUMN_TITLE, song.getTitle());
        values.put(PlaylistDBHandler.COLUMN_SUBTITLE, song.getSubTitle());
        values.put(PlaylistDBHandler.COLUMN_PATH, song.getPath());
        
        database.insertWithOnConflict(PlaylistDBHandler.TABLE_PLAYLIST_SONGS, null, 
                                    values, SQLiteDatabase.CONFLICT_REPLACE);
        close();
    }

    public ArrayList<Playlist> getPlaylists() {
        open();
        ArrayList<Playlist> playlists = new ArrayList<>();
        
        Cursor cursor = database.query(PlaylistDBHandler.TABLE_PLAYLISTS,
                new String[]{PlaylistDBHandler.COLUMN_PLAYLIST_ID, PlaylistDBHandler.COLUMN_PLAYLIST_NAME},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(PlaylistDBHandler.COLUMN_PLAYLIST_ID));
                String name = cursor.getString(cursor.getColumnIndex(PlaylistDBHandler.COLUMN_PLAYLIST_NAME));
                playlists.add(new Playlist(id, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return playlists;
    }

    public ArrayList<SongsList> getPlaylistSongs(long playlistId) {
        open();
        ArrayList<SongsList> songs = new ArrayList<>();
        
        Cursor cursor = database.query(PlaylistDBHandler.TABLE_PLAYLIST_SONGS,
                null,
                PlaylistDBHandler.COLUMN_PLAYLIST_ID + "=?",
                new String[]{String.valueOf(playlistId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(PlaylistDBHandler.COLUMN_TITLE));
                String subtitle = cursor.getString(cursor.getColumnIndex(PlaylistDBHandler.COLUMN_SUBTITLE));
                String path = cursor.getString(cursor.getColumnIndex(PlaylistDBHandler.COLUMN_PATH));
                songs.add(new SongsList(title, subtitle, path));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return songs;
    }

    public void deletePlaylist(long playlistId) {
        open();
        database.delete(PlaylistDBHandler.TABLE_PLAYLISTS,
                PlaylistDBHandler.COLUMN_PLAYLIST_ID + "=?",
                new String[]{String.valueOf(playlistId)});
        close();
    }

    public void removeSongFromPlaylist(long playlistId, String songPath) {
        open();
        database.delete(PlaylistDBHandler.TABLE_PLAYLIST_SONGS,
                PlaylistDBHandler.COLUMN_PLAYLIST_ID + "=? AND " + PlaylistDBHandler.COLUMN_PATH + "=?",
                new String[]{String.valueOf(playlistId), songPath});
        close();
    }

    public void updatePlaylistName(long playlistId, String newName) {
        open();
        ContentValues values = new ContentValues();
        values.put(PlaylistDBHandler.COLUMN_PLAYLIST_NAME, newName);
        database.update(PlaylistDBHandler.TABLE_PLAYLISTS,
                values,
                PlaylistDBHandler.COLUMN_PLAYLIST_ID + "=?",
                new String[]{String.valueOf(playlistId)});
        close();
    }
} 