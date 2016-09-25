package soprowerwolf.Database;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

        //"Jaeger" can be called at the beginning or at the end of "Tag" -> two possible next Phases
        // => so next Phase depends on previous Phase:
        //      - previous Phase = Tag -> next Phase = "Werwolf"
        //      - previous Phase != Tag -> next Phase = "Tag"
        if (!params[0].equals("")) //"Jaeger" has been killed
        {
            if (params[0] == "Tag") {
                paramsList.add(new BasicNameValuePair("nextPhase", "Werwolf"));
            } else
                paramsList.add(new BasicNameValuePair("nextPhase", "Tag"));
        }
        jsonParser.makeHttpRequest(url_set_next_phase, "POST", paramsList);



        if (globalVariables.getWinner() != null){
            paramsList.clear();
            paramsList.add(new BasicNameValuePair("gameID", gameID));
            paramsList.add(new BasicNameValuePair("currentPhase", currentPhase));
            paramsList.add(new BasicNameValuePair("nextPhase", "Spielende"));
        }
        jsonParser.makeHttpRequest(url_set_next_phase, "POST", paramsList);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // if next Phase is set -> get current Phase
        new getCurrentPhase().execute();
    }
}
