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
public class joinGameDB extends AsyncTask<String, String, String> {

    JSONParser jsonParser = new JSONParser();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_join_game = "http://192.168.0.158/SoPro/db_test/insert_player.php";

    @Override
    protected String doInBackground(String... params) {

        String playerID = String.valueOf(globalVariables.getOwnPlayerID());
        String gameID = String.valueOf(globalVariables.getGameID());

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        paramsList.add(new BasicNameValuePair("playerID", playerID));
        paramsList.add(new BasicNameValuePair("gameID", gameID));

        jsonParser.makeHttpRequest(url_join_game, "POST", paramsList);

        JSONObject Join = jsonParser.makeHttpRequest(url_join_game, "GET", paramsList);

        try {
            JSONArray J = Join.getJSONArray("role");
            globalVariables.setOwnRole(J.getJSONObject(J.length()-1).getString("role"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
