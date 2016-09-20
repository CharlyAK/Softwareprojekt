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

    private JSONParser jsonParser = new JSONParser();
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_change_role = "http://192.168.0.13/SoPro/db_test/changeRole.php";//"http://www-e.uni-magdeburg.de/jkloss/changeRole.php";

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        int gameID = globalVariables.getGameID();
        int playerID = globalVariables.getOwnPlayerID();

        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(gameID)));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(playerID)));
        paramsList.add(new BasicNameValuePair("newRole", params[0]));
        paramsList.add(new BasicNameValuePair("notChoosen", params[1]));
        paramsList.add(new BasicNameValuePair("nothingChoosen", params[2]));

        jsonParser.makeHttpRequest(url_change_role, "POST", paramsList);
        //ToDO: check for success
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        new setNextPhase().execute("");
    }
}
