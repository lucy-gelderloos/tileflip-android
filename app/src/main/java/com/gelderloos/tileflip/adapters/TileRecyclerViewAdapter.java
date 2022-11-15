package com.gelderloos.tileflip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelderloos.tileflip.MainActivity;
import com.gelderloos.tileflip.R;

import java.util.List;

public class TileRecyclerViewAdapter extends RecyclerView.Adapter<TileRecyclerViewAdapter.TileViewHolder> {
    List<MainActivity.Tile> tiles;
    Context callingActivity;

    public TileRecyclerViewAdapter(List<MainActivity.Tile> tiles, Context callingActivity) {
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

    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    public static class TileViewHolder extends RecyclerView.ViewHolder {
        public TileViewHolder(@NonNull View itemView) {super(itemView);}
    }
}
