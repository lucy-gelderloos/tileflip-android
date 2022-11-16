package com.gelderloos.tileflip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.gelderloos.tileflip.adapters.TileRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences preferences;
    List<Tile> tiles = null;
    TileRecyclerViewAdapter adapter;


    private int easyMatchPoint = 50, medMatchPoint = 75, hardMatchPoint = 100, noMatchPenalty = -10;
    private int maxTiles = 18;
    int difficulty, firstTileId = 0, firstTileValue = 0, secondTileId = 0, secondTileValue = 0, attempts, matchesLeft, score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiles = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpNewGameButton();
        generateTiles();
        setUpTileRecyclerView();
    }

    public class Tile {
        private String tileId;
        private String tileValue;

        Tile(String tileId, String tileValue) {
            this.tileId = tileId;
            this.tileValue = tileValue;
        }
    }

    private void setUpNewGameButton() {
//        get button & add click listener
    }

    private void generateTiles() {
//      create an ArrayList of all possible tile values
        ArrayList<Integer> possibleValues = new ArrayList<>();
        for(int i = 1; i <= maxTiles; i++) {
            possibleValues.add(i);
        }
//      randomly select which tiles will be on the board and add two of each to the tilesArray
        int[] tilesArray = new int[difficulty];
        int k = 0;
        for(int i = 0; i < difficulty / 2; i++) {
            int randIndex = (int) Math.floor(Math.random() * possibleValues.size());
            tilesArray[k] = possibleValues.get(randIndex);
            k++;
            tilesArray[k] = possibleValues.get(randIndex);
            k++;
            possibleValues.remove(randIndex);
        }
//      shuffle the tilesArray
        int len = tilesArray.length;
        int j, temp;
        while(len > 0) {
            len--;
            j  = (int) Math.floor(Math.random() * len);
            temp = tilesArray[len];
            tilesArray[len] = tilesArray[j];
            tilesArray[j] = temp;
        }
//      create list of Tiles
        for(int i = 1; i <= tilesArray.length; i++) {
            tiles.add(new Tile("tile" + i, "tile" + tilesArray[i - 1]));
        }
    }

    private void setUpTileRecyclerView() {
        RecyclerView tileRecyclerView = findViewById(R.id.mainTileRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,4);
        tileRecyclerView.setLayoutManager(layoutManager);
        adapter = new TileRecyclerViewAdapter(tiles, this);
        tileRecyclerView.setAdapter(adapter);
    }
}