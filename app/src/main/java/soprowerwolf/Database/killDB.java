package soprowerwolf.Database;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Gina on 21.09.2016.
 */

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class killDB extends AsyncTask<String, String, String> {

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();
    GameOverDB gameOverDB = new GameOverDB();

    private static final String url_changeAlive = "http://192.168.0.13/SoPro/db_test/changeAlive.php";//"http://www-e.uni-magdeburg.de/jkloss/changeAlive.php";
    private static final String url_setVictims = "http://192.168.0.13/SoPro/db_test/setVictims.php";//"http://www-e.uni-magdeburg.de/jkloss/setVictims.php";

    @Override
    protected String doInBackground(String... params) {

        //alive ändern
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        jsonParser.makeHttpRequest(url_changeAlive, "POST", paramsList);

        //victims NULL setzten
        paramsList.add(new BasicNameValuePair("victimWer", "0"));
        jsonParser.makeHttpRequest(url_setVictims, "POST", paramsList);

        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("victimDor", "0"));
        jsonParser.makeHttpRequest(url_setVictims, "POST", paramsList);

        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("victimHex", "0"));
        jsonParser.makeHttpRequest(url_setVictims, "POST", paramsList);

        return null;
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
    }

}
