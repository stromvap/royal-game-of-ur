package se.stromvap.royal.game.of.ur.model;

import java.io.Serializable;
import java.util.Random;

public class Game implements Serializable {
    private Player player1;
    private Player player2;
    private Board board;
    private Status status;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        board = new Board(player1, player2);

        status = new Status();
        status.setCurrentTurnPlayer(new Random().nextBoolean() ? player1 : player2);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Board getBoard() {
        return board;
    }

    public Status getStatus() {
        return status;
    }
}
