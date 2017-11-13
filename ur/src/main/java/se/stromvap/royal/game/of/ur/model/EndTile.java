package se.stromvap.royal.game.of.ur.model;

import java.io.Serializable;

public class EndTile extends Tile implements Safe, Serializable {
    public EndTile() {
        super(TileType.END);
    }
}
