package ru.npu3pak.rpg.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ru.npu3pak.rpg.R;
import ru.npu3pak.rpg.game.equipment.Item;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 27.11.13
 * Time: 11:14
 */
public class InventoryActivity extends GameActivity {
    public static final String INTENT_KEY_PLAYER = "Player";
    private Player player;

    private TextView goldText;
    private TextView damageText;
    private TextView armorClassText;
    private TextView weaponText;
    private TextView armorText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);
        goldText = (TextView) findViewById(R.id.inventory_text_gold);
        damageText = (TextView) findViewById(R.id.inventory_text_damage);
        armorClassText = (TextView) findViewById(R.id.inventory_text_armor_class);
        weaponText = (TextView) findViewById(R.id.inventory_text_weapon);
        armorText = (TextView) findViewById(R.id.inventory_text_armor);
        player = (Player) getIntent().getSerializableExtra(INTENT_KEY_PLAYER);
        refreshInfo();
    }

    private void refreshInfo() {
        goldText.setText(player.gold + "$");
        damageText.setText(player.getMinDamage() + "-" + player.getMaxDamage());
        armorClassText.setText("" + player.getArmor());
        if (player.equipment.weapon != null)
            weaponText.setText(player.equipment.weapon.name);
        if (player.equipment.armor != null)
            armorText.setText(player.equipment.armor.name);
    }

    private void showItemsList(List<Item> items, DialogInterface.OnClickListener clickListener) {
        String[] itemNames = new String[items.size()];
        for (int i = 0; i < itemNames.length; i++) {
            Item item = items.get(i);
            itemNames[i] = String.format("%s\n%s", item.name, item.getDescription());
        } //Показываем окно выбора скиллов
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(itemNames, clickListener);
        builder.create().show();
    }

    @SuppressWarnings("unused")
    public void onChangeArmorButtonClick(View sender) {
        final List<Item> items = getItemsOfType(Item.ItemType.ARMOR);
        if (items.size() == 0)
            showToast("Броня отсутствует в инвентаре");
        else
            showItemsList(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    player.equipment.armor = items.get(which);
                    refreshInfo();
                }
            });
    }

    @SuppressWarnings("unused")
    public void onChangeWeaponButtonClick(View sender) {
        final List<Item> items = getItemsOfType(Item.ItemType.WEAPON);
        if (items.size() == 0)
            showToast("Оружие отсутствует в инвентаре");
        else
            showItemsList(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    player.equipment.weapon = items.get(which);
                    refreshInfo();
                }
            });
    }

    private List<Item> getItemsOfType(Item.ItemType type) {
        List<Item> items = new ArrayList<Item>();
        for (Item item : player.items)
            if (item.type == type)
                items.add(item);
        return items;
    }

    @SuppressWarnings("unused")
    public void onCloseButtonClick(View sender) {
        Intent result = new Intent();
        result.putExtra(INTENT_KEY_PLAYER, player);
        setResult(RESULT_OK, result);
        finish();
    }
}