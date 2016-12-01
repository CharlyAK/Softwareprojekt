package soprowerwolf.Database;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class killDB extends AsyncTask<String, String, String> {

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();
    GameOverDB gameOverDB = new GameOverDB();

    private static final String url_changeAlive = "http://www-e.uni-magdeburg.de/jkloss/changeAlive.php";


    @Override
    protected String doInBackground(String... params) {

        //alive ändern
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        jsonParser.makeHttpRequest(url_changeAlive, "POST", paramsList);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        gameOverDB.gameOver();
    }
}
