package se.stromvap.royal.game.of.ur.ai;

import se.stromvap.royal.game.of.ur.ai.ais.RandomAi;
import se.stromvap.royal.game.of.ur.ai.ais.SimpleAi;

public class AiArenaMain {

    public static void main(String[] args) {
        Battle battle = new Battle(1000, new SimpleAi(), new RandomAi());
        AiArena.battle(battle);
    }
}
