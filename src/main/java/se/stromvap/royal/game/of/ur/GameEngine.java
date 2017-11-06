package se.stromvap.royal.game.of.ur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameEngine {
    private static final Logger log = LoggerFactory.getLogger(GameEngine.class);

    private Game game;

    public Game newGame() {
        game = new Game();
        return game;
    }

    public Game getGame() {
        return game;
    }

    public Player getCurrentTurnPlayer() {
        return game.getStatus().getCurrentTurnPlayer();
    }

    public Status getStatus() {
        return game.getStatus();
    }

    public int roll() {
        if (!game.getStatus().canRoll()) {
            throw new IllegalStateException("Last player has not moved, cannot roll right now");
        }

        int roll = 0;
        for (int i = 0; i < 4; i++) {
            roll += new Random().nextBoolean() ? 1 : 0;
        }

        game.getStatus().setLatestRoll(roll);

        log.info("{} rolled a {}", game.getStatus().getCurrentTurnPlayer().getName(), roll);

        if (!canCurrentPlayerMoveAnyGamePiece()) {
            log.info("{} can't move any game pieces", game.getStatus().getCurrentTurnPlayer().getName());
            switchPlayerTurn();
            endTurn();
        }

        return roll;
    }

    public void move(int gamePieceId) {
        Optional<GamePiece> gamePiece = game.getStatus().getCurrentTurnPlayer().getGamePieces().stream().filter(gp -> gp.getId() == gamePieceId).findFirst();
        if (gamePiece.isPresent()) {
            move(gamePiece.get());
        } else {
            throw new IllegalStateException("Game piece " + gamePieceId + " does not exist for player " + game.getStatus().getCurrentTurnPlayer().getName());
        }
    }

    public void move(GamePiece gamePiece) {
        if (game.getStatus().hasMoved()) {
            throw new IllegalStateException("Player has already moved, can't move again");
        }

        if (!canMove(gamePiece)) {
            throw new IllegalStateException("Can't move that game piece");
        }

        int latestRoll = game.getStatus().getLatestRoll();

        // Check if the game piece is on the board and move that one if that's the case
        List<Tile> tiles = game.getBoard().getTiles().get(game.getStatus().getCurrentTurnPlayer());
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);

            if (gamePiece == tile.getGamePiece()) {
                // We found the game piece on the board!

                // Remove the game piece from it's current tile
                tile.setGamePiece(null);

                // Find the new tile
                Tile newTile = tiles.get(i + latestRoll);

                // Move it to the new tile
                moveToTile(gamePiece, newTile);
                return;
            }
        }

        // If we get to here, the game piece we are trying to move is not on the board
        Tile newTile = tiles.get(latestRoll - 1);
        moveToTile(gamePiece, newTile);
    }

    public boolean canMove(GamePiece gamePiece) {
        List<Tile> tiles = game.getBoard().getTiles().get(game.getStatus().getCurrentTurnPlayer());
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);

            if (gamePiece == tile.getGamePiece()) {
                // We found the game piece on the board!

                // Check if the game piece will be moved out of bounds
                if (i + game.getStatus().getLatestRoll() >= tiles.size()) {
                    return false;
                }

                // Get the new tile
                Tile newTile = tiles.get(i + game.getStatus().getLatestRoll());

                // Check if we can move to the new tile
                return canMoveTo(newTile);
            }
        }

        // If we get to here, the game piece we are checking to move is not on the board
        Tile newTile = tiles.get(game.getStatus().getLatestRoll() - 1);
        return canMoveTo(newTile);
    }

    public Player isAnyPlayerAWinner() {
        if (game.getPlayer1().getGamePieces().isEmpty()) {
            log.info("{} won!", game.getPlayer1().getName());
            return game.getPlayer1();
        }

        if (game.getPlayer2().getGamePieces().isEmpty()) {
            log.info("{} won!", game.getPlayer2().getName());
            return game.getPlayer2();
        }

        return null;
    }

    private boolean canCurrentPlayerMoveAnyGamePiece() {
        if (game.getStatus().getLatestRoll() == 0) {
            return false;
        }

        for (GamePiece gamePiece : game.getStatus().getCurrentTurnPlayer().getGamePieces()) {
            if (canMove(gamePiece)) {
                return true;
            }
        }

        return false;
    }

    private boolean canMoveTo(Tile newTile) {
        if (newTile.getGamePiece() != null && newTile.getGamePiece().getPlayer() == game.getStatus().getCurrentTurnPlayer()) {
            // Can't move to a tile you already have a game piece at
            return false;
        }

        if (newTile instanceof FlowerTile && newTile.getGamePiece() != null && newTile.getGamePiece().getPlayer() != game.getStatus().getCurrentTurnPlayer()) {
            // Can't move to a flower tile with an opponents game piece
            return false;
        }

        return true;
    }

    private void moveToTile(GamePiece gamePiece, Tile newTile) {
        // An opponents game piece is at the new tile, yay!
        boolean knockedOut = newTile.getGamePiece() != null;

        // Set my own game piece there instead
        newTile.setGamePiece(gamePiece);

        // Congratulations, you are at the end
        if (newTile instanceof EndTile) {
            // Remove the game piece from the players rooster
            gamePiece.getPlayer().getGamePieces().remove(gamePiece);

            // Send the game piece into exile
            newTile.setGamePiece(null);
        }

        log.info("{} moved {} steps to a {} tile", game.getStatus().getCurrentTurnPlayer().getName(), game.getStatus().getLatestRoll(), newTile.getTileType());
        if (knockedOut) {
            log.info("The other player was knocked out from that tile");
        }

        if (newTile instanceof FlowerTile) {
            log.info("{} landed on a flower, he may roll again", game.getStatus().getCurrentTurnPlayer().getName());
        } else {
            switchPlayerTurn();
        }

        endTurn();
    }

    private void switchPlayerTurn() {
        Player newPlayer = game.getStatus().getCurrentTurnPlayer() == game.getPlayer1() ? game.getPlayer2() : game.getPlayer1();
        game.getStatus().setCurrentTurnPlayer(newPlayer);
    }

    private void endTurn() {
        game.getStatus().setHasMoved(true);
    }
}
