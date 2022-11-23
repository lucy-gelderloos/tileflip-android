package com.gelderloos.tileflip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelderloos.tileflip.R;

public class TileBoardFragment extends Fragment {

    public TileBoardFragment() {
        // Required empty public constructor
    }

    public static TileBoardFragment newInstance(String param1, String param2) {
        TileBoardFragment fragment = new TileBoardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tile_board, container, false);
    }
}