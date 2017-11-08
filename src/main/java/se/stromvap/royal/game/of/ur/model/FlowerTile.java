package se.stromvap.royal.game.of.ur.model;

import java.io.Serializable;

public class FlowerTile extends Tile implements Safe, Flower, Serializable {
    public FlowerTile() {
        super(TileType.FLOWER);
    }
}
