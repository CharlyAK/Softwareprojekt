package soprowerwolf.Activities.PhasesActivity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.NextPhaseDB;
import soprowerwolf.Database.checkPhases;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

public class AudioActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    Audio audio = new Audio();
    checkPhases check = new checkPhases();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(check.check()) {
                onStop();
                new getCurrentPhase().execute("");
            }
            else {
                timerHandler.postDelayed(this, 3000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        globalVariables.setCurrentContext(this);

        if(globalVariables.isSpielleiter()){
            audio.playAudioSleep(globalVariables.getCurrentPhase());
            globalVariables.setCurrentPhase("Audio");
        }

        else {
            globalVariables.setCurrentPhase("Audio");
            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 3000);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
