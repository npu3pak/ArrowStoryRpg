package ru.npu3pak.rpg.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ru.npu3pak.rpg.R;
import ru.npu3pak.rpg.game.equipment.Item;
import ru.npu3pak.rpg.game.equipment.ItemsUtil;
import ru.npu3pak.rpg.game.map.dynamic_objects.Merchant;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 25.11.13
 * Time: 9:21
 */
public class TradeActivity extends GameActivity {
    public static final String INTENT_KEY_PLAYER = "player";
    public static final String INTENT_KEY_MERCHANT = "merchant";
    private Player player;

    private TextView goldText;

    private List<Item> merchantItems;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade);
        ItemsUtil itemsUtil = new ItemsUtil(this);
        player = (Player) getIntent().getSerializableExtra(INTENT_KEY_PLAYER);
        Merchant merchant = (Merchant) getIntent().getSerializableExtra(INTENT_KEY_MERCHANT);
        goldText = (TextView) findViewById(R.id.trade_text_gold);
        ArrayList<Item> allItems = itemsUtil.loadAllItems();
        merchantItems = merchant.filterAvailableItems(allItems);
        updateInfo();
    }

    private void updateInfo() {
        goldText.setText(String.format("Gold: %d$", player.gold));
    }

    private void showItemsList(String title, List<Item> items, DialogInterface.OnClickListener clickListener) {
        String[] itemNames = new String[items.size()];
        for (int i = 0; i < itemNames.length; i++) {
            Item item = items.get(i);
            itemNames[i] = String.format("%s\n%s\n%d$", item.name, item.getDescription(), item.price);
        } //Показываем окно выбора скиллов
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(itemNames, clickListener);
        builder.create().show();
    }

    @SuppressWarnings("unused")
    public void onBuyButtonClick(View sender) {
        showItemsList("What are you buying?", merchantItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                Item selectedItem = merchantItems.get(index);
                if (player.gold >= selectedItem.price) {
                    player.gold -= selectedItem.price;
                    player.items.add(selectedItem);
                    updateInfo();
                } else
                    showToast("Недостаточно денег");
            }
        });
    }

    @SuppressWarnings("unused")
    public void onSellButtonClick(View sender) {
        showItemsList("What are you selling?", player.items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                Item selectedItem = player.items.get(index);
                if (selectedItem.equals(player.equipment.armor) || selectedItem.equals(player.equipment.weapon))
                    showToast("Нельзя продать предмет, пока его носит герой");
                else {
                    player.gold += selectedItem.price;
                    player.items.remove(selectedItem);
                }
                updateInfo();
            }
        });
    }

    @SuppressWarnings("unused")
    public void onLeaveButtonClick(View sender) {
        Intent data = new Intent();
        data.putExtra(INTENT_KEY_PLAYER, player);
        setResult(RESULT_OK, data);
        finish();
    }
}