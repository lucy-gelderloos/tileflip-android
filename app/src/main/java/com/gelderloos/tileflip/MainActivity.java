package com.gelderloos.tileflip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gelderloos.tileflip.adapters.TileGridViewAdapter;
import com.gelderloos.tileflip.fragments.InstructionsFragment;
import com.gelderloos.tileflip.fragments.WinGameDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String HIGH_SCORE_EASY_TAG = "HIGH_SCORE_EASY_TAG";
    public static final String HIGH_SCORE_MEDIUM_TAG = "HIGH_SCORE_MEDIUM_TAG";
    public static final String HIGH_SCORE_HARD_TAG = "HIGH_SCORE_HARD_TAG";

    SharedPreferences preferences;
    Context context;
    RadioGroup difficultyGroup;
    RadioButton difficultyRadioButton;
    WinGameDialogFragment winGameDialogFragment;

//User variables
    int highScoreEasy;
    int highScoreMedium;
    int highScoreHard;
//Game variables
    TextView scoreView;
    ImageView discardPile;
    int penalty = -10;
    int matchesLeft;
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
        RadioButton easyRadio = findViewById(R.id.radioButtonEasyMain);
        easyRadio.setChecked(true);

//        winGameDialogFragment.setTries(15);
//        winGameDialogFragment.setScore(250);
//        winGameDialogFragment.show(getSupportFragmentManager(),"win_game");

        setUpNewGameButton();
        setUpInstructionsButton();
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
            matchesLeft = difficulty / 2;
            discardPile.setImageResource(0);
            winGameDialogFragment = new WinGameDialogFragment();
            winGameDialogFragment.setContext(context);
            generateTiles();
            setUpTileGridView();
        });
    }

    private void setUpInstructionsButton() {
        Button instructionsButton = findViewById(R.id.howToPlayButton);
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        instructionsButton.setOnClickListener(view -> {
            instructionsFragment.show(getSupportFragmentManager(),"instructions_fragment");
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
        if(difficulty == 24) {
            tiles.add(12, new Tile(0, "0"));
        }
    }

    private void setUpTileGridView() {
        int boardWidth;
        ConstraintLayout tileBoard = findViewById(R.id.layoutTileBoardMain);
        ViewGroup.LayoutParams tileBoardParams = tileBoard.getLayoutParams();

        switch (difficulty) {
            case 24:
                boardWidth = 5;
                tileBoardParams.height = 1150;
                tileBoard.setLayoutParams(tileBoardParams);
                break;
            case 36:
                boardWidth = 6;
                tileBoardParams.height = 1400;
                tileBoard.setLayoutParams(tileBoardParams);
                break;
            default:
                boardWidth = 4;
                tileBoardParams.height = 950;
                tileBoard.setLayoutParams(tileBoardParams);
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
        matchesLeft--;
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
        if(matchesLeft == 0) {
            gameOver();
        }
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
        score += points;
        winGameDialogFragment.setScore(score);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            scoreView.setText(getString(R.string.score_string,score));
        }, 1000);
    }

    private void gameOver() {
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        int highScore;
        switch (difficulty) {
            case 16:
                highScore = preferences.getInt(HIGH_SCORE_EASY_TAG,0);
                if(score > highScore) {
                    preferenceEditor.putInt(HIGH_SCORE_EASY_TAG,score);
                    winGameDialogFragment.setNewHighScore(true);
                }
                preferenceEditor.apply();
                break;
            case 24:
                highScore = preferences.getInt(HIGH_SCORE_MEDIUM_TAG,0);
                if(score > highScore) {
                    preferenceEditor.putInt(HIGH_SCORE_MEDIUM_TAG,score);
                    winGameDialogFragment.setNewHighScore(true);
                }
                preferenceEditor.apply();
                break;
            case 36:
                highScore = preferences.getInt(HIGH_SCORE_HARD_TAG,0);
                if(score > highScore) {
                    preferenceEditor.putInt(HIGH_SCORE_HARD_TAG,score);
                    winGameDialogFragment.setNewHighScore(true);
                }
                preferenceEditor.apply();
                break;
            default:
                highScore = preferences.getInt(HIGH_SCORE_EASY_TAG,0);
                if(score > highScore) {
                    preferenceEditor.putInt(HIGH_SCORE_EASY_TAG,score);
                    winGameDialogFragment.setNewHighScore(true);
                }
                preferenceEditor.apply();
        }
        winGameDialogFragment.show(getSupportFragmentManager(),"win_game");
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
            if (!this.isFlipped() && !this.isMatchFound() && !secondClicked && !this.tileValue.equals("0")) {
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