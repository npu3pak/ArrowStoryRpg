package ru.npu3pak.rpg.game.map.dynamic_objects.npc;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: EVSafronov
 * Date: 26.11.13
 * Time: 15:49
 */
public class NpcUtil {
    private Context context;

    public NpcUtil(Context context) {
        this.context = context;
    }

    public ArrayList<Npc> loadNpcs(String fileName) {
        ArrayList<Npc> npcs = new ArrayList<Npc>();
        try {
            InputStream inStream = context.getAssets().open(fileName + ".json");
            Scanner in = new Scanner(inStream);
            StringBuilder jsonBuilder = new StringBuilder();
            while (in.hasNextLine())
                jsonBuilder.append(in.nextLine());
            String json = jsonBuilder.toString();
            JSONArray itemsArray = new JSONArray(json);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemJson = itemsArray.getJSONObject(i);
                npcs.add(createNpc(itemJson));
            }
            return npcs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Npc createNpc(JSONObject json) throws JSONException {
        Npc npc = new Npc();
        npc.id = json.getInt("id");
        npc.name = json.getString("name");
        JSONArray npcPhrases = json.getJSONArray("npcPhrases");
        npc.npcPhrases = getPhrases(npcPhrases);
        JSONArray playerPhrases = json.getJSONArray("playerPhrases");
        npc.playerPhrases = getPhrases(playerPhrases);
        JSONArray playerToNpcJson = json.getJSONArray("npcAnswers");
        npc.npcAnswers = getConversations(npc.playerPhrases, npc.npcPhrases, playerToNpcJson);
        JSONArray npcToPlayer = json.getJSONArray("playerAnswers");
        npc.playerAnswers = getConversations(npc.npcPhrases, npc.playerPhrases, npcToPlayer);
        return npc;
    }

    private Phrase[] getPhrases(JSONArray jsonArray) throws JSONException {
        Phrase[] phrases = new Phrase[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String text = jsonObject.getString("text");
            Phrase phrase = new Phrase(id, text);
            phrases[i] = phrase;
        }
        return phrases;
    }

    private Conversation[] getConversations(Phrase[] phrases, Phrase[] answers, JSONArray jsonArray) throws JSONException {
        Conversation[] conversations = new Conversation[jsonArray.length()];
        for (int i = 0; i < conversations.length; i++) {
            JSONObject conversationJson = jsonArray.getJSONObject(i);
            int phraseId = conversationJson.getInt("phrase");
            Phrase phrase = getPhrase(phraseId, phrases);
            JSONArray answersJson = conversationJson.getJSONArray("answers");
            Phrase[] conversationAnswers = new Phrase[answersJson.length()];
            for (int j = 0; j < answersJson.length(); j++) {
                JSONObject answerJson = answersJson.getJSONObject(j);
                int answerId = answerJson.getInt("id");
                Phrase answer = getPhrase(answerId, answers);
                conversationAnswers[j] = answer;
            }
            conversations[i] = new Conversation(phrase, conversationAnswers);
        }
        return conversations;
    }

    private Phrase getPhrase(int id, Phrase[] phrases) {
        for (Phrase phrase : phrases) {
            if (phrase.id == id)
                return phrase;
        }
        return null;
    }
}
