package ru.npu3pak.rpg.game.map;

import ru.npu3pak.rpg.game.Position;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 22.11.13
 * Time: 14:52
 */
public abstract class MapObject implements Serializable {
    public Position position;

    public MapObject(Position position) {
        this.position = position;
    }

    //Возвращает символ, который отрисовывается на карте
    public abstract char getViewChar();
}
