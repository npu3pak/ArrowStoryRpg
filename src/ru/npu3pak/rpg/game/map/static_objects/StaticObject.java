package ru.npu3pak.rpg.game.map.static_objects;

import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.map.MapObject;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 22.11.13
 * Time: 15:03
 */
public abstract class StaticObject extends MapObject {
    public StaticObject(Position position) {
        super(position);
    }

    public abstract boolean isBarrier();
}
