package ru.npu3pak.rpg.game;

import ru.npu3pak.rpg.game.equipment.Item;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 27.11.13
 * Time: 10:50
 */
public class CharacterEquipment implements Serializable {
    public Item weapon;
    public Item armor;
}
