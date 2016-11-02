package soprowerwolf.Activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.R;

public class MenuActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    private static final String url_reset = "http://www-e.uni-magdeburg.de/jkloss/reset.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGameSetup(View view) {
        Intent intent = new Intent(this, GameSetupActivity.class);
        startActivity(intent);
    }

    public void startJoinGame(View view) {
        Intent intent = new Intent(this, JoinGameActivity.class);
        startActivity(intent);
    }

    public void startSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startRules(View view) {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, LoginRegistrationActivity.class);
        startActivity(intent);
        globalVariables.setOwnPlayerID(0);
    }

    public void reset (View view){
        JSONParser jsonParser = new JSONParser();

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("nothing", "nothing"));
        jsonParser.makeHttpRequest(url_reset, "POST", params);

    }
}
