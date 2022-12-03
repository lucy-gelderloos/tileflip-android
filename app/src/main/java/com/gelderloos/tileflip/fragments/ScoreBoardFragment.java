package com.gelderloos.tileflip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelderloos.tileflip.R;

public class ScoreBoardFragment extends Fragment {

    public ScoreBoardFragment() {
    }

    public static ScoreBoardFragment newInstance(String param1, String param2) {
        return new ScoreBoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score_board, container, false);
    }
}