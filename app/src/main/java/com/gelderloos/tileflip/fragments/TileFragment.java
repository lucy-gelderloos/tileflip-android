package com.gelderloos.tileflip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class TileFragment extends Fragment {

    public TileFragment() {

    }

    public static TileFragment newInstance(String param1, String param2) {
        TileFragment fragment = new TileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
