package ru.npu3pak.rpg.game.equipment;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.npu3pak.rpg.game.CharacterStats;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 25.11.13
 * Time: 8:45
 */
public class ItemsUtil {
    private Context context;

    public ItemsUtil(Context context) {
        this.context = context;
    }

    public ArrayList<Item> loadAllItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        items.addAll(loadItems("potions", Item.ItemType.POTION));
        items.addAll(loadItems("weapons", Item.ItemType.WEAPON));
        items.addAll(loadItems("armor", Item.ItemType.ARMOR));
        return items;
    }

    public ArrayList<Item> loadItems(String fileName, Item.ItemType itemType) {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            InputStream inStream = context.getAssets().open(fileName + ".json");
            Scanner in = new Scanner(inStream);
            StringBuilder jsonBuilder = new StringBuilder();
            while (in.hasNextLine()) {
                jsonBuilder.append(in.nextLine());
            }

            String json = jsonBuilder.toString();
            JSONArray itemsArray = new JSONArray(json);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemJson = itemsArray.getJSONObject(i);
                Item item = new Item();
                item.level = itemJson.getInt("level");
                item.name = itemJson.getString("name");
                item.type = itemType;
                item.modifiers = CharacterStats.fromJson(itemJson);
                if (itemJson.has("price"))
                    item.price = itemJson.getInt("price");
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
