package soprowerwolf.Classes;

import android.graphics.Matrix;
import android.os.StrictMode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 04.09.2016.
 */
public class databaseCon {
    private JSONParser jsonParser = new JSONParser();

    public databaseCon() {
        // sonst funktionieren GET und POST nicht (bei mir zumindest)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private GlobalVariables globalVariables = GlobalVariables.getInstance();

    private static final String url_create_new_player = "http://www-e.uni-magdeburg.de/jkloss/create_new_player.php";
    private static final String url_login = "http://www-e.uni-magdeburg.de/jkloss/login.php";
    private static final String url_delete_Account = "http://www-e.uni-magdeburg.de/jkloss/deleteAccount.php";
    private static final String url_get_all_player = "http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";
    private static final String url_get_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_game_details.php";
    private static final String url_get_player_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_details.php";
    private static final String url_get_player_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_game_details.php";
    private static final String url_update_hexe = "http://www-e.uni-magdeburg.de/jkloss/updateHexe.php";
    private static final String url_set_victims = "";
    private static final String url_change_alive = "";
    private static final String url_vote_update = "http://www-e.uni-magdeburg.de/jkloss/vote_update.php";

    public boolean registration(String name, String email, String pw, Matrix image) {
        //ToDo: HashWerte für passwörter
        //ToDo: Bild in DB laden
        List<NameValuePair> paramsCheck = new ArrayList<NameValuePair>();

        paramsCheck.add(new BasicNameValuePair("email", email));

        JSONObject registration = jsonParser.makeHttpRequest(url_create_new_player, "GET", paramsCheck);

        try {
            if (registration.getInt("success") == 1) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("password", pw));

                jsonParser.makeHttpRequest(url_create_new_player, "POST", params);

                List<NameValuePair> paramsID = new ArrayList<NameValuePair>();
                paramsID.add(new BasicNameValuePair("email", email));
                paramsID.add(new BasicNameValuePair("password", pw));
                JSONObject newPlayer = jsonParser.makeHttpRequest(url_login, "GET", paramsID);

                JSONArray JID = newPlayer.getJSONArray("playerID");
                globalVariables.setOwnPlayerID(JID.getJSONObject(JID.length() - 1).getInt("playerID"));

                return true;
            } else return false;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean login(String email, String pw) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pw));

        JSONObject login = jsonParser.makeHttpRequest(url_login, "GET", params);
        try {
            if (login.getInt("success") == 1) {

                JSONArray JID = login.getJSONArray("playerID");
                globalVariables.setOwnPlayerID(JID.getJSONObject(JID.length() - 1).getInt("playerID"));

                return true;
            } else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteAccount() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));

        jsonParser.makeHttpRequest(url_delete_Account, "POST", params);

        JSONObject delete = jsonParser.makeHttpRequest(url_get_player_details, "GET", params);
        try {
            if (delete.getInt("success") == 0) {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getReady() {
        int ready = 0;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject JOready = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);
        try {
            if (JOready.getInt("success") == 1) {
                JSONArray JAReady = JOready.getJSONArray("players");
                for (int i = 0; i < JAReady.length(); i++) {
                    if (JAReady.getJSONObject(i).getInt("ready") == 1) {
                        ready++;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ready;
    }

    public int getNumPlayers() {
        int numPlayers = 0;
        boolean dieb = false;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject JONumPlayers = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);
        try {
            if (JONumPlayers.getInt("success") == 1) {
                JSONArray JANumPlayer = JONumPlayers.getJSONArray("players");
                for (int i = 0; i < JANumPlayer.length(); i++) {
                    if (JANumPlayer.getJSONObject(i).getString("role").equals("Dieb")) {
                        dieb = true;
                    }
                }

                if (dieb) {
                    numPlayers = JANumPlayer.length() - 2;
                } else
                    numPlayers = JANumPlayer.length();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return numPlayers;
    }

    public int[] getPlayerIDs() {
        int[] playerIDs = new int[20];
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject ID = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);
        try {
            if (ID.getInt("success") == 1) {
                JSONArray JID = ID.getJSONArray("players");
                for (int i = 0; i < JID.length(); i++) {
                    if (JID.getJSONObject(i).getInt("alive") == 0) //there are no playerID 0 -> if the playerID is 0, the player is dead
                    {
                        playerIDs[i] = 0;
                    } else
                        playerIDs[i] = JID.getJSONObject(i).getInt("playerID");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return playerIDs;
    }

    public String[] DiebGetRoles() {
        String[] roles = new String[2];
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject allPlayers = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);

        try {
            if (allPlayers.getInt("success") == 1) {
                JSONArray JRoles = allPlayers.getJSONArray("players");

                // last two Roles are the selectable Roles of the "Dieb"
                roles[0] = JRoles.getJSONObject(JRoles.length() - 1).getString("role");
                roles[1] = JRoles.getJSONObject(JRoles.length() - 2).getString("role");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return roles;
    }


    public void Werwolf() {

    }

    public String Seherin() {
        String GoodOrBad = null;

        int playerID = globalVariables.getCurrentlySelectedPlayer().getId();
        int gameID = globalVariables.getGameID();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("playerID", String.valueOf(playerID)));
        params.add(new BasicNameValuePair("gameID", String.valueOf(gameID)));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", params);

        try {
            JSONArray JRole = jsonObject.getJSONArray("player_game");
            String SRole = JRole.getJSONObject(JRole.length() - 1).getString("role");

            if (SRole.equals("Werwolf")) {
                GoodOrBad = "böse";
            } else GoodOrBad = "gut";

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GoodOrBad;
    }

    public String Hexe(String magic) {
        int gameID = globalVariables.getGameID();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", String.valueOf(gameID)));

        JSONObject jsonObjectGame = jsonParser.makeHttpRequest(url_get_game_details, "GET", params);

        switch (magic) {
            case "getVictimWer":
                try {
                    JSONArray JVictimID = jsonObjectGame.getJSONArray("game");
                    int victimID = JVictimID.getJSONObject(JVictimID.length() - 1).getInt("victimWer");

                    List<NameValuePair> playerName = new ArrayList<NameValuePair>();
                    playerName.add(new BasicNameValuePair("playerID", String.valueOf(victimID)));

                    JSONObject jsonObjectPlayer = jsonParser.makeHttpRequest(url_get_player_details, "GET", playerName);

                    JSONArray JVictimName = jsonObjectPlayer.getJSONArray("player");
                    return JVictimName.getJSONObject(JVictimName.length() - 1).getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "ableToSave":
                try {
                    JSONArray JHeal = jsonObjectGame.getJSONArray("game");
                    String able = JHeal.getJSONObject(JHeal.length() - 1).getString("heal");
                    return able;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "ableToPoison":
                try {
                    JSONArray JPoison = jsonObjectGame.getJSONArray("game");
                    return JPoison.getJSONObject(JPoison.length() - 1).getString("poison");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "saveVictim":
                params.add(new BasicNameValuePair("heal", "used"));
                JSONObject jsonObjectSave = jsonParser.makeHttpRequest(url_update_hexe, "POST", params);
                //ToDo: check for success
                break;

            case "kill":
                String playerID = String.valueOf(globalVariables.getCurrentlySelectedPlayer().getId());

                params.add(new BasicNameValuePair("poison", "used"));
                params.add(new BasicNameValuePair("victimHex", playerID));
                JSONObject jsonObjectKill = jsonParser.makeHttpRequest(url_update_hexe, "POST", params);
                //ToDo: check for success
                break;
        }
        return null;
    }

    public int[] Tag(String action) throws JSONException {

        int gameID = globalVariables.getGameID();

        switch (action){
            case "update":
                int[] playerIDs = getPlayerIDs();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (int i = 0; i < playerIDs.length; i++) {
                    params.add(new BasicNameValuePair("id" + i, ""+playerIDs[i]));
                }
                JSONObject jsonObjectVotes = jsonParser.makeHttpRequest(url_vote_update, "GET", params);
                //get the votes from VotingDay
                JSONArray jVotes = jsonObjectVotes.getJSONArray("votes");

                //merging playerIDs with numOfVotes
                int[] playerIDsAndVotes = new int[40];
                //there is the playerID first and afterwards the numOfVotes
                for (int i = 0, j = 0; i < playerIDs.length && i < jVotes.length(); i++, j++){
                    playerIDsAndVotes[j] = playerIDs[i];
                    playerIDsAndVotes[++j] = jVotes.getJSONObject(i).getInt("votes");
                }

                return playerIDsAndVotes;


            case "submitChoice":


        }
        return null;
    }
}
