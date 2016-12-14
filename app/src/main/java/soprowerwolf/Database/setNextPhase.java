package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Activities.PhasesActivity.AmorActivity;
import soprowerwolf.Activities.PhasesActivity.AudioActivity;
import soprowerwolf.Activities.PhasesActivity.DiebActivity;
import soprowerwolf.Activities.PhasesActivity.HexeActivity;
import soprowerwolf.Activities.PhasesActivity.JaegerActivity;
import soprowerwolf.Activities.PhasesActivity.LoverActivity;
import soprowerwolf.Activities.PhasesActivity.SeherinActivity;
import soprowerwolf.Activities.PhasesActivity.TagActivity;
import soprowerwolf.Activities.PhasesActivity.WerwolfActivity;
import soprowerwolf.Activities.PhasesActivity.showVictimActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 19.09.2016.
 */
public class setNextPhase extends AsyncTask<String, String, String> {

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();
    GameOverDB go = new GameOverDB();

    private static final String url_set_next_phase = "http://www-e.uni-magdeburg.de/jkloss/setNextPhase.php";

    AmorActivity amor = new AmorActivity();
    AudioActivity audio = new AudioActivity();
    DiebActivity dieb = new DiebActivity();
    HexeActivity hexe = new HexeActivity();
    JaegerActivity jaeger = new JaegerActivity();
    LoverActivity lover = new LoverActivity();
    SeherinActivity seherin = new SeherinActivity();
    showVictimActivity victim = new showVictimActivity();
    TagActivity tag = new TagActivity();
    WerwolfActivity wolf = new WerwolfActivity();

    @Override
    protected String doInBackground(String... params) {

        //close last Activity
        switch(globalVariables.getCurrentPhase()){
            case "Dieb":
                dieb.finish();
                break;
            case "Amor":
                amor.finish();
                break;
            case "Hexe":
                hexe.finish();
                break;
            case "Lover":
                lover.finish();
                break;
            case "Seherin":
                seherin.finish();
                break;
            case "Werwolf":
                wolf.finish();
                break;
            case "OpferTag":
                victim.finish();
                break;
            case "OpferNacht":
                victim.finish();
                break;
            case "Tag":
                tag.finish();
                break;
            case "Jaeger":
                jaeger.finish();
                break;
            case "Audio":
                audio.finish();
                break;
        }

        String gameID = String.valueOf(globalVariables.getGameID());
        String currentPhase = globalVariables.getCurrentPhase();

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", gameID));
        paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));


        if (globalVariables.getCurrentPhase().equals("Audio")){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", "Audio"));
            paramsList.add(new BasicNameValuePair("nextPhase", globalVariables.getNextPhase()));
        }

        if (params[0].equals("audio")){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Audio"));
        }

        if(globalVariables.getJaegerDies() && !params[0].equals("next"))
        {
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Jaeger"));
        }

        go.gameOver();
        if (!globalVariables.getWinner().equals("nobody")){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Spielende"));
        }

        jsonParser.makeHttpRequest(url_set_next_phase, "POST", paramsList);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // if next Phase is set -> get current Phase
        new getCurrentPhase().execute("");
    }
}
