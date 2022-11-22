package com.gelderloos.tileflip.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelderloos.tileflip.MainActivity;
import com.gelderloos.tileflip.R;
import com.gelderloos.tileflip.Tile;

import java.util.List;

public class TileRecyclerViewAdapter extends RecyclerView.Adapter<TileRecyclerViewAdapter.TileViewHolder> {
    List<Tile> tiles;
    Context callingActivity;
    String currentValue = "";
    Tile firstTile = null;
    Tile secondTile = null;
    ImageView firstTileFront;
    ImageView firstTileBack;
    ImageView secondTileFront;
    ImageView secondTileBack;

    public TileRecyclerViewAdapter(List<Tile> tiles, Context callingActivity) {
        this.tiles = tiles;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tileFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tile, parent, false);
        return new TileViewHolder(tileFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        String tileValue = tiles.get(position).getTileValue();
        Context context = this.callingActivity;
        int imgId = context.getResources().getIdentifier(tileValue,"drawable", context.getPackageName());
        if(imgId == 0) {
            imgId = context.getResources().getIdentifier("matchFound","drawable", context.getPackageName());
        }
        ImageView tileBackImageView = holder.itemView.findViewById(R.id.imgViewTileBack);
        tileBackImageView.setImageResource(R.drawable.tileback);
        ImageView tileFrontImageView = holder.itemView.findViewById(R.id.imgViewTileFront);
        tileFrontImageView.setImageResource(imgId);
        tileBackImageView.setOnClickListener(view -> {
            checkForMatch(tiles.get(position), context, tileBackImageView, tileFrontImageView);
        });
    }

    private void flipTile(Context context, View visibleView, View invisibleView) {
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

    private void flipBack(Context context, View visibleView, View invisibleView) {
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

    private void checkForMatch(Tile tile, Context context, ImageView backView, ImageView frontView) {
        System.out.println(tile.getTileValue());
        if(!tile.isFlipped() && !tile.isMatchFound()) {
            tile.setFlipped(true);
            flipTile(context,backView,frontView);
            System.out.println(currentValue);
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
                            matchFound();
                        }
                    }, 500);
                }
                reset(context);
            }
        }
    }

    private void matchFound() {
        firstTile.setMatchFound(true);
        secondTile.setMatchFound(true);
        firstTileBack.setImageResource(R.drawable.matchfound);
        secondTileBack.setImageResource(R.drawable.matchfound);
    }

    private void reset(Context context) {
        flipBack(context,firstTileBack,firstTileFront);
        flipBack(context,secondTileBack,secondTileFront);
        firstTile.setFlipped(false);
        secondTile.setFlipped(false);
        currentValue = "";
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    public static class TileViewHolder extends RecyclerView.ViewHolder {
        public TileViewHolder(@NonNull View itemView) {super(itemView);}
    }
}
