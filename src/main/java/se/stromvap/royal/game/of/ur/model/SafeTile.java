package se.stromvap.royal.game.of.ur.model;

import java.io.Serializable;

public class SafeTile extends Tile implements Safe, Serializable {
    public SafeTile() {
        super(TileType.SAFE);
    }
}
