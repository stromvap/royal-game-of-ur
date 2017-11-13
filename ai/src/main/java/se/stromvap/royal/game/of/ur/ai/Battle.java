package se.stromvap.royal.game.of.ur.ai;

import java.util.HashMap;
import java.util.Map;

public class Battle {
    private int numberOfRounds;
    private Ai player1;
    private Ai player2;
    private Map<Ai, Integer> result = new HashMap<>();

    public Battle(int numberOfRounds, Ai player1, Ai player2) {
        this.numberOfRounds = numberOfRounds;
        this.player1 = player1;
        this.player2 = player2;
        result.put(this.player1, 0);
        result.put(this.player2, 0);
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public Map<Ai, Integer> getResult() {
        return result;
    }

    public Ai getPlayer1() {
        return player1;
    }

    public Ai getPlayer2() {
        return player2;
    }

    public void reportWin(Ai winner) {
        result.compute(winner, (ai, i) -> i + 1);
    }
}
