package ru.npu3pak.rpg.game.map.dynamic_objects.npc;

import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.map.MapObject;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 26.11.13
 * Time: 15:08
 */
public class Npc extends MapObject {
    public int id;
    public String name;
    public Phrase[] npcPhrases;
    public Phrase[] playerPhrases;
    public Conversation[] playerAnswers;
    public Conversation[] npcAnswers;

    public Npc() {
        super(null);
    }

    public void setPosition(int x, int y) {
        position = new Position(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Npc npc = (Npc) o;
        return id == npc.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public char getViewChar() {
        return '8';
    }
}
