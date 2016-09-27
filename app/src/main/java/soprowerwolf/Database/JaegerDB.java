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
 * Created by Alex on 25.09.2016.
 */

public class JaegerDB extends AsyncTask<String, String, String> {
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    JSONParser jsonParser = new JSONParser();
    private static final String url_shoot = "http://192.168.0.13/SoPro/db_test/setVictims.php";

    @Override
    protected String doInBackground(String... params) {
        String gameID = String.valueOf(globalVariables.getGameID());

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        paramsList.add(new BasicNameValuePair("victimJaeger", params[0]));
        paramsList.add(new BasicNameValuePair("gameID", gameID));

        jsonParser.makeHttpRequest(url_shoot, "POST", paramsList);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        new setNextPhase().execute("next");
    }
}
