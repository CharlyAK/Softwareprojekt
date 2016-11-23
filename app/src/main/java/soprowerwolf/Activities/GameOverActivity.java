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
import soprowerwolf.Classes.popup;
import soprowerwolf.R;

public class GameOverActivity extends AppCompatActivity {

    private static final String url_exitGame = "http://www-e.uni-magdeburg.de/jkloss/exitGame.php";
    private JSONParser jsonParser = new JSONParser();

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        globalVariables.setCurrentContext(this);

        if((globalVariables.getOwnRole().equals("Werwolf") && globalVariables.getWinner().equals("Werwölfe")) ||
                (!globalVariables.getOwnRole().equals("Werwolf") && globalVariables.getWinner().equals("Dorfbewohner"))){
            popup.PopUpInfo("Die " + globalVariables.getWinner()+ " haben gewonnen! :)", "Glückwunsch!").show();
        }
        else {
            popup.PopUpInfo("Die " + globalVariables.getWinner() + " haben euch leider ausgerottet.", "Dumm gelaufen!").show();
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
