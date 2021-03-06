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
 * Created by Gina on 21.09.2016.
 */

public class GameOverDB {

    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private JSONParser jsonParser = new JSONParser();
    databaseCon Con = new databaseCon();

    private static final String url_get_all_player = "http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";

    public GameOverDB() {

    }

    public void gameOver() {

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject allplayers = jsonParser.makeHttpRequest(url_get_all_player, "GET", paramsList);

        try {

            if (allplayers.getInt("success") == 1) {

                JSONArray player = allplayers.getJSONArray("players");

                int e = 0;
                int g = 0;
                for (int i = 0; i < globalVariables.getNumPlayers(); i++) {

                    if (player.getJSONObject(i).getInt("alive") == 1 && player.getJSONObject(i).getString("role").equals("Werwolf")) {
                        e++;
                    } else if (player.getJSONObject(i).getInt("alive") == 1 && !player.getJSONObject(i).getString("role").equals("Werwolf")) {
                        g++;
                    }
                }

                if (e == 0) { globalVariables.setWinner("Dorfbewohner"); }
                if (g == 0) { globalVariables.setWinner("Werwölfe"); }

                if (e+g == 2){
                    String lover = "niemanden";
                    for(int i = 0; i < player.length(); i++){
                        if(player.getJSONObject(i).getInt("alive") == 1){
                            lover = Con.getLover(player.getJSONObject(i).getInt("playerID"));
                        }
                    }
                    if(!lover.equals("niemanden")){ globalVariables.setWinner("Liebenden"); }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
