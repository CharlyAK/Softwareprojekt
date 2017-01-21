package soprowerwolf.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.R;

public class GameOverActivity extends AppCompatActivity {

    private static final String url_exitGame = "http://www-e.uni-magdeburg.de/jkloss/exitGame.php";
    private JSONParser jsonParser = new JSONParser();
    databaseCon con = new databaseCon();

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup();

    GameActivity game = new GameActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game.finish();
        setContentView(R.layout.activity_game_over);
        globalVariables.setCurrentContext(this);

        String role = globalVariables.getOwnRole();
        String winner = globalVariables.getWinner();
        String lover = con.getLover(globalVariables.getOwnPlayerID());

        switch (winner){

            case "Werwölfe":
                if(role.equals("Werwolf")){
                    popup.PopUpInfo("Die " + winner + " haben gewonnen.", "Glückwunsch! :)").show();
                }
                else {
                    popup.PopUpInfo("Die " + winner + " haben euch leider ausgerottet.", "Dumm gelaufen! :(").show();
                }
                break;

            case "Dorfbewohner":
                if(!role.equals("Werwolf")){
                    popup.PopUpInfo("Die " + winner + " haben gewonnen.", "Glückwunsch! :)").show();
                }
                else {
                    popup.PopUpInfo("Die " + winner + " haben euch leider ausgerottet.", "Dumm gelaufen! :(").show();
                }
                break;

            case "Liebenden":
                if(lover.equals("niemanden")){
                    popup.PopUpInfo("Die " + winner + " haben euch leider ausgerottet.", "Dumm gelaufen! :(").show();
                }
                else {
                    popup.PopUpInfo("Die Liebe zwischen dir und " + lover + " hat euch das Leben gerettet. Ihr habt gewonnen.", "Glückwunsch! <3").show();
                }
                break;
        }
    }

    public void backToMenu(View view){

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        if(globalVariables.isSpielleiter()){
            params.add(new BasicNameValuePair("gameID", String.valueOf(globalVariables.getGameID())));
            params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));
            jsonParser.makeHttpRequest(url_exitGame, "GET", params);
        }
        else{
            params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));
            jsonParser.makeHttpRequest(url_exitGame, "GET", params);
        }
        Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
        startActivity(intent);
    }

}
