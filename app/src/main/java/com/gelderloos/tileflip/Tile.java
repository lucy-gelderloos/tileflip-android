package com.gelderloos.tileflip;

import static com.gelderloos.tileflip.TileBoard.matchFound;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class Tile {
    private int  tileId;
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
        if(!this.isFlipped() && !this.isMatchFound()) {
            this.setFlipped(true);
            TileBoard.flipTile(context,backView,frontView);
            if(TileBoard.currentValue.equals("")) {
                TileBoard.currentValue = this.tileValue;
                TileBoard.firstTile = this;
                TileBoard.firstTileBack = backView;
                TileBoard.firstTileFront = frontView;
            } else {
                TileBoard.secondTile = this;
                TileBoard.secondTileBack = backView;
                TileBoard.secondTileFront = frontView;

                if(TileBoard.currentValue.equals(this.getTileValue())) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            matchFound();
                        }
                    }, 500);
                }
                TileBoard.reset(context);
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
