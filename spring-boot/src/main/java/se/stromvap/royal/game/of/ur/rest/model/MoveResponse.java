package se.stromvap.royal.game.of.ur.rest.model;

public class MoveResponse {
    private String player;

    public MoveResponse(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
