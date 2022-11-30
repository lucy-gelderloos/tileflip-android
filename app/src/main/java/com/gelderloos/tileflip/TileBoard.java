package com.gelderloos.tileflip;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gelderloos.tileflip.adapters.TileRecyclerViewAdapter;

public class TileBoard {
    String currentValue = "";
    Tile firstTile = null;
    Tile secondTile = null;
    boolean secondClicked = false;

    ImageView firstTileFront;
    ImageView firstTileBack;
    ImageView secondTileFront;
    ImageView secondTileBack;
    TextView scoreView;

    int matchPoint;
    int score;
    int penalty = -10;

    public TileBoard() {

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
    }

    public void matchNotFound() {
        updateScore(penalty);
    }

    public void reset(Context context) {
        flipBack(context, firstTileBack, firstTileFront);
        flipBack(context, secondTileBack, secondTileFront);
        firstTile.setFlipped(false);
        secondTile.setFlipped(false);
        currentValue = "";
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                secondClicked = false;
            }
        }, 1500);
    }

    private void updateScore(int points) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                score += points;
                scoreView.setText("" + score);
            }
        }, 1000);

    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public Tile getFirstTile() {
        return firstTile;
    }

    public void setFirstTile(Tile firstTile) {
        this.firstTile = firstTile;
    }

    public Tile getSecondTile() {
        return secondTile;
    }

    public void setSecondTile(Tile secondTile) {
        this.secondTile = secondTile;
    }

    public ImageView getFirstTileFront() {
        return firstTileFront;
    }

    public void setFirstTileFront(ImageView firstTileFront) {
        this.firstTileFront = firstTileFront;
    }

    public ImageView getFirstTileBack() {
        return firstTileBack;
    }

    public void setFirstTileBack(ImageView firstTileBack) {
        this.firstTileBack = firstTileBack;
    }

    public ImageView getSecondTileFront() {
        return secondTileFront;
    }

    public void setSecondTileFront(ImageView secondTileFront) {
        this.secondTileFront = secondTileFront;
    }

    public ImageView getSecondTileBack() {
        return secondTileBack;
    }

    public void setSecondTileBack(ImageView secondTileBack) {
        this.secondTileBack = secondTileBack;
    }

    public TextView getScoreView() {
        return scoreView;
    }

    public void setScoreView(TextView scoreView) {
        this.scoreView = scoreView;
    }

    public int getMatchPoint() {
        return matchPoint;
    }

    public void setMatchPoint(int matchPoint) {
        this.matchPoint = matchPoint;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}
