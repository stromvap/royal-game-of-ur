package se.stromvap.royal.game.of.ur.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String id;
    private String name;
    private List<GamePiece> gamePieces = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void givePlayerPieces() {
        gamePieces.clear();
        for (int i = 1; i <= 7; i++) {
            gamePieces.add(new GamePiece(i, this));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GamePiece> getGamePieces() {
        return gamePieces;
    }

    public void setGamePieces(List<GamePiece> gamePieces) {
        this.gamePieces = gamePieces;
    }

    @Override
    public String toString() {
        return name;
    }
}
