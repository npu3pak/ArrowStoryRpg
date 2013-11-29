package ru.npu3pak.rpg.game.map.static_objects;

import ru.npu3pak.rpg.game.Position;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 22.11.13
 * Time: 15:17
 */
public class Tree extends StaticObject {
    private int type;

    public Tree(int x, int y, int type) {
        super(new Position(x, y));
        this.type = type;
    }

    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public char getViewChar() {
        return 'T';
    }
}
