package se.stromvap.royal.game.of.ur.rest.model;

import se.stromvap.royal.game.of.ur.model.GamePiece;

import java.util.List;

public class RollResponse {
    private int roll;
    private String player;
    private List<GamePiece> movableGamePieces;

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<GamePiece> getMovableGamePieces() {
        return movableGamePieces;
    }

    public void setMovableGamePieces(List<GamePiece> movableGamePieces) {
        this.movableGamePieces = movableGamePieces;
    }
}
