package ru.npu3pak.rpg.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ru.npu3pak.rpg.R;
import ru.npu3pak.rpg.game.map.dynamic_objects.Player;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Conversation;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Npc;
import ru.npu3pak.rpg.game.map.dynamic_objects.npc.Phrase;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 27.11.13
 * Time: 8:02
 */
public class TalkingActivity extends GameActivity {
    public static final String INTENT_KEY_PLAYER = "Player";
    public static final String INTENT_KEY_NPC = "Npc";
    private TextView npcPhraseText;
    private TextView playerPhraseText;
    private Button answerButton;

    private Npc npc;
    private Player player;

    private Phrase lastNpcPhrase;
    private Phrase lastPlayerPhrase;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talking);
        npcPhraseText = (TextView) findViewById(R.id.talking_text_npc_phrase);
        playerPhraseText = (TextView) findViewById(R.id.talking_text_player_phrase);
        answerButton = (Button) findViewById(R.id.talking_button_answer);
        npc = (Npc) getIntent().getSerializableExtra(INTENT_KEY_NPC);
        player = (Player) getIntent().getSerializableExtra(INTENT_KEY_PLAYER);
        UpdateScreen();
    }

    private void UpdateScreen() {
        Phrase[] possibleNpcAnswers = getPossibleAnswers(npc.npcAnswers, lastPlayerPhrase);
        lastNpcPhrase = possibleNpcAnswers != null ? possibleNpcAnswers[0] : npc.npcPhrases[0];
        if (lastNpcPhrase != null)
            npcPhraseText.setText(lastNpcPhrase.text);
        if (lastPlayerPhrase != null)
            playerPhraseText.setText(lastPlayerPhrase.text);

        Phrase[] possiblePlayerAnswers = getPossibleAnswers(npc.playerAnswers, lastNpcPhrase);
        if (possiblePlayerAnswers == null || possiblePlayerAnswers.length == 0)
            onDialogEnd();
    }

    @SuppressWarnings("unused")
    public void onAnswerButtonClick(View sender) {
        final Phrase[] possibleAnswers = getPossibleAnswers(npc.playerAnswers, lastNpcPhrase);
        String[] answerTexts = new String[possibleAnswers.length];
        for (int i = 0; i < possibleAnswers.length; i++) {
            Phrase phrase = possibleAnswers[i];
            answerTexts[i] = phrase.text;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(answerTexts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lastPlayerPhrase = possibleAnswers[which];
                UpdateScreen();
            }
        });
        builder.create().show();
    }

    private void onDialogEnd() {
        answerButton.setEnabled(false);
    }

    private Phrase[] getPossibleAnswers(Conversation[] conversations, Phrase phrase) {
        for (Conversation conversation : conversations) {
            if (conversation.phrase.equals(phrase))
                return conversation.answers;
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void onEndTalkingButtonClick(View sender) {
        setResult(RESULT_OK);
        finish();
    }
}