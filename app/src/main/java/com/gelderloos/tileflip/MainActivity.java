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
import android.widget.ImageView;

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
    int difficulty = 8;
//    int difficulty, firstTileId = 0, firstTileValue = 0, secondTileId = 0, secondTileValue = 0, attempts, matchesLeft, score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiles = new ArrayList<>();
        System.out.println(tiles);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpNewGameButton();
        generateTiles();
        setUpTileRecyclerView();
        setUpTestImage();
    }

    public static class Tile {
        String tileId;
        String tileValue;

        Tile(String tileId, String tileValue) {
            this.tileId = tileId;
            this.tileValue = tileValue;
        }
    }

    private void setUpNewGameButton() {
//        get button & add click listener
    }

    private void setUpTestImage() {
        ImageView testImageViewFront = findViewById(R.id.mainTestImageImageViewFront);
        ImageView testImageViewBack = findViewById(R.id.mainTestImageImageViewBack);
        testImageViewFront.setOnClickListener(view -> {
            flipTile(this,testImageViewFront,testImageViewBack);
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
            temp = tilesArray.size();
            tilesArray.set(len,tilesArray.get(j));
            tilesArray.set(j,temp);
        }
//      create list of Tiles
        for(int i = 1; i <= tilesArray.size(); i++) {
            tiles.add(new Tile("tile" + i, "tile" + tilesArray.get(i - 1)));
        }
    }

    private void flipTile(Context context, View visibleView, View invisibleView) {
//        https://medium.com/geekculture/how-to-add-card-flip-animation-in-the-android-app-3060afeadd45
        visibleView.setVisibility(View.VISIBLE);
        float scale = context.getResources().getDisplayMetrics().density;
        float cameraDist = 8000 * scale;
        invisibleView.setCameraDistance(cameraDist);
        visibleView.setCameraDistance(cameraDist);
        AnimatorSet flipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.back_animator);
        flipOutAnimatorSet.setTarget(invisibleView);
        AnimatorSet flipInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.front_animator);
        flipInAnimatorSet.setTarget(visibleView);
        flipOutAnimatorSet.start();
        flipInAnimatorSet.start();
//        need java version of flipInAnimatorSet.doOnEnd { invisibleView.gone() }

    }

    private void setUpTileRecyclerView() {
        RecyclerView tileRecyclerView = findViewById(R.id.mainTileRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this,4);
        tileRecyclerView.setLayoutManager(layoutManager);
        adapter = new TileRecyclerViewAdapter(tiles, this);
        tileRecyclerView.setAdapter(adapter);
    }
}