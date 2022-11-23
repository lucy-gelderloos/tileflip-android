package com.gelderloos.tileflip;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class TileBoard {
    static String currentValue = "";
    static Tile firstTile = null;
    static Tile secondTile = null;

    public static void flipTile(Context context, View visibleView, View invisibleView) {
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

    public static void flipBack(Context context, View visibleView, View invisibleView) {
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

    public static void checkForMatch(Tile tile, Context context, ImageView backView, ImageView frontView) {
        if(!tile.isFlipped() && !tile.isMatchFound()) {
            tile.setFlipped(true);
            flipTile(context,backView,frontView);

            ImageView firstTileFront = null;
            ImageView firstTileBack = null;
            ImageView secondTileFront;
            ImageView secondTileBack;
            if(currentValue.equals("")) {
                currentValue = tile.getTileValue();
                firstTile = tile;
                firstTileBack = backView;
                firstTileFront = frontView;
            } else {
                secondTile = tile;
                secondTileBack = backView;
                secondTileFront = frontView;

                if(currentValue.equals(tile.getTileValue())) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            matchFound(firstTileBack,secondTileBack);
                        }
                    }, 500);
                }
//                reset(context);
            }
        }
    }

    public static void matchFound(ImageView firstTileBack, ImageView secondTileBack) {
        firstTile.setMatchFound(true);
        secondTile.setMatchFound(true);
        firstTileBack.setImageResource(R.drawable.matchfound);
        secondTileBack.setImageResource(R.drawable.matchfound);
    }

    public static void reset(Context context, ImageView firstTileFront, ImageView secondTileFront, ImageView firstTileBack, ImageView secondTileBack) {
        flipBack(context,firstTileBack,firstTileFront);
        flipBack(context,secondTileBack,secondTileFront);
        firstTile.setFlipped(false);
        secondTile.setFlipped(false);
        currentValue = "";
    }
}
