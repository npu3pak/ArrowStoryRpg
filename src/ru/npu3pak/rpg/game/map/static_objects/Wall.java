package ru.npu3pak.rpg.game.map.static_objects;

import ru.npu3pak.rpg.game.Position;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 22.11.13
 * Time: 14:16
 */
public class Wall extends StaticObject {
    private int type;

    public Wall(int x, int y, int type) {
        super(new Position(x, y));
        this.type = type;
    }

    @Override
    public char getViewChar() {
        return type == 1 ? '#' : '=';
    }

    @Override
    public boolean isBarrier() {
        return true;
    }
}