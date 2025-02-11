package com.example.soc_macmini_15.musicplayer.Model;

public class Playlist {
    private long id;
    private String name;

    public Playlist(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 