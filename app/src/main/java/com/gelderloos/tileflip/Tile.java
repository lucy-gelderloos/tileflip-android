package com.gelderloos.tileflip;public class Tile {
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
