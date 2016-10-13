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

import soprowerwolf.Activities.GameSetupActivity;
import soprowerwolf.Activities.QRCodeActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 12.09.2016.
 */
public class createGameDB extends AsyncTask<String, String, String> {

    private JSONParser jsonParser = new JSONParser();
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_create_game = "http://www-e.uni-magdeburg.de/jkloss/create_new_game.php";

    @Override
    protected String doInBackground(String... params) {

        String playerID = String.valueOf(globalVariables.getOwnPlayerID());
        String[] cards = globalVariables.getCards();

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        paramsList.add(new BasicNameValuePair("playerID", playerID));
        paramsList.add(new BasicNameValuePair("role", cards[0]));

        jsonParser.makeHttpRequest(url_create_game, "POST", paramsList);

        JSONObject newGame = jsonParser.makeHttpRequest(url_create_game, "GET", paramsList);

        try {
            JSONArray JID = newGame.getJSONArray("gameID");
            globalVariables.setGameID(JID.getJSONObject(JID.length() - 1).getInt("gameID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<NameValuePair> paramsRoles = new ArrayList<NameValuePair>();

        String gameID = String.valueOf(globalVariables.getGameID());
        for (int i = 1; i < cards.length; i++) {
            paramsRoles.clear();
            paramsRoles.add(new BasicNameValuePair("role", cards[i]));
            paramsRoles.add(new BasicNameValuePair("gameID", gameID));
            jsonParser.makeHttpRequest(url_create_game, "POST", paramsRoles);
        }

        List<NameValuePair> paramsPhases = new ArrayList<NameValuePair>();
        paramsPhases.add(new BasicNameValuePair("firstPhase", "true"));

        // if some roles aren't selected -> there would be an error without this
        String[] phases = new String[7];
        int j = 0;
        for (int i = 0; i < globalVariables.getPhases().length; i++) {
            if (!globalVariables.getPhases()[i].equals("")) {
                phases[j] = globalVariables.getPhases()[i];
                j++;

                if (globalVariables.getPhases()[i].equals("Amor")) {
                    phases[j] = "Lover";
                    j++;
                }
            }

        }

        int i;
        for (i = 0; i < phases.length - 2; i++) {

            paramsPhases.add(new BasicNameValuePair("phase", phases[i]));
            paramsPhases.add(new BasicNameValuePair("nextPhase", phases[i + 1]));
            paramsPhases.add(new BasicNameValuePair("gameID", gameID));
            jsonParser.makeHttpRequest(url_create_game, "POST", paramsPhases);
            paramsPhases.clear();
        }

        paramsPhases.clear();
        paramsPhases.add(new BasicNameValuePair("phase", phases[i]));
        paramsPhases.add(new BasicNameValuePair("nextPhase", "OpferNacht"));
        paramsPhases.add(new BasicNameValuePair("gameID", gameID));
        jsonParser.makeHttpRequest(url_create_game, "POST", paramsPhases);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Activity context = globalVariables.getCurrentContext();

        Intent intent = new Intent(context, QRCodeActivity.class);
        context.startActivity(intent);
    }
}
