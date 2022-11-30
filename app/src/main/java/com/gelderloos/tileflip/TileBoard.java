package com.gelderloos.tileflip;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.gelderloos.tileflip.adapters.TileRecyclerViewAdapter;

public class TileBoard {
    static String currentValue = "";
    static Tile firstTile = null;
    static Tile secondTile = null;

    //TODO: fix this memory leak
    static ImageView firstTileFront;
    static ImageView firstTileBack;
    static ImageView secondTileFront;
    static ImageView secondTileBack;

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

    public static void matchFound() {
        firstTile.setMatchFound(true);
        secondTile.setMatchFound(true);
        firstTileBack.setImageResource(R.drawable.matchfound);
        secondTileBack.setImageResource(R.drawable.matchfound);
    }

    public static void reset(Context context) {
        flipBack(context, firstTileBack, firstTileFront);
        flipBack(context, secondTileBack, secondTileFront);
        firstTile.setFlipped(false);
        secondTile.setFlipped(false);
        currentValue = "";
    }

}
