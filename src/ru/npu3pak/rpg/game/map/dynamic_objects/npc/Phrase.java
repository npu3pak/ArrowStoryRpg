package ru.npu3pak.rpg.game.map.dynamic_objects.npc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 26.11.13
 * Time: 15:11
 */
public class Phrase implements Serializable {
    public int id;
    public String text;

    public Phrase(int id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phrase phrase = (Phrase) o;
        return id == phrase.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
