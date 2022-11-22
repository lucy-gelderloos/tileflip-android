package com.gelderloos.tileflip;public class Tile {
    private String  tileId;
    private String tileValue;

    Tile(String tileId, String tileValue) {
        this.tileId = tileId;
        this.tileValue = tileValue;
    }

    public String getTileId() {
        return tileId;
    }

    public void setTileId(String tileId) {
        this.tileId = tileId;
    }

    public String getTileValue() {
        return tileValue;
    }

    public void setTileValue(String tileValue) {
        this.tileValue = tileValue;
    }
}
