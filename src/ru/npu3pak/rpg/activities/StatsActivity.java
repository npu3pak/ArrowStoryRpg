package ru.npu3pak.rpg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ru.npu3pak.rpg.R;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 27.11.13
 * Time: 14:05
 */
public class StatsActivity extends GameActivity {
    public static final String INTENT_KEY_PLAYER = "Player";

    private Player player;

    private TextView levelText;
    private TextView expText;
    private TextView expToLevelText;
    private TextView hpText;
    private TextView manaText;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
        player = (Player) getIntent().getSerializableExtra(INTENT_KEY_PLAYER);

        levelText = (TextView) findViewById(R.id.stats_text_level);
        expText = (TextView) findViewById(R.id.stats_text_exp);
        expToLevelText = (TextView) findViewById(R.id.stats_text_exp_for_level);
        hpText = (TextView) findViewById(R.id.stats_text_hp);
        manaText = (TextView) findViewById(R.id.stats_text_mana);
        refreshInfo();
    }

    private void refreshInfo() {
        levelText.setText("" + player.stats.level);
        expText.setText("" + player.stats.exp);
        expToLevelText.setText("" + (player.getNextLevelExp() - player.stats.exp));
        hpText.setText(player.stats.hp + "/" + player.stats.maxHp);
        manaText.setText(player.stats.mana + "/" + player.stats.maxMana);
    }

    @SuppressWarnings("unused")
    public void onCloseButtonClick(View sender) {
        Intent data = new Intent();
        data.putExtra(INTENT_KEY_PLAYER, player);
        setResult(RESULT_OK, data);
        finish();
    }
}