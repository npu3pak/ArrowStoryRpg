package ru.npu3pak.rpg.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ru.npu3pak.rpg.R;
import ru.npu3pak.rpg.game.Direction;
import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.map.Map;
import ru.npu3pak.rpg.game.map.MapDisplay;
import ru.npu3pak.rpg.game.map.dynamic_objects.Merchant;
import ru.npu3pak.rpg.game.map.dynamic_objects.Monster;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Npc;

public class AdventureActivity extends GameActivity {
    private MapDisplay mapDisplay;

    private Player player;
    private Map map;
    //Сохраняем монстра, с которым воюем, чтобы потом, в случае победы, его стереть с карты
    private Monster lastMonster;

    public static final int REQUEST_CODE_START_BATTLE = 20;
    public static final int REQUEST_CODE_START_TRADE = 21;
    public static final int REQUEST_CODE_START_TALKING = 22;
    public static final int REQUEST_CODE_SHOW_INVENTORY = 23;
    public static final int REQUEST_CODE_SHOW_STATS = 24;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventure);
        TextView textDisplay = (TextView) findViewById(R.id.adventure_text_display);
        mapDisplay = new MapDisplay(textDisplay);
        initNewGame();
        refreshScreen();
    }

    public void refreshScreen() {
        mapDisplay.showScreen(map);
    }

    public void initNewGame() {
        map = Map.load(this, "map1");
        player = new Player(map.getStartPlayerPosition(), map.getStartPlayerDirection());
        map.player = player;
        player.direction = map.getStartPlayerDirection();
    }

    @SuppressWarnings("unused")
    public void OnStatsButtonClick(View sender) {
        Intent showStats = new Intent(this, StatsActivity.class);
        showStats.putExtra(StatsActivity.INTENT_KEY_PLAYER, player);
        startActivityForResult(showStats, REQUEST_CODE_SHOW_STATS);
    }

    @SuppressWarnings("unused")
    public void OnInventoryButtonClick(View sender) {
        Intent showInventory = new Intent(this, InventoryActivity.class);
        showInventory.putExtra(InventoryActivity.INTENT_KEY_PLAYER, player);
        startActivityForResult(showInventory, REQUEST_CODE_SHOW_INVENTORY);
    }

    @SuppressWarnings("unused")
    public void OnForwardButtonClick(View sender) {
        player.direction = Direction.UP;
        makeMove();
    }

    @SuppressWarnings("unused")
    public void OnBackButtonClick(View sender) {
        player.direction = Direction.DOWN;
        makeMove();
    }

    @SuppressWarnings("unused")
    public void OnMoveLeftButtonClick(View sender) {
        player.direction = Direction.LEFT;
        makeMove();
    }

    @SuppressWarnings("unused")
    public void OnMoveRightButtonClick(View sender) {
        player.direction = Direction.RIGHT;
        makeMove();
    }

    private void makeMove() {
        Position nextPosition = player.position.getNextForwardPosition(player.direction);
        if (map.canMove(nextPosition))
            player.position = nextPosition;

        checkMonsterFight();
        checkMerchant();
        checkNpc();

        refreshScreen();
    }

    public void checkNpc() {
        Npc npc = map.checkNpc(player.position);
        if (npc != null) {
            Intent startTalking = new Intent(this, TalkingActivity.class);
            startTalking.putExtra(TalkingActivity.INTENT_KEY_PLAYER, player);
            startTalking.putExtra(TalkingActivity.INTENT_KEY_NPC, npc);
            startActivityForResult(startTalking, REQUEST_CODE_START_TALKING);
        }
    }

    public void checkMerchant() {
        Merchant merchant = map.checkMerchant(player.position);
        if (merchant != null) {
            Intent startTrade = new Intent(this, TradeActivity.class);
            startTrade.putExtra(TradeActivity.INTENT_KEY_PLAYER, player);
            startTrade.putExtra(TradeActivity.INTENT_KEY_MERCHANT, merchant);
            startActivityForResult(startTrade, REQUEST_CODE_START_TRADE);
        }
    }

    public void checkMonsterFight() {
        Monster monster = map.checkMonster(player.position);
        if (monster != null) {
            lastMonster = monster;
            Intent startBattle = new Intent(this, BattleActivity.class);
            startBattle.putExtra(BattleActivity.BUNDLE_KEY_PLAYER, player);
            startBattle.putExtra(BattleActivity.BUNDLE_KEY_MONSTER, monster);
            startActivityForResult(startBattle, REQUEST_CODE_START_BATTLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Вернулись с экрана торговли
        if (requestCode == REQUEST_CODE_START_TRADE && resultCode == Activity.RESULT_OK) {
            Player playerData = (Player) data.getSerializableExtra(TradeActivity.INTENT_KEY_PLAYER);
            player.gold = playerData.gold;
            player.items = playerData.items;
        }
        //Вернулись с экрана битвы
        else if (requestCode == REQUEST_CODE_START_BATTLE)
            switch (resultCode) {
                case Activity.RESULT_OK:
                    BattleActivity.BattleResult battleResult = (BattleActivity.BattleResult) data.getSerializableExtra(BattleActivity.BUNDLE_KEY_BATTLE_RESULT);
                    if (battleResult == BattleActivity.BattleResult.WIN)
                        onBattleWin(data);
                    else if (battleResult == BattleActivity.BattleResult.FLEE)
                        onBattleFlee(data);
                    else if (battleResult == BattleActivity.BattleResult.LOSE)
                        showMessageDialog("GAME OVER", lastMonster.stats.name + " убил доблестного война. RIP.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    break;
                case Activity.RESULT_CANCELED:
                    finish();
                    break;
            }
            //Вернулись с экрана инвентаря
        else if (requestCode == REQUEST_CODE_SHOW_INVENTORY && resultCode == Activity.RESULT_OK) {
            Player playerData = (Player) data.getSerializableExtra(InventoryActivity.INTENT_KEY_PLAYER);
            player.gold = playerData.gold;
            player.items = playerData.items;
            player.equipment = playerData.equipment;
        }
        //Вернулись из окна статистики
        else if (requestCode == REQUEST_CODE_SHOW_STATS && resultCode == Activity.RESULT_OK) {
            Player playerData = (Player) data.getSerializableExtra(StatsActivity.INTENT_KEY_PLAYER);
            player.gold = playerData.gold;
            player.items = playerData.items;
            player.equipment = playerData.equipment;
            player.stats = playerData.stats;
        }
    }

    private void onBattleFlee(Intent data) {
        showToast("%s зазевался. Крути педали, пока не дали!", lastMonster.stats.name);
        Player playerData = (Player) data.getSerializableExtra(BattleActivity.BUNDLE_KEY_PLAYER);
        player.stats = playerData.stats;
        player.items = playerData.items;
        Monster monsterData = (Monster) data.getSerializableExtra(BattleActivity.BUNDLE_KEY_MONSTER);
        lastMonster.stats = monsterData.stats;
    }

    private void onBattleWin(Intent data) {
        showToast("%s был побежден доблестным войном!\nПолучено %dEXP %d$", lastMonster.stats.name, lastMonster.stats.exp, lastMonster.gold);
        map.removeMonster(lastMonster);
        Player playerData = (Player) data.getSerializableExtra(BattleActivity.BUNDLE_KEY_PLAYER);
        player.stats = playerData.stats;
        player.stats.exp += lastMonster.stats.exp;
        player.items = playerData.items;
        player.gold += lastMonster.gold;
        if (player.checkedLevelUp())
            showToast("Уровень персонажа достиг новых высот и теперь равен %d", player.stats.level);
    }


}