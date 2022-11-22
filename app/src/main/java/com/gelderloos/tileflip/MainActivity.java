package com.gelderloos.tileflip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.gelderloos.tileflip.adapters.TileRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences preferences;
    List<Tile> tiles = null;
    TileRecyclerViewAdapter adapter;
    String[] difficultyArr = new String[]{"Easy","Medium","Hard"};
    Spinner difficultySpinner = null;

    private int easyMatchPoint = 50, medMatchPoint = 75, hardMatchPoint = 100, noMatchPenalty = -10;
    private int maxTiles = 18;
    int difficulty = 16;
//    attempts, matchesLeft, score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiles = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpNewGameButton();
        setUpDifficultySpinner();
//        generateTiles();
//        setUpTileRecyclerView();
    }

    private void setUpDifficultySpinner() {
        difficultySpinner = findViewById(R.id.spinnerDifficultySelectorMain);
        difficultySpinner.setAdapter(new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                difficultyArr
        ));
    }

    private void setUpNewGameButton() {
        Button newGameButton = findViewById(R.id.buttonNewGameMain);
        newGameButton.setOnClickListener(view -> {
            String selectedDifficulty = difficultySpinner.getSelectedItem().toString();
            switch (selectedDifficulty) {
                case "Easy": difficulty = 16;
                break;
                case "Medium": difficulty = 24;
                break;
                case "Hard": difficulty = 36;
                break;
                default: difficulty = 24;
            }
            generateTiles();
            setUpTileRecyclerView();
        });
    }

    private void generateTiles() {
        //      create an ArrayList of all possible tile values
        ArrayList<Integer> possibleValues = new ArrayList<>();
        for(int i = 1; i <= maxTiles; i++) {
            possibleValues.add(i);
        }
        //      randomly select which tiles will be on the board and add two of each to the tilesArray
        ArrayList<Integer> tilesArray = new ArrayList<>();
        for(int i = 0; i < (difficulty / 2); i++) {
            int randIndex = (int) Math.floor(Math.random() * possibleValues.size());
            tilesArray.add(possibleValues.get(randIndex));
            tilesArray.add(possibleValues.get(randIndex));
            possibleValues.remove(randIndex);
        }
        //      shuffle the tilesArray
        int len = tilesArray.size();
        int j, temp;
        while(len > 0) {
            len--;
            j  = (int) Math.floor(Math.random() * len);
            temp = tilesArray.get(len);
            tilesArray.set(len,tilesArray.get(j));
            tilesArray.set(j,temp);
        }
        //      create list of Tiles
        for(int i = 1; i <= tilesArray.size(); i++) {
            tiles.add(new Tile(i, "tile" + tilesArray.get(i - 1)));
        }
    }

    private void setUpTileRecyclerView() {
        int boardWidth;
        switch(difficulty) {
            case 16: boardWidth = 4;
            break;
            case 24: boardWidth = 5;
            break;
            case 36: boardWidth = 6;
            break;
            default: boardWidth = 5;
        }
        RecyclerView tileRecyclerView = findViewById(R.id.mainTileRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,boardWidth);
        tileRecyclerView.setLayoutManager(layoutManager);
        adapter = new TileRecyclerViewAdapter(tiles, this);
        tileRecyclerView.setAdapter(adapter);
    }
}