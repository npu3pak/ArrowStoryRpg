package ru.npu3pak.rpg.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import ru.npu3pak.rpg.R;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 29.11.13
 * Time: 7:35
 */
public class GameActivity extends Activity {

    public void showToast(String template, Object... args) {
        Toast.makeText(this, String.format(template, args), Toast.LENGTH_LONG).show();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showMessageDialog(String title, String message, DialogInterface.OnClickListener clickListener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if (clickListener != null)
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", clickListener);
        else
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();
    }

    public void showList(String title, String[] items, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(items, clickListener);
        builder.create().show();
    }
}
