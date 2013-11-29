package ru.npu3pak.rpg.game.map.static_objects;

import ru.npu3pak.rpg.game.Position;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 27.11.13
 * Time: 9:17
 */
public class Letter extends StaticObject {
    private char character;

    public Letter(int x, int y, char character) {
        super(new Position(x, y));
        this.character = character;
    }

    @Override
    public boolean isBarrier() {
        return false;
    }

    @Override
    public char getViewChar() {
        return character;
    }
}
