package soprowerwolf.Database;

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
 * Created by Gina on 24.10.2016.
 */

public class NextPhaseDB {

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser json = new JSONParser();

    public NextPhaseDB(){}

    public void setNextPhase(){

        List<NameValuePair> par = new ArrayList<NameValuePair>();
        par.add(new BasicNameValuePair("currentPhase", globalVariables.getCurrentPhase()));
        par.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject JSONnextPhase = json.makeHttpRequest("http://www-e.uni-magdeburg.de/jkloss/getNextPhase.php", "GET", par);
        try {
            JSONArray phase = JSONnextPhase.getJSONArray("phase");
            globalVariables.setNextPhase(phase.getJSONObject(0).getString("nextPhase"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
