package ru.npu3pak.rpg.game.map.dynamic_objects.npc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 26.11.13
 * Time: 15:15
 */
public class Conversation implements Serializable{
    public Phrase phrase;
    public Phrase[] answers;

    public Conversation(Phrase phrase, Phrase[] answers) {
        this.phrase = phrase;
        this.answers = answers;
    }
}
