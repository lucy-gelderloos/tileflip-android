package com.gelderloos.tileflip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelderloos.tileflip.R;

public class Tile extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TILEID = "tileId";
    private static final String TILEVALUE = "tileValue";

    private String mTileId;
    private String mTileValue;

    public Tile() {
        // Required empty public constructor
    }

    public static Tile newInstance(String param1, String param2) {
        Tile fragment = new Tile();
        Bundle args = new Bundle();
        args.putString(TILEID, param1);
        args.putString(TILEVALUE, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTileId = getArguments().getString(TILEID);
            mTileValue = getArguments().getString(TILEVALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tile, container, false);
    }
}