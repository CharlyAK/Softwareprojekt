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
 * Created by Alex on 19.09.2016.
 */
public class setNextPhase extends AsyncTask<String, String, String> {

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();

    private static final String url_set_next_phase = "http://www-e.uni-magdeburg.de/jkloss/setNextPhase.php";

    @Override
    protected String doInBackground(String... params) {

        String gameID = String.valueOf(globalVariables.getGameID());
        String currentPhase = globalVariables.getCurrentPhase();

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", gameID));
        paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));


        if (globalVariables.getCurrentPhase().equals("Audio")){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", "Audio"));
            paramsList.add(new BasicNameValuePair("nextPhase", globalVariables.getNextPhase()));
        }

        if (params[0].equals("audio")){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Audio"));
        }

        if (globalVariables.getWinner() != null){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Spielende"));
        }

        if(globalVariables.getJaegerDies() && !params[0].equals("next"))
        {
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Jaeger"));
        }

        jsonParser.makeHttpRequest(url_set_next_phase, "POST", paramsList);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // if next Phase is set -> get current Phase
        new getCurrentPhase().execute("");
    }
}
