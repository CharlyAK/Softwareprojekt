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
import soprowerwolf.Classes.databaseCon;

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

        /** victims[0] = victimDor; victim[1] = [victimDor = good] or [victimDor = bad]
          * victims[2] = victimWer; victim[3] = [victimWer = good] or [victimWer = bad]
          * victims[4] = victimHex; victim[5] = [victimHex = good] or [victimHex = bad]
          * victims[6] = Lover; victim[7] = [Lover = good] or [Lover = bad]
          * victims[8] = victimJaeg; victim[9] = [victimJaeg = good] or [victimJaeg = bad]
          * victims[10] = LoverVictimJaeg; victim[11] = [LoverVictimJaeg = good] or [LoverVictimJaeg = bad]
          * victim[12] = Lover of victim[6] --> easier for showing (see below)
          * victim[13] = number of victims --> for a better Text
          */

    public String[] getVictims() {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        int[] victimsID = new int[7];

        JSONObject V = jsonParser.makeHttpRequest(url_get_game_details, "GET", paramsList);
        try {
            JSONArray JV = V.getJSONArray("game");
            victimsID[0] = JV.getJSONObject(0).getInt("victimDor");
            victimsID[1] = JV.getJSONObject(0).getInt("victimWer");
            victimsID[2] = JV.getJSONObject(0).getInt("victimHex");
            victimsID[4] = JV.getJSONObject(0).getInt("victimJaeger");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimDor has a lover and/ or is "Jäger"
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[0])));
        JSONObject loverVicDor = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JLoverVicDor = loverVicDor.getJSONArray("player_game");
            if (JLoverVicDor.getJSONObject(0).getInt("lover") != 0) {
                victimsID[3] = JLoverVicDor.getJSONObject(0).getInt("lover");
                victimsID[6] = victimsID[0];
            }

            if (JLoverVicDor.getJSONObject(0).getString("role").equals("Jaeger")) {
                globalVariables.setJaegerDies(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimWer has a lover and/ or is "Jäger"
        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[1])));
        JSONObject loverVicWer = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JLoverVicWer = loverVicWer.getJSONArray("player_game");
            if (JLoverVicWer.getJSONObject(0).getInt("lover") != 0) {
                victimsID[3] = JLoverVicWer.getJSONObject(0).getInt("lover");
                victimsID[6] = victimsID[1];
            }

            if (JLoverVicWer.getJSONObject(0).getString("role").equals("Jaeger")) {
                globalVariables.setJaegerDies(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimHex has a lover and/ or is "Jäger"
        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[2])));
        JSONObject loverVicHex = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JLoverVicHex = loverVicHex.getJSONArray("player_game");
            if (JLoverVicHex.getJSONObject(0).getInt("lover") != 0) {
                victimsID[3] = JLoverVicHex.getJSONObject(0).getInt("lover");
                victimsID[6] = victimsID[2];
            }

            if (JLoverVicHex.getJSONObject(0).getString("role").equals("Jaeger")) {
                globalVariables.setJaegerDies(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // check if victimJaeger has a lover
        paramsList.clear();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[4])));
        JSONObject loverVicJaeg = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
        try {
            JSONArray JLoverVicJaeg = loverVicJaeg.getJSONArray("player_game");
            if (JLoverVicJaeg.getJSONObject(0).getInt("lover") != 0) {
                victimsID[5] = JLoverVicJaeg.getJSONObject(0).getInt("lover");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //check, if lover was "Jäger"
        if (victimsID[3] != 0) {
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
            paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[4])));
            JSONObject checkLoverJaeg = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", paramsList);
            try {
                JSONArray JCheckLoverJaeg = checkLoverJaeg.getJSONArray("player_game");
                if (JCheckLoverJaeg.getJSONObject(0).getString("role").equals("Jaeger")) {
                    globalVariables.setJaegerDies(true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] victims = new String[14];
        databaseCon Con = new databaseCon();
        int j = 0;
        int counter = 0;
        for (int i = 0; i < victimsID.length; i++) {
            if (victimsID[i] != 0) {
                if (i < 4) {
                    counter++;
                }

                paramsList.clear();
                paramsList.add(new BasicNameValuePair("playerID", String.valueOf(victimsID[i])));
                JSONObject victimName = jsonParser.makeHttpRequest(url_get_player_details, "GET", paramsList);
                try {
                    JSONArray JVictimName = victimName.getJSONArray("player");
                    victims[j] = JVictimName.getJSONObject(0).getString("name");
                    victims[j + 1] = Con.Seherin(JVictimName.getJSONObject(0).getInt("playerID"));
                    j = j + 2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                victims[j] = "0";
                victims[j + 1] = "0";
                j = j + 2;
            }
        }
        victims[13] = String.valueOf(counter);

        return victims;
    }
}
