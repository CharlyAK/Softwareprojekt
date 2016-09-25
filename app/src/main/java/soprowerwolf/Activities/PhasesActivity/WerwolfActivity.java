package soprowerwolf.Activities.PhasesActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;

public class WerwolfActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    Audio audio = new Audio();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            timerHandler.postDelayed(this, 2000);
        }
    };

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


        } else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
