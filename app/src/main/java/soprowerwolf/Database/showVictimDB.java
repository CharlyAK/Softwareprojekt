package soprowerwolf.Database;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

    public showVictimDB(){}

    public String[] getVictims(){
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        int[] victimsID = new int[4];

        JSONObject V = jsonParser.makeHttpRequest(url_get_game_details, "GET", paramsList);
        try {
            victimsID[0] = V.getInt("victimDor");
            victimsID[1] = V.getInt("victimWer");
            victimsID[2] = V.getInt("victimHex");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[0])));
        JSONObject V1 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            if(V1.getInt("lover") != 0){ victimsID[3] = V1.getInt("lover");}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[1])));
        JSONObject V2 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            if(V2.getInt("lover") != 0){ victimsID[3] = V1.getInt("lover");}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[2])));
        JSONObject V3 = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            if(V3.getInt("lover") != 0){ victimsID[3] = V1.getInt("lover");}
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] victims = new String[3];

        for(int i = 0; i < victimsID.length; i++) {
            int j = 0;
            if(victimsID[i] != 0) {
                paramsList.clear();
                paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[i])));
                JSONObject J = jsonParser.makeHttpRequest(url_get_player_details, "GET", paramsList);
                try {
                    victims[j] = J.getString("name");
                    j++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return victims;
    }
}
