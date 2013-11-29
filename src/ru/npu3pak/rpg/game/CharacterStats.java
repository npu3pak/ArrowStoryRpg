package ru.npu3pak.rpg.game;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CharacterStats implements Serializable {
    public String name;
    public int hp;
    public int mana;
    public int maxHp;
    public int maxMana;
    public int minDamage;
    public int maxDamage;
    public int level;
    public int exp;
    public int armor;

    public CharacterStats() {
    }

    public static CharacterStats fromJson(JSONObject json) throws JSONException {
        CharacterStats stats = new CharacterStats();
        if (json.has("name"))
            stats.name = json.getString("name");
        if (json.has("hp"))
            stats.hp = json.getInt("hp");
        if (json.has("mp"))
            stats.mana = json.getInt("mp");
        if (json.has("maxMp"))
            stats.maxMana = json.getInt("maxMp");
        if (json.has("maxHp"))
            stats.maxHp = json.getInt("maxHp");
        if (json.has("minDamage"))
            stats.minDamage = json.getInt("minDamage");
        if (json.has("maxDamage"))
            stats.maxDamage = json.getInt("maxDamage");
        if (json.has("armor"))
            stats.armor = json.getInt("armor");
        if (json.has("level"))
            stats.level = json.getInt("level");
        if (json.has("exp"))
            stats.exp = json.getInt("exp");
        return stats;
    }

    public void applyModifiers(CharacterStats modifier) {
        hp = hp + modifier.hp;
        hp = Math.min(hp, maxHp);
        mana = mana + modifier.mana;
        mana = Math.min(mana, maxMana);
    }
}