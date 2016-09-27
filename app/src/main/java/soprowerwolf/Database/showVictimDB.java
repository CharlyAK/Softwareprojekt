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
 * Created by Gina on 25.09.2016.
 */

public class showVictimDB {

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    private JSONParser jsonParser = new JSONParser();
    private static final String url_get_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_game_details.php";
    private static final String url_get_player_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_game_details.php";
    private static final String url_get_player_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_details.php";

    public showVictimDB() {
    }

    public String[] getVictims() {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        int[] victimsID = new int[5];

        JSONObject V = jsonParser.makeHttpRequest(url_get_game_details, "GET", paramsList);
        try {
            JSONArray JV = V.getJSONArray("game");
            victimsID[0] = JV.getJSONObject(0).getInt("victimDor");
            victimsID[1] = JV.getJSONObject(0).getInt("victimWer");
            victimsID[2] = JV.getJSONObject(0).getInt("victimHex");
            victimsID[3] = JV.getJSONObject(0).getInt("victimJaeger");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimDor has a lover and/ or is "Jäger"
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[0])));
        JSONObject V1 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JV1 = V1.getJSONArray("player_game");
            if (JV1.getJSONObject(0).getInt("lover") != 0) {
                victimsID[4] = JV1.getJSONObject(0).getInt("lover");
            }

            if (JV1.getJSONObject(0).getString("role").equals("Jaeger")) {
                globalVariables.setJaegerDies(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimWer has a lover and/ or is "Jäger"
        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[1])));
        JSONObject V2 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JV2 = V2.getJSONArray("player_game");
            if (JV2.getJSONObject(0).getInt("lover") != 0) {
                victimsID[4] = JV2.getJSONObject(0).getInt("lover");
            }

            if (JV2.getJSONObject(0).getString("role").equals("Jaeger")) {
                globalVariables.setJaegerDies(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimHex has a lover and/ or is "Jäger"
        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[2])));
        JSONObject V3 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JV3 = V3.getJSONArray("player_game");
            if (JV3.getJSONObject(0).getInt("lover") != 0) {
                victimsID[4] = JV3.getJSONObject(0).getInt("lover");
            }

            if (JV3.getJSONObject(0).getString("role").equals("Jaeger")) {
                globalVariables.setJaegerDies(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimJaeger has a lover
        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[3])));
        JSONObject V4 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JV4 = V4.getJSONArray("player_game");
            if (JV4.getJSONObject(0).getInt("lover") != 0) {
                victimsID[4] = JV4.getJSONObject(0).getInt("lover");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (victimsID[4] != 0) {
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
            paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[4])));
            JSONObject V5 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
            try {
                JSONArray JV5 = V5.getJSONArray("player_game");
                if (JV5.getJSONObject(0).getString("role").equals("Jaeger")) {
                    globalVariables.setJaegerDies(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] victims = new String[5];
        int j = 0;
        for (int i = 0; i < victimsID.length; i++) {
            if (victimsID[i] != 0) {
                paramsList.clear();
                paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[i])));
                JSONObject J = jsonParser.makeHttpRequest(url_get_player_details, "GET", paramsList);
                try {
                    JSONArray JVictimName = J.getJSONArray("player");
                    victims[j] = JVictimName.getJSONObject(0).getString("name");
                    j++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                victims[j] = "0";
                j++;
            }
        }

        return victims;
    }
}
