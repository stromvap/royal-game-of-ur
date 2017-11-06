package se.stromvap.royal.game.of.ur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Player, List<Tile>> tiles;

    public Board(Player player1, Player player2) {
        tiles = new HashMap<>();

        List<Tile> player1Tiles = new ArrayList<>();
        List<Tile> player2Tiles = new ArrayList<>();

        player1Tiles.add(new SafeTile());
        player1Tiles.add(new SafeTile());
        player1Tiles.add(new SafeTile());
        player1Tiles.add(new FlowerTile());

        player2Tiles.add(new SafeTile());
        player2Tiles.add(new SafeTile());
        player2Tiles.add(new SafeTile());
        player2Tiles.add(new FlowerTile());

        Tile tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new FlowerTile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        tile = new Tile();
        player1Tiles.add(tile);
        player2Tiles.add(tile);

        player1Tiles.add(new SafeTile());
        player1Tiles.add(new FlowerTile());
        player1Tiles.add(new EndTile());

        player2Tiles.add(new SafeTile());
        player2Tiles.add(new FlowerTile());
        player2Tiles.add(new EndTile());

        tiles.put(player1, player1Tiles);
        tiles.put(player2, player2Tiles);
    }

    public Map<Player, List<Tile>> getTiles() {
        return tiles;
    }

    public void setTiles(Map<Player, List<Tile>> tiles) {
        this.tiles = tiles;
    }
}
