package ru.npu3pak.rpg.game;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 20.11.13
 * Time: 8:35
 */
public class Position implements Serializable {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position getNextBackwardPosition(Direction direction) {
        switch (direction) {
            case UP:
                return new Position(x, y + 1);
            case DOWN:
                return new Position(x, y - 1);
            case LEFT:
                return new Position(x + 1, y);
            case RIGHT:
                return new Position(x - 1, y);
        }
        return this;
    }

    public Position getNextForwardPosition(Direction direction) {
        switch (direction) {
            case UP:
                return new Position(x, y - 1);
            case DOWN:
                return new Position(x, y + 1);
            case LEFT:
                return new Position(x - 1, y);
            case RIGHT:
                return new Position(x + 1, y);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
