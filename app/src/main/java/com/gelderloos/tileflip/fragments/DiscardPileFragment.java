package com.gelderloos.tileflip.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelderloos.tileflip.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscardPileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscardPileFragment extends Fragment {

    public DiscardPileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiscardPileFragment newInstance(String param1, String param2) {
        DiscardPileFragment fragment = new DiscardPileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discard_pile, container, false);
    }
}