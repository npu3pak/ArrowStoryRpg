package ru.npu3pak.rpg.game.map.dynamic_objects;

import ru.npu3pak.rpg.game.*;
import ru.npu3pak.rpg.game.equipment.Item;
import ru.npu3pak.rpg.game.map.MapObject;
import ru.npu3pak.rpg.game.map.MapUtil;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 20.11.13
 * Time: 8:34
 */
public class Player extends MapObject {

    public ArrayList<Skill.AttackSkills> skills = new ArrayList<Skill.AttackSkills>();
    public ArrayList<Item> items = new ArrayList<Item>();
    public CharacterStats stats = new CharacterStats();
    public CharacterEquipment equipment = new CharacterEquipment();
    public int gold;

    public Direction direction;

    public Player(Position position, Direction direction) {
        super(position);
        this.direction = direction;

        stats.name = "Доблестный воин";
        stats.level = 1;
        stats.exp = 0;
        stats.hp = 200;
        stats.mana = 200;
        stats.maxHp = 200;
        stats.maxMana = 200;
        stats.minDamage = 0;
        stats.maxDamage = 20;

        learnSkill(1);
        learnSkill(1);
        learnSkill(1);

        gold = 1000;
    }

    public int getArmor() {
        int modifier = equipment.armor == null ? 0 : equipment.armor.modifiers.armor;
        return stats.armor + modifier;
    }

    public int getMinDamage() {
        int modifier = equipment.weapon == null ? 0 : equipment.weapon.modifiers.minDamage;
        return stats.minDamage + modifier;
    }

    public int getMaxDamage() {
        int modifier = equipment.weapon == null ? 0 : equipment.weapon.modifiers.maxDamage;
        return stats.maxDamage + modifier;
    }

    @Override
    public char getViewChar() {
        switch (direction) {
            case UP:
                return '^';
            case LEFT:
                return '<';
            case RIGHT:
                return '>';
            default:
                return 'v';
        }
    }

    public void useItem(Item item) {
        stats.applyModifiers(item.modifiers);
    }

    public void learnSkill(int level) {
        Skill.AttackSkills skill = MapUtil.createBattleSkill(level);
        boolean skillAdded = false;
        while (!skillAdded) {
            if (!skills.contains(skill)) {
                skills.add(skill);
                skillAdded = true;
            }
        }
    }

    public boolean checkedLevelUp() {
        if (stats.exp > getNextLevelExp()) {
            stats.level++;
            stats.maxMana = stats.maxMana + 100;
            stats.hp = stats.maxHp;
            stats.maxHp = stats.maxHp + 100;
            stats.mana = stats.maxMana;
            learnSkill(stats.level);
            return true;
        }
        return false;
    }

    public int getNextLevelExp() {
        return (stats.level + 1) * 100;
    }
}