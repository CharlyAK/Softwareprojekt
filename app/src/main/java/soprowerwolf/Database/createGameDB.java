package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 12.09.2016.
 */
public class createGameDB extends AsyncTask<String, String, String> {

    JSONParser jsonParser = new JSONParser();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
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
            globalVariables.setGameID(JID.getJSONObject(JID.length()-1).getInt("gameID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<NameValuePair> paramsRoles = new ArrayList<NameValuePair>();

        String gameID = String.valueOf(globalVariables.getGameID());
        for(int i = 1; i < cards.length; i++)
        {
            paramsRoles.clear();
            paramsRoles.add(new BasicNameValuePair("role", cards[i]));
            paramsRoles.add(new BasicNameValuePair("gameID", gameID));
            jsonParser.makeHttpRequest(url_create_game, "POST", paramsRoles);
        }

        return null;
    }
}
