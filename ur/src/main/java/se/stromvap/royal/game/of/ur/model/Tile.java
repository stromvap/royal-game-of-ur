package se.stromvap.royal.game.of.ur.model;

import java.io.Serializable;

public class Tile implements Serializable {
    private int id;
    private TileType tileType;
    private GamePiece gamePiece;

    public Tile() {
        this(TileType.NORMAL);
    }

    public Tile(TileType tileType) {
        this.tileType = tileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public GamePiece getGamePiece() {
        return gamePiece;
    }

    public void setGamePiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }
}
