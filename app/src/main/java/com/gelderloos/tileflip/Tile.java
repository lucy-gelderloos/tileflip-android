package com.gelderloos.tileflip;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

public class Tile {
    private int  tileId;
    private String tileValue;
    private TileBoard gameBoard;
    private boolean flipped;
    private boolean matchFound;

    Tile(int tileId, String tileValue, TileBoard gameBoard) {
        this.tileId = tileId;
        this.tileValue = tileValue;
        this.gameBoard = gameBoard;
        this.flipped = false;
        this.matchFound = false;
    }

    public void checkForMatch(Context context, ImageView backView, ImageView frontView) {
        if(!this.isFlipped() && !this.isMatchFound() && !gameBoard.secondClicked) {
            this.setFlipped(true);
            gameBoard.flipTile(context,backView,frontView);
            if(gameBoard.currentValue.equals("")) {
                gameBoard.currentValue = this.tileValue;
                gameBoard.firstTile = this;
                gameBoard.firstTileBack = backView;
                gameBoard.firstTileFront = frontView;
            } else {
                gameBoard.secondTile = this;
                gameBoard.secondTileBack = backView;
                gameBoard.secondTileFront = frontView;
                gameBoard.secondClicked = true;

                if(gameBoard.currentValue.equals(this.getTileValue())) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gameBoard.matchFound();
                        }
                    }, 500);
                } else gameBoard.matchNotFound();
                gameBoard.reset(context);
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
