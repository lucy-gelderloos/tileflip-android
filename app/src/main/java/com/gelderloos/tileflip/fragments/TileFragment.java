package com.gelderloos.tileflip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelderloos.tileflip.R;

public class TileFragment extends Fragment {

    public TileFragment() {
        // Required empty public constructor
    }

    public static TileFragment newInstance(String tileId, String tileValue) {
        return new TileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tile, container, false);
    }
}