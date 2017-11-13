package se.stromvap.royal.game.of.ur.grpc;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameMapper {

    public static Game map(se.stromvap.royal.game.of.ur.model.Game game) {
        Game.Builder builder = Game.newBuilder();
        builder.setPlayer1(map(game.getPlayer1()));
        builder.setPlayer2(map(game.getPlayer2()));
        mapBoard(builder, game.getBoard());

        Player currentTurnPlayer = builder.getPlayer1().getId().equals(game.getStatus().getCurrentTurnPlayer().getId()) ? builder.getPlayer1() : builder.getPlayer2();
        builder.setStatus(Status.newBuilder().setCurrentTurnPlayer(currentTurnPlayer).setLatestRoll(game.getStatus().getLatestRoll()).setHasMoved(game.getStatus().hasMoved()).build());

        return builder.build();
    }

    private static void mapBoard(Game.Builder gameBuilder, se.stromvap.royal.game.of.ur.model.Board board) {
        Board.Builder builder = Board.newBuilder();
        for (Map.Entry<se.stromvap.royal.game.of.ur.model.Player, List<se.stromvap.royal.game.of.ur.model.Tile>> e : board.getTiles().entrySet()) {
            builder.putTiles(e.getKey().getId(), map(gameBuilder, e.getValue()));
        }

        gameBuilder.setBoard(builder.build());
    }

    private static TileList map(Game.Builder gameBuilder, List<se.stromvap.royal.game.of.ur.model.Tile> tiles) {
        TileList.Builder builder = TileList.newBuilder();
        builder.addAllTile(tiles.stream().map(tile -> map(gameBuilder, tile)).collect(Collectors.toList()));
        return builder.build();
    }

    private static Tile map(Game.Builder gameBuilder, se.stromvap.royal.game.of.ur.model.Tile tile) {
        Tile.Builder tileBuilder = Tile.newBuilder().
                setId(tile.getId()).
                setTileType(TileType.valueOf(tile.getTileType().toString()));

        if (tile.getGamePiece() != null) {
            Player tilePlayer = gameBuilder.getPlayer1().getId().equals(tile.getGamePiece().getPlayer().getId()) ? gameBuilder.getPlayer1() : gameBuilder.getPlayer2();
            tileBuilder.setGamePiece(map(tilePlayer, tile.getGamePiece()));
        }

        return tileBuilder.build();
    }

    public static GamePiece map(Player player, se.stromvap.royal.game.of.ur.model.GamePiece gamePiece) {
        return GamePiece.newBuilder().setId(gamePiece.getId()).setPlayer(player).build();
    }

    public static GamePiece map(Player.Builder player, se.stromvap.royal.game.of.ur.model.GamePiece gamePiece) {
        return GamePiece.newBuilder().setId(gamePiece.getId()).setPlayer(player).build();
    }

    public static Player map(se.stromvap.royal.game.of.ur.model.Player player) {
        Player.Builder builder = Player.newBuilder().setId(player.getId()).setName(player.getName());
        builder.addAllGamePiece(player.getGamePieces().stream().map(gp -> map(builder, gp)).collect(Collectors.toList()));
        return builder.build();
    }
}
