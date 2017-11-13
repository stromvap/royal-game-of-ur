package se.stromvap.royal.game.of.ur.grpc;

import se.stromvap.royal.game.of.ur.model.*;
import se.stromvap.royal.game.of.ur.model.Game;
import se.stromvap.royal.game.of.ur.model.GamePiece;
import se.stromvap.royal.game.of.ur.model.Player;

public interface RemoteAi {
    String getName();

    GamePiece yourTurn(Game game);

    void gameOver(Player winner);
}
