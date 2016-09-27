package soprowerwolf.Database;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Activities.GameOverActivity;
import soprowerwolf.Activities.PhasesActivity.AmorActivity;
import soprowerwolf.Activities.PhasesActivity.DiebActivity;
import soprowerwolf.Activities.PhasesActivity.HexeActivity;
import soprowerwolf.Activities.PhasesActivity.JaegerActivity;
import soprowerwolf.Activities.PhasesActivity.SeherinActivity;
import soprowerwolf.Activities.PhasesActivity.TagActivity;
import soprowerwolf.Activities.PhasesActivity.WerwolfActivity;
import soprowerwolf.Activities.PhasesActivity.showVictimActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;
import soprowerwolf.R;

/**
 * Created by Alex on 17.09.2016.
 */
public class getCurrentPhase extends AsyncTask<String, String, String> {
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();

    private static final String url_get_current_phase = "http://192.168.0.13/SoPro/db_test/getCurrentPhase.php";//"http://www-e.uni-magdeburg.de/jkloss/getCurrentPhase.php";
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

        // if current Phase from database doesn't equals current Phase from global Variabels -> Phase has been changed
        // ==> call next Phase
        if (!currentPhase.equals(globalVariables.getCurrentPhase())) {
            switch (currentPhase) {
                case "Dieb":
                    globalVariables.setCurrentPhase("Dieb");
                    Intent dieb = new Intent(context, DiebActivity.class);
                    context.startActivity(dieb);
                    break;
                case "Amor":
                    globalVariables.setCurrentPhase("Amor");
                    Intent amor = new Intent(context, AmorActivity.class);
                    context.startActivity(amor);
                    break;
                case "Werwolf":
                    globalVariables.setCurrentPhase("Werwolf");
                    Intent werwolf = new Intent(context, WerwolfActivity.class);
                    context.startActivity(werwolf);
                    break;
                case "Seherin":
                    globalVariables.setCurrentPhase("Seherin");
                    Intent seherin = new Intent(context, SeherinActivity.class);
                    context.startActivity(seherin);
                    break;
                case "Hexe":
                    globalVariables.setCurrentPhase("Hexe");
                    Intent hexe = new Intent(context, HexeActivity.class);
                    context.startActivity(hexe);
                    break;
                case "OpferNacht":
                    globalVariables.setCurrentPhase("OpferNacht");
                    Intent victimsNight = new Intent(context, showVictimActivity.class);
                    context.startActivity(victimsNight);
                    break;
                case "Tag":
                    globalVariables.setCurrentPhase("Tag");
                    Intent tag = new Intent(context, TagActivity.class);
                    context.startActivity(tag);
                    break;
                case "OpferTag":
                    globalVariables.setCurrentPhase("OpferTag");
                    Intent victimsDay = new Intent(context, showVictimActivity.class);
                    context.startActivity(victimsDay);
                    break;
                case "Jaeger":
                    globalVariables.setCurrentPhase("Jaeger");
                    Intent jaeger = new Intent(context, JaegerActivity.class);
                    context.startActivity(jaeger);
                    break;
                case "Spielende":
                    globalVariables.setCurrentPhase("Spielende");
                    Intent gameOver = new Intent(context, GameOverActivity.class);
                    context.startActivity(gameOver);
                    break;
            }
        }
    }
}
