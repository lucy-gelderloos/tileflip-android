package com.gelderloos.tileflip;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gelderloos.tileflip.adapters.TileGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences preferences;
    Context context;
//Game variables
    TextView scoreView;
    ImageView discardPile;
    int penalty = -10;

    RadioGroup difficultyGroup;
    RadioButton difficultyRadioButton;
    RadioButton buttonEasy;
    RadioButton buttonMedium;
    RadioButton buttonHard;
//Round variables
    List<Tile> tiles = null;
    int difficulty = 16;
    int matchPoint = 50;
    int score;
//Turn variables
    String currentValue = "";
    Tile firstTile = null;
    ImageView firstTileFront;
    ImageView firstTileBack;
    boolean secondClicked = false;
    Tile secondTile = null;
    ImageView secondTileFront;
    ImageView secondTileBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        scoreView = findViewById(R.id.textViewScoreBoardScore);
        discardPile = findViewById(R.id.imageViewDiscardPileTile);
        context = this.getApplicationContext();
        difficultyGroup = (RadioGroup) findViewById(R.id.radioGroupDifficultyMain);

        setUpNewGameButton();
        setUpDifficultyRadio();
    }

    private void setUpDifficultyRadio() {
        difficultyGroup = findViewById(R.id.radioGroupDifficultyMain);
        buttonEasy = findViewById(R.id.radioButtonEasyMain);
        buttonMedium = findViewById(R.id.radioButtonMediumMain);
        buttonHard = findViewById(R.id.radioButtonHardMain);
    }

    public void onRadioButtonClicked(View view) {
        int mediumId = R.id.radioButtonMediumMain;
        int hardId = R.id.radioButtonHardMain;
        int selectedId = difficultyGroup.getCheckedRadioButtonId();
        difficultyRadioButton = findViewById(selectedId);
        if(selectedId == mediumId) {
            difficulty = 24;
            matchPoint = 75;
        } else if(selectedId == hardId) {
            difficulty = 36;
            matchPoint = 100;
        } else {
            difficulty = 16;
            matchPoint = 50;
        }
    }

    private void setUpNewGameButton() {
        Button newGameButton = findViewById(R.id.buttonNewGameMain);
        newGameButton.setOnClickListener(view -> {
            tiles = new ArrayList<>();
            score = 0;
            scoreView.setText(getString(R.string.score_string,score));
            discardPile.setImageResource(0);
            generateTiles();
            setUpTileGridView();
        });
    }

    private void generateTiles() {
        ArrayList<Integer> possibleValues = new ArrayList<>();
        int maxTiles = 18;
        for (int i = 1; i <= maxTiles; i++) {
            possibleValues.add(i);
        }
        ArrayList<Integer> tilesArray = new ArrayList<>();
        for (int i = 0; i < (difficulty / 2); i++) {
            int randIndex = (int) Math.floor(Math.random() * possibleValues.size());
            tilesArray.add(possibleValues.get(randIndex));
            tilesArray.add(possibleValues.get(randIndex));
            possibleValues.remove(randIndex);
        }
        int len = tilesArray.size();
        int j, temp;
        while (len > 0) {
            len--;
            j = (int) Math.floor(Math.random() * len);
            temp = tilesArray.get(len);
            tilesArray.set(len, tilesArray.get(j));
            tilesArray.set(j, temp);
        }
        for (int i = 1; i <= tilesArray.size(); i++) {
            tiles.add(new Tile(i, "tile" + tilesArray.get(i - 1)));
        }
    }

    private void setUpTileGridView() {
        int boardWidth;
        switch (difficulty) {
            case 24:
                boardWidth = 5;
                break;
            case 36:
                boardWidth = 6;
                break;
            default:
                boardWidth = 4;
        }
        GridView tileGridView = findViewById(R.id.gridViewTileBoard);
        tileGridView.setNumColumns(boardWidth);
        TileGridViewAdapter adapter = new TileGridViewAdapter(this, tiles);
        tileGridView.setAdapter(adapter);
    }

    public void flipTile(Context context, View visibleView, View invisibleView) {
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
    }

    public void flipBack(Context context, View visibleView, View invisibleView) {
        visibleView.setVisibility(View.VISIBLE);
        float scale = context.getResources().getDisplayMetrics().density;
        float cameraDist = 8000 * scale;
        invisibleView.setCameraDistance(cameraDist);
        visibleView.setCameraDistance(cameraDist);
        AnimatorSet flipOutAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.back_animator_reverse);
        flipOutAnimatorSet.setTarget(invisibleView);
        AnimatorSet flipInAnimatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.front_animator_reverse);
        flipInAnimatorSet.setTarget(visibleView);
        flipOutAnimatorSet.start();
        flipInAnimatorSet.start();
    }

    public void matchFound() {
        updateScore(matchPoint);
        firstTile.setMatchFound(true);
        secondTile.setMatchFound(true);
        firstTileBack.setImageResource(R.drawable.matchfound);
        secondTileBack.setImageResource(R.drawable.matchfound);

        int discardImg = context.getResources().getIdentifier(firstTile.getTileValue(), "drawable", context.getPackageName());
        if (discardImg == 0) {
            discardImg = context.getResources().getIdentifier("tileBack", "drawable", context.getPackageName());
        }
        Handler handler = new Handler();
        int finalDiscardImg = discardImg;
        handler.postDelayed(() -> discardPile.setImageResource(finalDiscardImg), 1500);
    }

    public void matchNotFound() {
        updateScore(penalty);
    }

    public void reset(Context context) {
        flipBack(context, firstTileBack, firstTileFront);
        flipBack(context, secondTileBack, secondTileFront);
        currentValue = "";
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            secondClicked = false;
            firstTile.setFlipped(false);
            secondTile.setFlipped(false);
        }, 1500);
    }

    private void updateScore(int points) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            score += points;
            scoreView.setText(getString(R.string.score_string,score));
        }, 1000);
    }

    public class Tile {
        private int tileId;
        private String tileValue;
        private boolean flipped;
        private boolean matchFound;

        Tile(int tileId, String tileValue) {
            this.tileId = tileId;
            this.tileValue = tileValue;
            this.flipped = false;
            this.matchFound = false;
        }

        public void checkForMatch(Context context, ImageView backView, ImageView frontView) {
            if (!this.isFlipped() && !this.isMatchFound() && !secondClicked) {
                this.setFlipped(true);
                flipTile(context, backView, frontView);
                if (currentValue.equals("")) {
                    currentValue = this.tileValue;
                    firstTile = this;
                    firstTileBack = backView;
                    firstTileFront = frontView;
                } else {
                    secondTile = this;
                    secondTileBack = backView;
                    secondTileFront = frontView;
                    secondClicked = true;

                    if (currentValue.equals(this.getTileValue())) {
                        Handler handler = new Handler();
                        handler.postDelayed(MainActivity.this::matchFound, 500);
                    } else matchNotFound();
                    reset(context);
                }
            }
        }

        public int getTileId() {
            return tileId;
        }

        public void setTileId(int tileId) {
            this.tileId = tileId;
        }

        public String getTileValue() {
            return tileValue;
        }

        public void setTileValue(String tileValue) {
            this.tileValue = tileValue;
        }

        public boolean isFlipped() {
            return flipped;
        }

        public void setFlipped(boolean flipped) {
            this.flipped = flipped;
        }

        public boolean isMatchFound() {
            return matchFound;
        }

        public void setMatchFound(boolean matchFound) {
            this.matchFound = matchFound;
        }
    }
}