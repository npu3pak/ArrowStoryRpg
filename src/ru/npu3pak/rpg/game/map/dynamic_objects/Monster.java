package ru.npu3pak.rpg.game.map.dynamic_objects;

import ru.npu3pak.rpg.game.CharacterStats;
import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.map.MapObject;


/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 22.11.13
 * Time: 14:16
 */
public class Monster extends MapObject {
    public CharacterStats stats = new CharacterStats();
    public int gold;

    public Monster(int x, int y) {
        super(new Position(x, y));
    }

    @Override
    public char getViewChar() {
        return 'M';
    }
}