package soprowerwolf.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    databaseCon Con = new databaseCon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Philosopher-Regular.ttf");
        ((Button) findViewById(R.id.startGame)).setTypeface(font);
        ((Button) findViewById(R.id.joinGame)).setTypeface(font);
        ((Button) findViewById(R.id.settings)).setTypeface(font);
        ((TextView) findViewById(R.id.textViewPlayerName)).setTypeface(font);

        TextView playerName = (TextView) findViewById(R.id.textViewPlayerName);
        playerName.setText(playerName.getText() + " " + Con.getName());
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
        globalVariables.setOwnPlayerID(0);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(globalVariables.getSharedPrefContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("PlayerID");
        editor.apply();

        Intent intent = new Intent(this, LoginRegistrationActivity.class);
        startActivity(intent);
    }

    public void reset (View view){
        JSONParser jsonParser = new JSONParser();

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("nothing", "nothing"));
        jsonParser.makeHttpRequest(url_reset, "POST", params);

    }
}
