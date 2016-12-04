package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Activities.PhasesActivity.DiebActivity;
import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;

/**
 * Created by Alex on 12.09.2016.
 */


/**
 * updates database -> changes Role of "Dieb" + deletes Phases of not choosen roles (if necessary)
 * params[0] = new role
 * params[1] = the role, which wasn't choosen
 * params[2] = the second role, which wasn't choosen (if the player wants to stay "Dorfbewohner" -> important to delete the right phases)
 */
public class DiebDB extends AsyncTask<String, String, String> {

    private JSONParser jsonParser = new JSONParser();
    private GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_change_role = "http://www-e.uni-magdeburg.de/jkloss/changeRole.php";
    NextPhaseDB nextPhaseDB = new NextPhaseDB();

    @Override
    protected String doInBackground(String... params) {
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        int gameID = globalVariables.getGameID();
        int playerID = globalVariables.getOwnPlayerID();

        paramsList.add(new BasicNameValuePair("gameID", String.valueOf(gameID)));
        paramsList.add(new BasicNameValuePair("playerID", String.valueOf(playerID)));
        paramsList.add(new BasicNameValuePair("newRole", params[0]));
        paramsList.add(new BasicNameValuePair("notChoosen", params[1]));
        paramsList.add(new BasicNameValuePair("nothingChoosen", params[2]));

        jsonParser.makeHttpRequest(url_change_role, "POST", paramsList);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        nextPhaseDB.setNextPhase();
        new setNextPhase().execute("audio");
    }
}
