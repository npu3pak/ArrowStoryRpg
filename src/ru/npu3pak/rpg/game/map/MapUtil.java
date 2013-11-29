package ru.npu3pak.rpg.game.map;

import android.content.Context;
import ru.npu3pak.rpg.game.Direction;
import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.Skill;
import ru.npu3pak.rpg.game.equipment.Item;
import ru.npu3pak.rpg.game.map.dynamic_objects.Merchant;
import ru.npu3pak.rpg.game.map.dynamic_objects.Monster;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Npc;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.NpcUtil;
import ru.npu3pak.rpg.game.map.static_objects.Letter;
import ru.npu3pak.rpg.game.map.static_objects.Tree;
import ru.npu3pak.rpg.game.map.static_objects.Wall;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 20.11.13
 * Time: 9:55
 */
public class MapUtil {
    Context context;

    public MapUtil(Context context) {
        this.context = context;
    }

    public Map loadMap(String mapName) {
        NpcUtil npcUtil = new NpcUtil(context);
        ArrayList<Npc> npcs = npcUtil.loadNpcs(mapName + "npc");
        Map map = new Map();
        try {
            InputStream inStream = context.getAssets().open(mapName + ".map");
            Scanner in = new Scanner(inStream);
            int x, y = 0;
            while (in.hasNextLine()) {
                x = 0;
                char[] line = in.nextLine().toCharArray();

                for (int i = 0; i < line.length - 1; i += 2) {
                    char typeChar = line[i]; //читаем 2 символа. Первый - тип объекта.
                    char propertyChar = line[i + 1]; //Второй - параметр объекта
                    switch (typeChar) {
                        case '#':
                            int wallType = Character.getNumericValue(propertyChar);
                            map.staticObjects.add(new Wall(x, y, wallType));
                            break;
                        case 'T':
                            int treeType = Character.getNumericValue(propertyChar);
                            map.staticObjects.add(new Tree(x, y, treeType));
                            break;
                        case '\\': //Буква. Показываем букву из параметра
                            map.staticObjects.add(new Letter(x, y, propertyChar));
                            break;
                        case 'P':
                            //Игрок. Первай симол - позиция.
                            map.startPlayerPosition = new Position(x, y);
                            //Второй символ - направление
                            switch (propertyChar) {
                                case 'U':
                                    map.startPlayerDirection = Direction.UP;
                                    break;
                                case 'L':
                                    map.startPlayerDirection = Direction.LEFT;
                                    break;
                                case 'R':
                                    map.startPlayerDirection = Direction.RIGHT;
                                    break;
                                case 'D':
                                    map.startPlayerDirection = Direction.DOWN;
                                    break;
                            }
                            break;
                        case 'S':
                            int merchantLevel = Character.getNumericValue(propertyChar);
                            map.merchants.add(new Merchant(x, y, merchantLevel));
                            break;
                        case 'M':
                            //Первый символ - позиция моба. Вторая - уровень
                            int monsterLevel = Character.getNumericValue(propertyChar);
                            map.monsters.add(createMonster(monsterLevel, x, y));
                            break;
                        case 'N': //NPC
                            //Первый символ - позиция непися. Вторая - его id в файле <map_name>npc.json
                            int npcId = Character.getNumericValue(propertyChar);
                            Npc npc = getNpc(npcs, npcId);
                            npc.setPosition(x, y);
                            map.npcs.add(npc);
                            System.out.println();
                            break;
                    }
                    x++;
                }
                y++;
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
    }

    public Npc getNpc(ArrayList<Npc> npcs, int id) {
        for (Npc npc : npcs) {
            if (npc.id == id) {
                Npc newNpc = new Npc();
                newNpc.id = npc.id;
                newNpc.name = npc.name;
                newNpc.npcPhrases = npc.npcPhrases;
                newNpc.playerPhrases = npc.playerPhrases;
                newNpc.playerAnswers = npc.playerAnswers;
                newNpc.npcAnswers = npc.npcAnswers;
                return newNpc;
            }
        }
        return null;
    }

    public static Monster createMonster(int level, int x, int y) {
        Monster monster = new Monster(x, y);
        //Генерируем имя
        String[][] adjectives = {
                {"Слабый", "Тупой", "Унылый", "Голый", "Полумертвый", "Недобитый", "Мелкий", "Сонный"},
                {"Голодный", "Дикий", "Черный", "Волосатый", "Сбежавший", "Глухой", "Вонючий"},
                {"Сильный", "Могучий", "Великий", "Суровый", "Жуткий", "Свирепый", "Непобедимый", "Быстрый"}
        };
        String[] nouns = {"Гадобрызг", "Пиродактиль", "Жованый Крот", "Муравчик", "Буровозик", "Урчальник", "Синегал", "Робокот", "Таджук"};
        String name;
        if (level < 3)
            name = getRandomElement(adjectives[0]) + " " + getRandomElement(nouns);
        else if (level < 6)
            name = getRandomElement(adjectives[1]) + " " + getRandomElement(nouns);
        else
            name = getRandomElement(adjectives[2]) + " " + getRandomElement(nouns);
        monster.stats.name = name;
        monster.stats.exp = (int) (Math.random() * level * 100);
        monster.stats.minDamage = (int) (Math.random() * level * 10);
        monster.stats.maxDamage = (int) (Math.random() * level * 10 + 10);
        monster.stats.hp = (int) (Math.random() * level * 100);
        monster.gold = (int) (Math.random() * (level) * 5);
        return monster;
    }

    public static Skill.AttackSkills createBattleSkill(int level) {
        String[][] adjectives = {
                {"Слабое", "Ленивое", "Сонное", "Вялое", "Неумелое"},
                {"Значительное", "Красивое", "Умелое", "Быстрое"},
                {"Профессиональное", "Суровое", "Жесткое"},
        };
        String[] nouns = {"Выжигание", "Фтыкание", "Разрывание", "Съедение", "Оскорбление", "Распиливание", "Пропивание", "Растаптывание", "Раздалбывание"};
        String name;
        if (level < 3)
            name = getRandomElement(adjectives[0]) + " " + getRandomElement(nouns);
        else if (level < 6)
            name = getRandomElement(adjectives[1]) + " " + getRandomElement(nouns);
        else
            name = getRandomElement(adjectives[2]) + " " + getRandomElement(nouns);

        int manaCost = (int) ((level + Math.random()) * 10);
        int minAttack = (int) ((level + Math.random()) * 10);
        int maxAttack = (int) ((level + Math.random() + 10) * 10);
        return new Skill.AttackSkills(name, minAttack, maxAttack, manaCost);
    }

    private static <T> T getRandomElement(T[] elements) {
        int index = (int) (Math.random() * elements.length - 1);
        return elements[index];
    }
}