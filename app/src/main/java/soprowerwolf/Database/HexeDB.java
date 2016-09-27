package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

import static soprowerwolf.Classes.popup.PopUpInfo;

/**
 * Created by Alex on 20.09.2016.
 */

public class HexeDB extends AsyncTask<String, String, String> {
    private JSONParser jsonParser = new JSONParser();

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_update_hexe = "http://192.168.0.13/SoPro/db_test/updateHexe.php";//"http://www-e.uni-magdeburg.de/jkloss/updateHexe.php";

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        switch (params[0])
        {
            case "saveVictim":
                paramsList.add(new BasicNameValuePair("heal", "used"));
                JSONObject jsonObjectSave = jsonParser.makeHttpRequest(url_update_hexe, "POST", paramsList);
                //ToDo: check for success
                break;

            case "killDB":
                String playerID = params[1];

                paramsList.add(new BasicNameValuePair("poison", "used"));
                paramsList.add(new BasicNameValuePair("victimHex", playerID));
                JSONObject jsonObjectKill = jsonParser.makeHttpRequest(url_update_hexe, "POST", paramsList);
                //ToDo: check for success
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
