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
 * Created by Alex on 11.09.2016.
 */
public class loginDB extends AsyncTask<String, String, String> {

    JSONParser jsonParser = new JSONParser();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_login = "http://192.168.0.67/SoPro/db_test/login.php";
    @Override
    protected String doInBackground(String... params) {

        String success = null;
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        paramsList.add(new BasicNameValuePair("email", params[0]));
        paramsList.add(new BasicNameValuePair("password", params[1]));

        JSONObject login = jsonParser.makeHttpRequest(url_login, "GET", paramsList);
        try {
            if (login.getInt("success") == 1)
            {

                JSONArray JID = login.getJSONArray("playerID");
                globalVariables.setOwnPlayerID(JID.getJSONObject(JID.length()-1).getInt("playerID"));

                success = "true";
            }
            else {
                success = "false";
                globalVariables.setOwnPlayerID(-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return success;
    }
    @Override
    protected void onCancelled(){

    }
}
