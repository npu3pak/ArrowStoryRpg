package ru.npu3pak.rpg.game.equipment;

import ru.npu3pak.rpg.game.CharacterStats;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 21.11.13
 * Time: 11:19
 */
public class Item implements Serializable {
    public int level;
    public String name;
    public ItemType type;
    public CharacterStats modifiers = new CharacterStats();
    public int price;

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        if (modifiers.hp > 0)
            builder.append(modifiers.hp).append("HP\n");
        if (modifiers.mana > 0)
            builder.append(modifiers.mana).append("MP\n");
        if (modifiers.armor > 0)
            builder.append("Защита ").append(modifiers.armor).append("\n");
        if (modifiers.minDamage > 0 || modifiers.maxDamage > 0)
            builder.append("Урон ").append(modifiers.minDamage).append('-').append(modifiers.maxDamage).append('\n');
        return builder.toString().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (level != item.level) return false;
        if (price != item.price) return false;
        if (modifiers != null ? !modifiers.equals(item.modifiers) : item.modifiers != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (type != item.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = level;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (modifiers != null ? modifiers.hashCode() : 0);
        result = 31 * result + price;
        return result;
    }

    public enum ItemType {
        POTION, WEAPON, ARMOR
    }
}