package com.gelderloos.tileflip.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.Resources;
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
        System.out.println(tiles.get(position).getTileValue());
        int imgId = context.getResources().getIdentifier(tileValue,"drawable", context.getPackageName());
        if(imgId == 0) {
            imgId = context.getResources().getIdentifier("matchFound","drawable", context.getPackageName());
        }
        ImageView tileBackImageView = holder.itemView.findViewById(R.id.imgViewTileBack);
        tileBackImageView.setImageResource(R.drawable.tileback);
        ImageView tileFrontImageView = holder.itemView.findViewById(R.id.imgViewTileFront);
        tileFrontImageView.setImageResource(imgId);
        tileBackImageView.setOnClickListener(view -> {
            flipTile(this.callingActivity,tileBackImageView,tileFrontImageView);
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

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    public static class TileViewHolder extends RecyclerView.ViewHolder {
        public TileViewHolder(@NonNull View itemView) {super(itemView);}
    }
}
