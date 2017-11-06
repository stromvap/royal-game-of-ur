package se.stromvap.royal.game.of.ur;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GamePiece {
    private int id;
    private Player player;

    public GamePiece(int id, Player player) {
        this.id = id;
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return player.getName();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
