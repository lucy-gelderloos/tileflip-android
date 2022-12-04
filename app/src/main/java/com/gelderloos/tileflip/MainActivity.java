package com.gelderloos.tileflip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gelderloos.tileflip.adapters.TileGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences preferences;
    List<Tile> tiles = null;
    String[] difficultyArr = new String[]{"Easy","Medium","Hard"};
    Spinner difficultySpinner = null;
    int difficulty;
    int matchPoint;
    TileBoard gameBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpNewGameButton();
        setUpDifficultySpinner();
    }

    private void setUpDifficultySpinner() {
        difficultySpinner = findViewById(R.id.spinnerDifficultySelectorSettings);
        difficultySpinner.setAdapter(new ArrayAdapter<>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                difficultyArr
        ));
    }

    private void setUpNewGameButton() {
        Button newGameButton = findViewById(R.id.buttonNewGameMain);
//        Button newGameButton = findViewById(R.id.buttonNewGameSettings);
        newGameButton.setOnClickListener(view -> {
            tiles = new ArrayList<>();
            String selectedDifficulty = difficultySpinner.getSelectedItem().toString();
            switch (selectedDifficulty) {
                case "Easy": difficulty = 16;
                matchPoint = 50;
                break;
                case "Medium": difficulty = 24;
                matchPoint = 75;
                break;
                case "Hard": difficulty = 36;
                matchPoint = 100;
                break;
                default: difficulty = 24;
                matchPoint = 75;
            }
            TextView scoreView = findViewById(R.id.textViewScoreBoardScore);
            ImageView discardPile = findViewById(R.id.imageViewDiscardPileTile);
            discardPile.setImageResource(R.drawable.tileback);
            gameBoard = new TileBoard();
            gameBoard.setScoreView(scoreView);
            gameBoard.setDiscardPile(discardPile);
            gameBoard.setContext(getApplicationContext());
            gameBoard.setMatchPoint(matchPoint);
            generateTiles();
            setUpTileGridView();
        });
    }

    private void generateTiles() {
        ArrayList<Integer> possibleValues = new ArrayList<>();
        int maxTiles = 18;
        for(int i = 1; i <= maxTiles; i++) {
            possibleValues.add(i);
        }
        ArrayList<Integer> tilesArray = new ArrayList<>();
        for(int i = 0; i < (difficulty / 2); i++) {
            int randIndex = (int) Math.floor(Math.random() * possibleValues.size());
            tilesArray.add(possibleValues.get(randIndex));
            tilesArray.add(possibleValues.get(randIndex));
            possibleValues.remove(randIndex);
        }
        int len = tilesArray.size();
        int j, temp;
        while(len > 0) {
            len--;
            j  = (int) Math.floor(Math.random() * len);
            temp = tilesArray.get(len);
            tilesArray.set(len,tilesArray.get(j));
            tilesArray.set(j,temp);
        }
        for(int i = 1; i <= tilesArray.size(); i++) {
            tiles.add(new Tile(i, "tile" + tilesArray.get(i - 1),gameBoard));
        }
    }

    private void setUpTileGridView() {
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
        GridView tileGridView = findViewById(R.id.gridViewTileBoard);
        tileGridView.setNumColumns(boardWidth);
        TileGridViewAdapter adapter = new TileGridViewAdapter(this, tiles);
        tileGridView.setAdapter(adapter);
    }

}