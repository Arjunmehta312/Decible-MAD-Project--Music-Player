package com.example.soc_macmini_15.musicplayer.Adapter;

import android.content.ContentResolver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.soc_macmini_15.musicplayer.Fragments.AllSongFragment;
import com.example.soc_macmini_15.musicplayer.Fragments.CurrentSongFragment;
import com.example.soc_macmini_15.musicplayer.Fragments.FavSongFragment;
import com.example.soc_macmini_15.musicplayer.Fragments.PlaylistFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ContentResolver contentResolver;
    private String title[] = {"ALL SONGS", "CURRENT PLAYLIST", "FAVORITES", "PLAYLISTS"};

    public ViewPagerAdapter(FragmentManager fm, ContentResolver contentResolver) {
        super(fm);
        this.contentResolver = contentResolver;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AllSongFragment.getInstance(position, contentResolver);
            case 1:
                return CurrentSongFragment.getInstance(position);
            case 2:
                return FavSongFragment.getInstance(position);
            case 3:
                return PlaylistFragment.getInstance(position);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
