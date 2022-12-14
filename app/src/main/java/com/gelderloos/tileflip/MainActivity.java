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
import android.util.TypedValue;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String HIGH_SCORE_EASY_TAG = "HIGH_SCORE_EASY_TAG";
    public static final String HIGH_SCORE_MEDIUM_TAG = "HIGH_SCORE_MEDIUM_TAG";
    public static final String HIGH_SCORE_HARD_TAG = "HIGH_SCORE_HARD_TAG";
    public static final String DIFFICULTY_TAG = "DIFFICULTY_TAG";
    private AdView mAdView;

    SharedPreferences preferences;
    Context context;
    RadioGroup difficultyGroup;
    RadioButton difficultyRadioButton;
    WinGameDialogFragment winGameDialogFragment;

//Game variables
    TextView scoreView;
    ImageView discardPile;
    ImageView discardedTile;
    int penalty = -10;
    int easyMatchPoint = 50;
    int mediumMatchPoint = 75;
    int hardMatchPoint = 100;
//Round variables
    List<Tile> tiles = null;
    int difficulty;
    int matchesLeft;
    int matchPoint = easyMatchPoint;
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains(DIFFICULTY_TAG)) {
            difficulty = preferences.getInt(DIFFICULTY_TAG,16);
        } else difficulty = 16;
        scoreView = findViewById(R.id.textViewScoreBoardScore);
        discardPile = findViewById(R.id.imageViewDiscardPileTile);
        discardedTile = findViewById(R.id.imageViewDiscardedTile);
        context = this.getApplicationContext();
        difficultyGroup = findViewById(R.id.radioGroupMainActivityDifficultyButtons);
        RadioButton easyRadio = findViewById(R.id.radioButtonMainActivityEasyButton);
        RadioButton mediumRadio = findViewById(R.id.radioButtonMainActivityMediumButton);
        RadioButton hardRadio = findViewById(R.id.radioButtonMainActivityHardButton);
        switch (difficulty) {
            case 24:
                easyRadio.setChecked(false);
                mediumRadio.setChecked(true);
                hardRadio.setChecked(false);
                matchPoint = mediumMatchPoint;
                break;
            case 36:
                easyRadio.setChecked(false);
                mediumRadio.setChecked(false);
                hardRadio.setChecked(true);
                matchPoint = hardMatchPoint;
                break;
            default:
                easyRadio.setChecked(true);
                mediumRadio.setChecked(false);
                hardRadio.setChecked(false);
                matchPoint = easyMatchPoint;
        }

        setUpNewGameButton();
        setUpInstructionsButton();
    }

    public void onRadioButtonClicked(View view) {
        int mediumId = R.id.radioButtonMainActivityMediumButton;
        int hardId = R.id.radioButtonMainActivityHardButton;
        int selectedId = difficultyGroup.getCheckedRadioButtonId();
        difficultyRadioButton = findViewById(selectedId);
        if(selectedId == mediumId) {
            difficulty = 24;
            matchPoint = mediumMatchPoint;
        } else if(selectedId == hardId) {
            difficulty = 36;
            matchPoint = hardMatchPoint;
        } else {
            difficulty = 16;
            matchPoint = easyMatchPoint;
        }
    }

    private void setUpNewGameButton() {
        Button newGameButton = findViewById(R.id.buttonMainActivityNewGame);
        newGameButton.setOnClickListener(view -> startNewGame());
    }

    public void startNewGame() {
        tiles = new ArrayList<>();
        score = 0;
        scoreView.setText(getString(R.string.score_string,score));
        matchesLeft = difficulty / 2;
        discardPile.setImageResource(0);
        discardedTile.setImageResource(0);
        winGameDialogFragment = new WinGameDialogFragment();
        winGameDialogFragment.setContext(context);
        generateTiles();
        setUpTileGridView();
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putInt(DIFFICULTY_TAG,difficulty);
        preferenceEditor.apply();
    }

    private void setUpInstructionsButton() {
        Button instructionsButton = findViewById(R.id.howToPlayButton);
        InstructionsFragment instructionsFragment = new InstructionsFragment();
        instructionsButton.setOnClickListener(view -> instructionsFragment.show(getSupportFragmentManager(),"instructions_fragment"));
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
        int boardHeight;
        ConstraintLayout tileBoard = findViewById(R.id.layoutTileBoardMain);
        ViewGroup.LayoutParams tileBoardParams = tileBoard.getLayoutParams();

        switch (difficulty) {
            case 24:
                boardWidth = 5;
                boardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                tileBoardParams.height = boardHeight;
                tileBoard.setLayoutParams(tileBoardParams);
                break;
            case 36:
                boardWidth = 6;
                boardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 540, getResources().getDisplayMetrics());
                tileBoardParams.height = boardHeight;
                tileBoard.setLayoutParams(tileBoardParams);
                break;
            default:
                boardWidth = 4;
                boardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 360, getResources().getDisplayMetrics());
                tileBoardParams.height = boardHeight;
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

    public void matchFound() {
        updateScore(matchPoint);
        matchesLeft--;
        firstTile.setMatchFound(true);
        secondTile.setMatchFound(true);
        firstTileBack.setImageResource(0);
        secondTileBack.setImageResource(0);
        discardTiles();
        if(matchesLeft == 0) {
            gameOver();
        }
    }

    public void discardTiles() {
        AnimatorSet shrinkAnimatorSet1 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.shrink);
        AnimatorSet shrinkAnimatorSet2 = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.shrink);
        shrinkAnimatorSet1.setTarget(firstTileFront);
        shrinkAnimatorSet2.setTarget(secondTileFront);
        shrinkAnimatorSet1.start();
        shrinkAnimatorSet2.start();
        int discardImg = context.getResources().getIdentifier(firstTile.getTileValue(), "drawable", context.getPackageName());
        AnimatorSet growTile = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.grow);
        growTile.setTarget(discardedTile);
        growTile.start();
        Handler handler = new Handler();
        handler.postDelayed(() -> discardedTile.setImageResource(discardImg),333);
        handler.postDelayed(() -> discardPile.setImageResource(discardImg), 1000);
    }

    public void matchNotFound(Context context) {
        updateScore(penalty);
        flipBack(context, firstTileBack, firstTileFront);
        flipBack(context, secondTileBack, secondTileFront);
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

    private void updateScore(int points) {
        score += points;
        winGameDialogFragment.setScore(score);
        Handler handler = new Handler();
        handler.postDelayed(() -> scoreView.setText(getString(R.string.score_string,score)), 1000);
    }

    public void reset() {
        currentValue = "";
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            secondClicked = false;
            firstTile.setFlipped(false);
            secondTile.setFlipped(false);
        }, 2000);
    }

    private void gameOver() {
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        int highScore;
        switch (difficulty) {
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
        Handler handler = new Handler();
        handler.postDelayed(() -> winGameDialogFragment.show(getSupportFragmentManager(),"win_game"),1500);
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
                    } else matchNotFound(context);
                    reset();
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