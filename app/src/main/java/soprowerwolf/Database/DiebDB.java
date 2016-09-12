package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 12.09.2016.
 */
public class DiebDB extends AsyncTask<String, String, String> {

    JSONParser jsonParser = new JSONParser();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_change_role = "http://192.168.0.13/SoPro/db_test/changeRole.php";

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        //ToDo: playerID und gameID können über die globalen Variablen abgefragt werden
        int gameID = globalVariables.getGameID();
        int playerID = globalVariables.getOwnPlayerID();

        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(gameID)));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(playerID)));
        paramsList.add(new BasicNameValuePair("newRole", params[0]));

        jsonParser.makeHttpRequest(url_change_role, "POST", paramsList);
        //ToDO: check for success
        return null;
    }
}
