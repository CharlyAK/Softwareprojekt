package soprowerwolf.Database;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Activities.GameOverActivity;
import soprowerwolf.Activities.PhasesActivity.*;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 17.09.2016.
 */
public class getCurrentPhase extends AsyncTask<String, String, String> {
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();
    NextPhaseDB nextPhaseDB = new NextPhaseDB();
    GameOverDB gameOverDB = new GameOverDB();

    private static final String url_get_current_phase = "http://www-e.uni-magdeburg.de/jkloss/getCurrentPhase.php";
    private String currentPhase;

    @Override
    protected String doInBackground(String... params) {
        String gameID = String.valueOf(globalVariables.getGameID());

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", gameID));

        // get current Phase
        JSONObject JCP = jsonParser.makeHttpRequest(url_get_current_phase, "GET", paramsList);

        try {
            JSONArray ACP = JCP.getJSONArray("currentPhase");
            currentPhase = ACP.getJSONObject(0).getString("currentPhase");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Activity context = globalVariables.getCurrentContext();

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        // get current Phase
        JSONObject JCP = jsonParser.makeHttpRequest(url_get_current_phase, "GET", paramsList);

        try {
            JSONArray ACP = JCP.getJSONArray("currentPhase");
            currentPhase = ACP.getJSONObject(0).getString("currentPhase");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // if current Phase from database doesn't equals current Phase from global variables -> Phase has been changed
        // ==> call next Phase
        if (!currentPhase.equals(globalVariables.getCurrentPhase())) {
            switch (currentPhase) {
                case "Dieb":
                    globalVariables.setCurrentPhase("Dieb");
                    nextPhaseDB.setNextPhase();
                    Intent dieb = new Intent(context, DiebActivity.class);
                    context.startActivity(dieb);
                    break;
                case "Amor":
                    globalVariables.setCurrentPhase("Amor");
                    nextPhaseDB.setNextPhase();
                    Intent amor = new Intent(context, AmorActivity.class);
                    context.startActivity(amor);
                    break;
                case "Lover":
                    globalVariables.setCurrentPhase("Lover");
                    nextPhaseDB.setNextPhase();
                    Intent lover = new Intent(context, LoverActivity.class);
                    context.startActivity(lover);
                    break;
                case "Werwolf":
                    globalVariables.setCurrentPhase("Werwolf");
                    nextPhaseDB.setNextPhase();
                    Intent werwolf = new Intent(context, WerwolfActivity.class);
                    context.startActivity(werwolf);
                    break;
                case "Seherin":
                    globalVariables.setCurrentPhase("Seherin");
                    nextPhaseDB.setNextPhase();
                    Intent seherin = new Intent(context, SeherinActivity.class);
                    context.startActivity(seherin);
                    break;
                case "Hexe":
                    globalVariables.setCurrentPhase("Hexe");
                    nextPhaseDB.setNextPhase();
                    Intent hexe = new Intent(context, HexeActivity.class);
                    context.startActivity(hexe);
                    break;
                case "OpferNacht":
                    globalVariables.setCurrentPhase("OpferNacht");
                    nextPhaseDB.setNextPhase();
                    Intent victimsNight = new Intent(context, showVictimActivity.class);
                    context.startActivity(victimsNight);
                    break;
                case "Tag":
                    globalVariables.setCurrentPhase("Tag");
                    nextPhaseDB.setNextPhase();
                    Intent tag = new Intent(context, TagActivity.class);
                    context.startActivity(tag);
                    break;
                case "OpferTag":
                    globalVariables.setCurrentPhase("OpferTag");
                    nextPhaseDB.setNextPhase();
                    Intent victimsDay = new Intent(context, showVictimActivity.class);
                    context.startActivity(victimsDay);
                    break;
                case "Jaeger":
                    globalVariables.setCurrentPhase("Jaeger");
                    nextPhaseDB.setNextPhase();
                    Intent jaeger = new Intent(context, JaegerActivity.class);
                    context.startActivity(jaeger);
                    break;
                case "Spielende":
                    gameOverDB.gameOver();
                    globalVariables.setCurrentPhase("Spielende");
                    nextPhaseDB.setNextPhase();
                    Intent gameOver = new Intent(context, GameOverActivity.class);
                    context.startActivity(gameOver);
                    break;
                case "Audio":
                    Intent sleep = new Intent(context, AudioActivity.class);
                    context.startActivity(sleep);
                    break;
            }
        }
    }
}
