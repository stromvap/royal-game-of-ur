package se.stromvap.royal.game.of.ur;

public class Status {
    private Player currentTurnPlayer;
    private int latestRoll;
    private boolean hasMoved = true;

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public void setCurrentTurnPlayer(Player currentTurnPlayer) {
        this.currentTurnPlayer = currentTurnPlayer;
    }

    public int getLatestRoll() {
        return latestRoll;
    }

    public void setLatestRoll(int latestRoll) {
        this.latestRoll = latestRoll;
        setHasMoved(false);
    }

    public boolean canRoll() {
        return hasMoved;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
