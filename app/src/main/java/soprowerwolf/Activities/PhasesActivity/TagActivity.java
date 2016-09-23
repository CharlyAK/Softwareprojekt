package soprowerwolf.Activities.PhasesActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.games.Game;

import org.json.JSONException;

import java.util.Random;
import java.util.TimerTask;

import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;

public class TagActivity extends AppCompatActivity {

    GameActivity create = new GameActivity();
    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup(this);
    int[] playerIDsAndVotes = new int[40];
    Activity context = globalVariables.getCurrentContext();
    Activity context1;

    //this checks the database every second
    boolean started;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                playerIDsAndVotes = Con.Tag("update");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            create.updateVoteButtons(playerIDsAndVotes);
            handler.postDelayed(this, 2000);

        }
    };


    public void stop() {
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 2000);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        create.createObjects();

        context1 = create;

        popup.PopUpInfo("Wähle nun eine Person, die du hängen möchtest", "Tägliche Abstimmung").show();

        start();

    }

    protected void onResume(){
        super.onResume();
        start();
    }

    @Override
    protected void onStop(){
        super.onStop();
        //stop();
    }

    @Override
    public void onBackPressed() {

    }
}
