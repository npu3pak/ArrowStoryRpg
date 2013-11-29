package ru.npu3pak.rpg.game.map;

import android.widget.TextView;
import ru.npu3pak.rpg.game.Position;
import ru.npu3pak.rpg.game.map.dynamic_objects.Merchant;
import ru.npu3pak.rpg.game.map.dynamic_objects.Monster;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Npc;
import ru.npu3pak.rpg.game.map.static_objects.StaticObject;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 20.11.13
 * Time: 8:18
 */
public class MapDisplay {
    public static final int VISIBLE_DISTANCE = 10;

    private TextView displayView;

    public MapDisplay(TextView displayView) {
        this.displayView = displayView;
    }

    public void showScreen(Map map) {
        //Формируем видимую область и делаем ее пустой
        char[][] region = new char[VISIBLE_DISTANCE * 2 + 1][VISIBLE_DISTANCE * 2 + 1];
        for (char[] row : region)
            Arrays.fill(row, ' ');

        Player player = map.player;

        //Рисуем в видимой области мобов
        for (Monster monster : map.monsters)
            drawObject(region, player.position, monster);

        //Рисуем торговцев
        for (Merchant merchant : map.merchants)
            drawObject(region, player.position, merchant);

        //Рисуем неписей
        for (Npc npc : map.npcs)
            drawObject(region, player.position, npc);

        //Рисуем в центре видимой области игрока
        region[VISIBLE_DISTANCE][VISIBLE_DISTANCE] = player.getViewChar();

        //Рисуем в видимой области статические объекты.
        //Важно это делать после отрисовки игрока, чтобы его не было видно за деревьями
        for (StaticObject staticObject : map.staticObjects)
            drawObject(region, player.position, staticObject);

        //Формируем строку для вывода на экран
        StringBuilder builder = new StringBuilder();
        for (char[] row : region) {
            for (char aRow : row) {
                builder.append(aRow);
            }
            builder.append('\n');
        }

        displayView.setText(builder.toString());
    }


    private void drawObject(char[][] region, Position playerPosition, MapObject object) {
        Position objectPosition = object.position;
        if (objectPosition.x >= playerPosition.x - VISIBLE_DISTANCE && objectPosition.y >= playerPosition.y - VISIBLE_DISTANCE &&
                objectPosition.x <= playerPosition.x + VISIBLE_DISTANCE && objectPosition.y <= playerPosition.y + VISIBLE_DISTANCE) {
            int regionObjX = objectPosition.x - playerPosition.x + VISIBLE_DISTANCE;
            int regionObjY = objectPosition.y - playerPosition.y + VISIBLE_DISTANCE;
            region[regionObjY][regionObjX] = object.getViewChar();
        }
    }
}
