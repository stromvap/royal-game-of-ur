package se.stromvap.royal.game.of.ur;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;

    private List<GamePiece> gamePieces = new ArrayList<>();

    public Player() {
    }

    public Player(String name) {
        this.name = name;

        for (int i = 1; i <= 7; i++) {
            gamePieces.add(new GamePiece(i, this));
        }
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
