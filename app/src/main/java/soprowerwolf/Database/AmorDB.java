package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 20.09.2016.
 */

/**
 * updates database -> sets lover
 *  params[0] = lover1
 *  params[1] = lover2
 */
public class AmorDB extends AsyncTask<String, String, String> {

    private JSONParser jsonParser = new JSONParser();
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_set_lovers = "http://www-e.uni-magdeburg.de/jkloss/setLovers.php";

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        int gameID = globalVariables.getGameID();

        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(gameID)));
        paramsList.add(new BasicNameValuePair("lover1", params[0]));
        paramsList.add(new BasicNameValuePair("lover2", params[1]));

        jsonParser.makeHttpRequest(url_set_lovers, "POST", paramsList);
        //ToDO: check for success
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
