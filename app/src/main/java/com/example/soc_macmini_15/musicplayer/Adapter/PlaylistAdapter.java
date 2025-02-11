package com.example.soc_macmini_15.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.soc_macmini_15.musicplayer.Model.Playlist;
import com.example.soc_macmini_15.musicplayer.R;
import com.example.soc_macmini_15.musicplayer.DB.PlaylistOperations;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {
    private Context mContext;
    private ArrayList<Playlist> playlists;
    private PlaylistOperations playlistOperations;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        super(context, 0, playlists);
        this.mContext = context;
        this.playlists = playlists;
        this.playlistOperations = new PlaylistOperations(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.playlist_item, parent, false);
        }

        Playlist currentPlaylist = playlists.get(position);
        TextView playlistName = listItem.findViewById(R.id.tv_playlist_name);
        TextView songCount = listItem.findViewById(R.id.tv_song_count);
        
        int count = playlistOperations.getPlaylistSongs(currentPlaylist.getId()).size();
        playlistName.setText(currentPlaylist.getName());
        songCount.setText(count + " songs");

        return listItem;
    }
} 