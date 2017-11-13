package se.stromvap.royal.game.of.ur.grpc;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrpcGameMapper {

    public static se.stromvap.royal.game.of.ur.model.Game map(Game game) {
        se.stromvap.royal.game.of.ur.model.Game gameModel = new se.stromvap.royal.game.of.ur.model.Game();
        gameModel.setPlayer1(map(game.getPlayer1()));
        gameModel.setPlayer2(map(game.getPlayer2()));
        mapBoard(gameModel, game.getBoard());

        se.stromvap.royal.game.of.ur.model.Player currentTurnPlayer = gameModel.getPlayer1().getId().equals(game.getStatus().getCurrentTurnPlayer().getId()) ? gameModel.getPlayer1() : gameModel.getPlayer2();
        se.stromvap.royal.game.of.ur.model.Status status = new se.stromvap.royal.game.of.ur.model.Status();
        status.setCurrentTurnPlayer(currentTurnPlayer);
        status.setLatestRoll(game.getStatus().getLatestRoll());
        status.setHasMoved(game.getStatus().getHasMoved());
        gameModel.setStatus(status);

        return gameModel;
    }

    private static void mapBoard(se.stromvap.royal.game.of.ur.model.Game gameModel, Board board) {
        se.stromvap.royal.game.of.ur.model.Board boardModel = new se.stromvap.royal.game.of.ur.model.Board(gameModel.getPlayer1(), gameModel.getPlayer2());
        boardModel.getTiles().clear();
        for (Map.Entry<String, TileList> e : board.getTilesMap().entrySet()) {
            se.stromvap.royal.game.of.ur.model.Player player = gameModel.getPlayer1().getId().equals(e.getKey()) ? gameModel.getPlayer1() : gameModel.getPlayer2();
            boardModel.getTiles().put(player, new ArrayList<>(map(gameModel, e.getValue().getTileList())));
        }

        gameModel.setBoard(boardModel);
    }

    private static List<se.stromvap.royal.game.of.ur.model.Tile> map(se.stromvap.royal.game.of.ur.model.Game gameModel, List<Tile> tiles) {
        return tiles.stream().map(tile -> map(gameModel, tile)).collect(Collectors.toList());
    }

    private static se.stromvap.royal.game.of.ur.model.Tile map(se.stromvap.royal.game.of.ur.model.Game gameModel, Tile tile) {
        se.stromvap.royal.game.of.ur.model.Tile tileModel;
        if (tile.getTileType() == TileType.NORMAL) {
            tileModel = new se.stromvap.royal.game.of.ur.model.Tile();
        } else if (tile.getTileType() == TileType.SAFE) {
            tileModel = new se.stromvap.royal.game.of.ur.model.SafeTile();
        } else if (tile.getTileType() == TileType.FLOWER) {
            tileModel = new se.stromvap.royal.game.of.ur.model.FlowerTile();
        } else if (tile.getTileType() == TileType.END) {
            tileModel = new se.stromvap.royal.game.of.ur.model.EndTile();
        } else {
            throw new IllegalStateException("Faield to map enum");
        }

        tileModel.setId(tile.getId());

        if (tile.hasGamePiece()) {
            se.stromvap.royal.game.of.ur.model.Player tilePlayer = gameModel.getPlayer1().getId().equals(tile.getGamePiece().getPlayer().getId()) ? gameModel.getPlayer1() : gameModel.getPlayer2();
            tileModel.setGamePiece(tilePlayer.getGamePieces().stream().filter(gp -> gp.getId() == tile.getGamePiece().getId()).findFirst().get());
        }

        return tileModel;
    }

    private static se.stromvap.royal.game.of.ur.model.GamePiece map(se.stromvap.royal.game.of.ur.model.Player playerModel, GamePiece gamePiece) {
        return new se.stromvap.royal.game.of.ur.model.GamePiece(gamePiece.getId(), playerModel);
    }

    public static se.stromvap.royal.game.of.ur.model.Player map(Player player) {
        se.stromvap.royal.game.of.ur.model.Player mappedPlayer = new se.stromvap.royal.game.of.ur.model.Player(player.getName());
        mappedPlayer.setId(player.getId());
        mappedPlayer.getGamePieces().addAll(player.getGamePieceList().stream().map(gp -> map(mappedPlayer, gp)).collect(Collectors.toList()));
        return mappedPlayer;
    }

}
