package soprowerwolf.Activities.PhasesActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.json.JSONException;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;

public class WerwolfActivity extends AppCompatActivity {

    Audio audio = new Audio();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            timerHandler.postDelayed(this, 2000);
        }
    };

    GameActivity create = new GameActivity();
    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    soprowerwolf.Classes.popup popup = new popup(this);
    int[] playerIDsAndVotes = new int[40];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(globalVariables.isSpielleiter()){audio.playWolfW(WerwolfActivity.this);}

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Werwolf")) {
            setContentView(R.layout.activity_werwolf);
            globalVariables.setCurrentContext(this);

            //View settings: Fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            GameActivity create = new GameActivity();
            create.createObjects();

            popup.PopUpInfo("Wähle nun eine Person, die du töten möchtest", "Werwolf").show();

            start();



        } else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                playerIDsAndVotes = Con.Werwolf("update");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                create.updateVoteButtons(playerIDsAndVotes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, 2000);

        }
    };


    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void start() {
        handler.postDelayed(runnable, 2000);
    }

    public void submitChoice()  {
        try {
            Con.Werwolf("submitChoice");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void onResume(){
        super.onResume();
        start();
    }

    @Override
    protected void onStop(){
        super.onStop();
        stop();
    }

    @Override
    public void onBackPressed() {

    }
}
