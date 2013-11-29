package ru.npu3pak.rpg.game.map.dynamic_objects;

import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.equipment.Item;
import ru.npu3pak.rpg.game.map.MapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.npu3pak.rpg.game.equipment.Item.ItemType;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 25.11.13
 * Time: 10:11
 */
public class Merchant extends MapObject {
    private List<ItemType> availableItemTypes = null;
    public int level;

    public Merchant(int x, int y, int level, ItemType... itemTypes) {
        super(new Position(x, y));
        this.level = level;
        if (itemTypes.length > 0)
            availableItemTypes = Arrays.asList(itemTypes);
    }

    @Override
    public char getViewChar() {
        return 'S';
    }

    public List<Item> filterAvailableItems(List<Item> allItems) {
        List<Item> availableItems = new ArrayList<Item>();
        for (Item item : allItems)
            if ((availableItemTypes == null || availableItemTypes.contains(item.type)) && item.level <= level)
                availableItems.add(item);
        return availableItems;
    }
}
