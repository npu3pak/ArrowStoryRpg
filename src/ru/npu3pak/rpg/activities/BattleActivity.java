package ru.npu3pak.rpg.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ru.npu3pak.rpg.R;
import ru.npu3pak.rpg.game.Skill;
import ru.npu3pak.rpg.game.equipment.Item;
import ru.npu3pak.rpg.game.map.dynamic_objects.Monster;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 20.11.13
 * Time: 15:48
 */
public class BattleActivity extends GameActivity {
    public static final String BUNDLE_KEY_PLAYER = "Player";
    public static final String BUNDLE_KEY_MONSTER = "Monster";
    public static final String BUNDLE_KEY_BATTLE_RESULT = "BattleResult";

    public static enum BattleResult {WIN, LOSE, FLEE}

    private TextView monsterInfoText;
    private TextView playerHpText;
    private TextView playerManaText;

    private Player player;
    private Monster monster;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.battle);
        monsterInfoText = (TextView) findViewById(R.id.battle_text_monster_info);
        playerHpText = (TextView) findViewById(R.id.battle_text_player_hp);
        playerManaText = (TextView) findViewById(R.id.battle_text_player_mana);

        player = (Player) getIntent().getSerializableExtra(BUNDLE_KEY_PLAYER);
        monster = (Monster) getIntent().getSerializableExtra(BUNDLE_KEY_MONSTER);

        updateInfo();
    }

    private void updateInfo() {
        monsterInfoText.setText(String.format("%s (%dHP)", monster.stats.name, monster.stats.hp));
        playerHpText.setText(String.format("%dHP", player.stats.hp));
        playerManaText.setText(String.format("%dMP", player.stats.mana));
    }

    @SuppressWarnings("unused")
    public void onAttackButtonClick(View view) {
        makeTurn(null);
    }

    @SuppressWarnings("unused")
    public void onFleeButtonClick(View view) {
        if (Math.random() > 0.5) {
            onBattleFlee();
        } else {
            showToast("%s своими цепкими клешнями помешал герою сбежать", monster.stats.name);
            skipTurn();
        }
    }

    @SuppressWarnings("unused")
    public void onItemsButtonClick(View view) {
        String[] itemNames = new String[player.items.size()];
        for (int i = 0; i < itemNames.length; i++) {
            Item item = player.items.get(i);
            itemNames[i] = String.format("%s (%s)", item.name, item.getDescription());
        } //Показываем окно выбора скиллов
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(itemNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                Item item = player.items.get(index);
                player.useItem(item);
                player.items.remove(item);
                updateInfo();
            }
        });
        builder.create().show();
    }


    @SuppressWarnings("unused")
    public void onSkillsButtonClick(View view) {
        //Формируем список названий скиллов для показа в выпадающем списке
        String[] skillNames = new String[player.skills.size()];
        for (int i = 0; i < skillNames.length; i++) {
            Skill.AttackSkills skill = player.skills.get(i);
            skillNames[i] = String.format("%s (%d-%d|%dMP)", skill.name, skill.minDamage, skill.maxDamage, skill.manaCost);
        }


        //Показываем окно выбора скиллов
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(skillNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                Skill.AttackSkills skill = player.skills.get(index);
                if (skill.manaCost <= player.stats.mana)
                    makeTurn(skill);
                else
                    showToast("Недостаточно маны!");
            }
        });
        builder.create().show();
    }

    //Передаем, каким скиллом атакуем. Если просто атака - передаем null
    private void makeTurn(Skill.AttackSkills skill) {
        makePlayerTurn(skill);
        checkConditions();
        makeMonsterTurn();
        checkConditions();
        updateInfo();
    }

    private void skipTurn() {
        makeMonsterTurn();
        checkConditions();
        updateInfo();
    }

    private void makePlayerTurn(Skill.AttackSkills skill) {
        if (skill == null)
            monster.stats.hp = monster.stats.hp - (player.getMinDamage() + (int) (Math.random() * player.getMaxDamage() - player.getMinDamage()));
        else {
            monster.stats.hp = monster.stats.hp - ((int) (Math.random() * skill.maxDamage - skill.minDamage));
            player.stats.mana = player.stats.mana - skill.manaCost;
        }
    }

    private void makeMonsterTurn() {
        int damage = (monster.stats.minDamage + (int) (Math.random() * monster.stats.maxDamage - monster.stats.minDamage));
        player.stats.hp = player.stats.hp - Math.max(damage - player.getArmor(), 0);
    }

    private void checkConditions() {
        if (monster.stats.hp < 1)
            onBattleWin();
        if (player.stats.hp < 1)
            onBattleLose();
    }

    public void onBattleLose() {
        Intent result = new Intent();
        result.putExtra(BUNDLE_KEY_BATTLE_RESULT, BattleResult.LOSE);
        result.putExtra(BUNDLE_KEY_PLAYER, player);
        setResult(RESULT_OK, result);
        finish();
    }

    public void onBattleWin() {
        Intent result = new Intent();
        result.putExtra(BUNDLE_KEY_BATTLE_RESULT, BattleResult.WIN);
        result.putExtra(BUNDLE_KEY_PLAYER, player);
        setResult(RESULT_OK, result);
        finish();
    }

    public void onBattleFlee() {
        Intent result = new Intent();
        result.putExtra(BUNDLE_KEY_BATTLE_RESULT, BattleResult.FLEE);
        result.putExtra(BUNDLE_KEY_PLAYER, player);
        result.putExtra(BUNDLE_KEY_MONSTER, monster);
        setResult(RESULT_OK, result);
        finish();
    }
}