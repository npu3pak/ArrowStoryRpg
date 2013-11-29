package ru.npu3pak.rpg.game.map;

import android.content.Context;
import ru.npu3pak.rpg.game.Direction;
import ru.npu3pak.rpg.game.map.dynamic_objects.Merchant;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;
import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.map.dynamic_objects.Monster;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Npc;
import ru.npu3pak.rpg.game.map.static_objects.StaticObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 20.11.13
 * Time: 8:47
 */
public class Map {
    public Position startPlayerPosition;
    public Direction startPlayerDirection;

    public Player player;
    public ArrayList<StaticObject> staticObjects = new ArrayList<StaticObject>();
    public ArrayList<Monster> monsters = new ArrayList<Monster>();
    public ArrayList<Merchant> merchants = new ArrayList<Merchant>();
    public ArrayList<Npc> npcs = new ArrayList<Npc>();

    public static Map load(Context context, String mapName) {
        MapUtil util = new MapUtil(context);
        return util.loadMap(mapName);
    }

    public boolean canMove(Position position) {
        for (StaticObject object : staticObjects) {
            if (object.position.equals(position))
                return !object.isBarrier();
        }
        return true;
    }

    public Merchant checkMerchant(Position position) {
        for (Merchant merchant : merchants) {
            if (merchant.position.equals(position))
                return merchant;
        }
        return null;
    }

    public Monster checkMonster(Position position) {
        for (Monster monster : monsters) {
            if (monster.position.equals(position))
                return monster;
        }
        return null;
    }

    public Npc checkNpc(Position position) {
        for (Npc npc : npcs) {
            if (npc.position.equals(position))
                return npc;
        }
        return null;
    }

    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    public Position getStartPlayerPosition() {
        return startPlayerPosition;
    }

    public Direction getStartPlayerDirection() {
        return startPlayerDirection;
    }
}