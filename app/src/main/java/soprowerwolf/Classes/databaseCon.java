package soprowerwolf.Classes;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
    private static final String url_save_image = "http://www-e.uni-magdeburg.de/jkloss/save_image.php";
    private static final String url_get_all_player = "http://www-e.uni-magdeburg.de/jkloss/get_all_player.php";
    private static final String url_get_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_game_details.php";
    private static final String url_get_player_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_details.php";
    private static final String url_get_player_game_details = "http://www-e.uni-magdeburg.de/jkloss/get_player_game_details.php";
    private static final String url_update_hexe = "http://www-e.uni-magdeburg.de/jkloss/updateHexe.php";
    private static final String url_set_victims = "http://www-e.uni-magdeburg.de/jkloss/setVictims.php";
    private static final String url_vote_update = "http://www-e.uni-magdeburg.de/jkloss/vote_update.php";
    private static final String url_submit_choice = "http://www-e.uni-magdeburg.de/jkloss/submit_choice.php";
    private static final String url_getNumOfWerAlive = "http://www-e.uni-magdeburg.de/jkloss/getNumOfWerAlive.php";
    private static final String url_update_player_game = "http://www-e.uni-magdeburg.de/jkloss/update_player_game.php";


    /**
     * creats a new player, getting playerID
     *
     * @param name  -> displayed name during the game
     * @param email -> needed for next login
     * @param pw    -> needed for next login
     * @return true (if the registration was successful) or false (if the player already exists)
     */
    public boolean registration(String name, String email, String pw) {
        //ToDo: HashWerte für passwörter
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

    /**
     * getting Playerinformation from database to login -> compare data
     *
     * @param email
     * @param pw    ==> combination needed for login
     * @return true(if login successful) or false (if login failed)
     */
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

    /**
     * deletes the entry of a player
     *
     * @return true(if player successfully deleted) or false (if delete failed)
     */
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

    /**
     * Takes a bitmap which is encoded and saved in the database
     *
     * @param bitmap to be saved
     */
    public void setImage(Bitmap bitmap) {
        //use following method to convert bitmap to byte array:
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        //to encode base64 from byte array use following method

        String encoded = Base64.encodeToString(byteArray, 0);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));
        params.add(new BasicNameValuePair("image", encoded));

        jsonParser.makeHttpRequest(url_save_image, "POST", params);
    }

    /**
     * Gets the blob object for a specific player from the database.
     * This is converted to a bitmap.
     *
     * @return returns the created bitmap
     * @throws JSONException
     */

    public Bitmap getImage() throws JSONException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_save_image, "GET", params);

        JSONObject jsonObject1 = jsonObject.getJSONObject("image");
        String base64String = jsonObject1.getString("0");

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmapResult = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmapResult;
    }

    /**
     * This function gets the image of a player as a string.
     * It will be stored in the global variables
     *
     * @param playerID of the player to get the image from
     * @return returns the String which contains the image information
     * @throws JSONException
     */
    public String getImagesAsString(int playerID) throws JSONException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("playerID", String.valueOf(playerID)));

        JSONObject jsonObject = jsonParser.makeHttpRequest(url_save_image, "GET", params);

        JSONObject jsonObject1 = jsonObject.getJSONObject("image");
        return jsonObject1.getString("0");
    }

    /**
     * getting number auf players, who are ready to play
     *
     * @return number of players, where "ready" is true (database)
     */
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

    public int getPlayerInGame() {
        int numPlayersIn = 0;
        boolean dieb = globalVariables.getDiebChoosen();

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject JOAllPlayers = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);
        try {
            if (JOAllPlayers.getInt("success") == 1) {
                JSONArray JAAllPlayer = JOAllPlayers.getJSONArray("players");

                if (dieb) {
                    for (int i = 0; i < JAAllPlayer.length() - 2; i++) {
                        if (JAAllPlayer.getJSONObject(i).getInt("playerID") != 0) {
                            numPlayersIn++;
                        }
                    }
                } else {
                    for (int i = 0; i < JAAllPlayer.length(); i++) {
                        if (JAAllPlayer.getJSONObject(i).getInt("playerID") != 0) {
                            numPlayersIn++;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return numPlayersIn;
    }

    /**
     * getting number of Game players
     *
     * @return number of players
     */
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

    /**
     * getting all playerIDs of Game players for playerIcons
     *
     * @return array of playerIDs (playerID = 0 if player is dead)
     */
    public void getPlayerIDs() {
        int[] playerIDs = new int[20];
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject ID = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);
        try {
            if (ID.getInt("success") == 1) {
                JSONArray JID = ID.getJSONArray("players");
                for (int i = 0; i < JID.length(); i++) {
                    playerIDs[i] = JID.getJSONObject(i).getInt("playerID");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        globalVariables.setPlayerIDs(playerIDs);
    }

    /**
     * getting all playernames of Game Players for playerIcons
     *
     * @return array of playerNames
     */
    public void getPlayerNames() {
        String[] playerNames = new String[20];
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        int[] playerIDs = globalVariables.getPlayerIDs();

        try {
            for (int i = 0; i < globalVariables.getNumPlayers(); i++) {
                params.clear();
                params.add(new BasicNameValuePair("playerID", String.valueOf(playerIDs[i])));

                JSONObject name = jsonParser.makeHttpRequest(url_get_player_details, "GET", params);

                JSONArray AName = name.getJSONArray("player");
                playerNames[i] = AName.getJSONObject(0).getString("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        globalVariables.setPlayerNames(playerNames);
    }

    public int[] getAlive() {
        int[] alive = new int[20];
        int numPlayersAlive = 0;

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));

        JSONObject ID = jsonParser.makeHttpRequest(url_get_all_player, "GET", params);
        try {
            if (ID.getInt("success") == 1) {
                JSONArray JID = ID.getJSONArray("players");
                for (int i = 0; i < JID.length(); i++) {
                    alive[i] = JID.getJSONObject(i).getInt("alive");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < globalVariables.getNumPlayers(); i++) {
            if (alive[i] == 1)
                numPlayersAlive++;
        }

        globalVariables.setNumPlayersAlive(numPlayersAlive);
        return alive;
    }

    /**
     * getting own name for displaying (settings/ menu)
     *
     * @return String name
     */
    public String getName() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));

        try {
            JSONObject name = jsonParser.makeHttpRequest(url_get_player_details, "GET", params);

            JSONArray AName = name.getJSONArray("player");
            return AName.getJSONObject(0).getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * getting remaining roles from database
     *
     * @return array with two roles (choices)
     */
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

    /**
     * Sets the victim of the current phase
     *
     * @param victimID playerID of the victim
     */

    public void setVictims(int victimID) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        switch (globalVariables.getCurrentPhase()) {
            case "Werwolf":
                params.add(new BasicNameValuePair("victimWer", String.valueOf(victimID)));
                break;

            case "Tag":
                params.add(new BasicNameValuePair("victimDor", String.valueOf(victimID)));
                break;
        }
        JSONObject jsonObjectChoice = jsonParser.makeHttpRequest(url_set_victims, "POST", params);
        //ToDo: check for success
    }

    /**
     * Called during the Werwolf-phase
     *
     * @param action: decides if you want to get an update on how many werewolves have voted
     *                or if you want to submit your own choice
     *                or if you want to get the number of werewolves alive
     * @return returns the playerIds and the votes
     * in the following pattern: {id1, votes1, id2, votes2, ...}
     * or the number of werewolves alive
     * @throws JSONException
     */

    public int[] Werwolf(String action) throws JSONException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        switch (action) {
            case "update":
                int[] playerIDs = globalVariables.getPlayerIDs();

                params.add(new BasicNameValuePair("phase", "night"));
                for (int i = 0; i < playerIDs.length; i++) {
                    params.add(new BasicNameValuePair("id" + i, "" + playerIDs[i]));
                }
                JSONObject jsonObjectVotes = jsonParser.makeHttpRequest(url_vote_update, "GET", params);
                //get the votes from VotingNight
                JSONArray jVotes = jsonObjectVotes.getJSONArray("votes");

                //merging playerIDs with numOfVotes
                int[] playerIDsAndVotes = new int[40];
                //there is the playerID first and afterwards the numOfVotes
                for (int i = 0, j = 0; i < playerIDs.length && i < jVotes.length(); i++, j++) {
                    playerIDsAndVotes[j] = playerIDs[i];
                    playerIDsAndVotes[++j] = jVotes.getJSONObject(i).getInt("votes");
                }

                return playerIDsAndVotes;


            case "submitChoice":
                String playerID = String.valueOf(globalVariables.getCurrentlySelectedPlayer().getId());

                params.add(new BasicNameValuePair("playerID", playerID));
                JSONObject jsonObjectChoice = jsonParser.makeHttpRequest(url_submit_choice, "POST", params);
                //ToDo: check for success
                break;

            case "getNumOfWerAlive":
                params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
                JSONObject jsonObjectNumber = jsonParser.makeHttpRequest(url_getNumOfWerAlive, "GET", params);

                int[] numOfWerAlive = new int[1];
                numOfWerAlive[0] = jsonObjectNumber.getInt("numOfWerAlive");

                return numOfWerAlive;
        }

        return null;
    }


    /**
     * getting role of "victim" from database
     *
     * @return if "ictim" is good or bad
     */
    public String Seherin(int playerID) {
        String GoodOrBad = null;

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

    /**
     * @param magic: calls different functions (getVictimWer, ableToSave, ableToPoison, saveVictim, kill)
     * @return
     */

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

                    JSONObject jsonObjectPlayer = jsonParser.makeHttpRequest(url_get_player_details,
                            "GET", playerName);

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

    /**
     * Called during the day
     *
     * @param action: choose if you want to get an update on how many players have voted
     *                or if you want to submit your own choice to the database
     * @return returns the playerIds and the votes
     * in the following pattern: {id1, votes1, id2, votes2, ...}
     * @throws JSONException
     */

    public int[] Tag(String action) throws JSONException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        switch (action) {
            case "update":
                int[] playerIDs = globalVariables.getPlayerIDs();

                for (int i = 0; i < playerIDs.length; i++) {
                    params.add(new BasicNameValuePair("id" + i, "" + playerIDs[i]));
                }
                JSONObject jsonObjectVotes = jsonParser.makeHttpRequest(url_vote_update, "GET", params);
                //get the votes from VotingDay
                JSONArray jVotes = jsonObjectVotes.getJSONArray("votes");

                //merging playerIDs with numOfVotes
                int[] playerIDsAndVotes = new int[40];
                //there is the playerID first and afterwards the numOfVotes
                for (int i = 0, j = 0; i < playerIDs.length && i < jVotes.length(); i++, j++) {
                    playerIDsAndVotes[j] = playerIDs[i];
                    if (!((jVotes.getJSONObject(i).getString("votes")).equals("null"))) ;
                    playerIDsAndVotes[++j] = jVotes.getJSONObject(i).getInt("votes");
                }

                return playerIDsAndVotes;


            case "submitChoice":
                String playerID = String.valueOf(globalVariables.getCurrentlySelectedPlayer().getId());
                params.add(new BasicNameValuePair("playerID", playerID));
                JSONObject jsonObjectChoice = jsonParser.makeHttpRequest(url_submit_choice, "POST", params);
                //ToDo: check for success
                break;

        }
        return null;
    }

    /*
            DEBUG
     */
    public void debugStartGame() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        JSONObject jsonObject = jsonParser.makeHttpRequest(url_update_player_game, "POST", params);
    }

    public String getLover() {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));
        params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
        JSONObject JSONLover = jsonParser.makeHttpRequest(url_get_player_game_details, "GET", params);

        try {

            JSONArray player = JSONLover.getJSONArray("player_game");
            int lover = player.getJSONObject(0).getInt("lover");

            if (lover != 0) {
                params.clear();
                params.add(new BasicNameValuePair("playerID", String.valueOf(lover)));
                JSONObject JSON = jsonParser.makeHttpRequest(url_get_player_details, "GET", params);
                try {
                    JSONArray JSONarray = JSON.getJSONArray("player");
                    return JSONarray.getJSONObject(0).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "niemanden";
    }

}
