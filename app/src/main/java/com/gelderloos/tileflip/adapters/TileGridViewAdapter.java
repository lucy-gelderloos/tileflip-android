package com.gelderloos.tileflip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gelderloos.tileflip.R;
import com.gelderloos.tileflip.Tile;

import java.util.List;

public class TileGridViewAdapter extends ArrayAdapter<Tile> {
    List<Tile> tiles;
    Context context;

    public TileGridViewAdapter(@NonNull Context context, List<Tile> tiles ) {
        super(context,0,tiles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tile, parent, false);
        }

        Tile thisTile = getItem(position);
        String tileValue = thisTile.getTileValue();
        Context context = this.context;
        int imgId = context.getResources().getIdentifier(tileValue,"drawable", context.getPackageName());
        if(imgId == 0) {
            imgId = context.getResources().getIdentifier("matchFound","drawable", context.getPackageName());
        }
        ImageView tileBackImageView = listItemView.findViewById(R.id.imgViewTileBack);
        tileBackImageView.setImageResource(R.drawable.tileback);
        ImageView tileFrontImageView = listItemView.findViewById(R.id.imgViewTileFront);
        tileFrontImageView.setImageResource(imgId);
        tileBackImageView.setOnClickListener(view -> {
            thisTile.checkForMatch(context, tileBackImageView, tileFrontImageView);
        });
        return listItemView;
    }

}
