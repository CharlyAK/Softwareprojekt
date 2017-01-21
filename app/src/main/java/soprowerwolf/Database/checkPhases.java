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
 * Created by Alex on 21.01.2017.
 */

//checks if Phase has been changed
public class checkPhases {
    public checkPhases()
    {

    }
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();

    private static final String url_get_current_phase = "http://www-e.uni-magdeburg.de/jkloss/getCurrentPhase.php";
    private String currentPhase;

    public boolean check()
    {
        boolean newPhase;
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        // get current Phase
        JSONObject JCP = jsonParser.makeHttpRequest(url_get_current_phase, "GET", paramsList);

        try {
            JSONArray ACP = JCP.getJSONArray("currentPhase");
            currentPhase = ACP.getJSONObject(0).getString("currentPhase");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!globalVariables.getCurrentPhase().equals(currentPhase))
            newPhase = true;
        else
            newPhase = false;

        return newPhase;
    }
}
