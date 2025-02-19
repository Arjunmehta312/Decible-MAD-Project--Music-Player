package com.example.soc_macmini_15.musicplayer.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.soc_macmini_15.musicplayer.Adapter.PlaylistAdapter;
import com.example.soc_macmini_15.musicplayer.Adapter.SongAdapter;
import com.example.soc_macmini_15.musicplayer.DB.PlaylistOperations;
import com.example.soc_macmini_15.musicplayer.Model.Playlist;
import com.example.soc_macmini_15.musicplayer.Model.SongsList;
import com.example.soc_macmini_15.musicplayer.R;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {
    private PlaylistOperations playlistOperations;
    private ListView listView;
    private ArrayList<Playlist> playlists;
    private PlaylistAdapter playlistAdapter;
    private createPlaylistDialog createPlaylistDialog;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        PlaylistFragment tabFragment = new PlaylistFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            createPlaylistDialog = (createPlaylistDialog) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement createPlaylistDialog");
        }
        playlistOperations = new PlaylistOperations(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.playlist_list_view);
        FloatingActionButton addPlaylist = view.findViewById(R.id.btn_add_playlist);
        
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreatePlaylistDialog();
            }
        });

        setContent();
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPlaylistSongs(playlists.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPlaylistOptionsDialog(position);
                return true;
            }
        });
    }

    private void setContent() {
        try {
            playlists = playlistOperations.getPlaylists();
            if (playlists == null) {
                playlists = new ArrayList<>();
            }
            playlistAdapter = new PlaylistAdapter(getContext(), playlists);
            if (listView != null) {
                listView.setAdapter(playlistAdapter);
            }
            playlistAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error loading playlists", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCreatePlaylistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Create New Playlist");

        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = input.getText().toString();
                if (!playlistName.isEmpty()) {
                    playlistOperations.createPlaylist(playlistName);
                    setContent();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void showPlaylistOptionsDialog(final int position) {
        final Playlist playlist = playlists.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] options = {"Delete Playlist", "Rename Playlist"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        deletePlaylist(playlist);
                        break;
                    case 1:
                        showRenamePlaylistDialog(playlist);
                        break;
                }
            }
        });
        builder.show();
    }

    private void deletePlaylist(Playlist playlist) {
        playlistOperations.deletePlaylist(playlist.getId());
        setContent();
    }

    private void showRenamePlaylistDialog(final Playlist playlist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rename Playlist");

        final EditText input = new EditText(getContext());
        input.setText(playlist.getName());
        builder.setView(input);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                if (!newName.isEmpty()) {
                    playlist.setName(newName);
                    playlistOperations.updatePlaylistName(playlist.getId(), newName);
                    setContent();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    private void showPlaylistSongs(final Playlist playlist) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(playlist.getName());

        final ArrayList<SongsList> songs = playlistOperations.getPlaylistSongs(playlist.getId());
        if (songs.isEmpty()) {
            Toast.makeText(getContext(), "No songs in playlist", Toast.LENGTH_SHORT).show();
            return;
        }

        final SongAdapter adapter = new SongAdapter(getContext(), songs);
        ListView songListView = new ListView(getContext());
        songListView.setAdapter(adapter);

        builder.setView(songListView);
        final AlertDialog dialog = builder.create();

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createPlaylistDialog.onDataPass(songs.get(position).getTitle(), songs.get(position).getPath());
                createPlaylistDialog.fullSongList(songs, position);
                dialog.dismiss();
            }
        });

        songListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showSongOptionsDialog(playlist, songs.get(position));
                return true;
            }
        });

        dialog.show();
    }

    private void showSongOptionsDialog(final Playlist playlist, final SongsList song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] options = {"Remove from playlist"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        playlistOperations.removeSongFromPlaylist(playlist.getId(), song.getPath());
                        Toast.makeText(getContext(), "Song removed from playlist", Toast.LENGTH_SHORT).show();
                        setContent();
                        break;
                }
            }
        });
        builder.show();
    }

    public interface createPlaylistDialog {
        void onDataPass(String name, String path);
        void fullSongList(ArrayList<SongsList> songList, int position);
    }

    public void refreshPlaylist() {
        setContent();
    }
} 