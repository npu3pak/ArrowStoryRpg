package ru.npu3pak.rpg.game;

import java.io.Serializable;

public class Skill implements Serializable {
    public static class AttackSkills implements Serializable {
        public String name;
        public int minDamage;
        public int maxDamage;
        public int manaCost;

        public AttackSkills(String name, int minDamage, int maxDamage, int manaCost) {
            this.name = name;
            this.minDamage = minDamage;
            this.maxDamage = maxDamage;
            this.manaCost = manaCost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttackSkills that = (AttackSkills) o;
            return !(name != null ? !name.equals(that.name) : that.name != null);
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}