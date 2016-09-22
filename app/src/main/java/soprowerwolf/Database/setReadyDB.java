package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 22.09.2016.
 */

public class setReadyDB extends AsyncTask<String, String, String> {
    JSONParser jsonParser = new JSONParser();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    private static final String url_set_ready = "http://192.168.0.13/SoPro/db_test/setReady.php";
    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));

        jsonParser.makeHttpRequest(url_set_ready, "POST", paramsList);

        return null;
    }
}
