package se.stromvap.royal.game.of.ur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {
    private static final Logger log = LoggerFactory.getLogger(Game.class);

    private Player player1;
    private Player player2;
    private Board board;
    private Status status;

    public Game() {
        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        board = new Board(player1, player2);

        status = new Status();
        status.setCurrentTurnPlayer(player1);
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
