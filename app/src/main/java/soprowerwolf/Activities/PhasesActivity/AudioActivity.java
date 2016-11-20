package soprowerwolf.Activities.PhasesActivity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.NextPhaseDB;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

public class AudioActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    Audio audio = new Audio();
    NextPhaseDB nextPhaseDB = new NextPhaseDB();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute("");
            timerHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        globalVariables.setCurrentContext(this);

        if(globalVariables.isSpielleiter()){
            switch (globalVariables.getCurrentPhase()){
                case "Dieb":
                    globalVariables.setCurrentPhase("Audio");
                    audio.playDiebS();
                    break;
                case "Amor":
                    globalVariables.setCurrentPhase("Audio");
                    audio.playAmorS();
                    break;
                case "Werwolf":
                    globalVariables.setCurrentPhase("Audio");
                    audio.playWolfS();
                    break;
                case "Seherin":
                    globalVariables.setCurrentPhase("Audio");
                    audio.playSeherinS();
                    break;
                case "Hexe":
                    globalVariables.setCurrentPhase("Audio");
                    audio.playHexeS();
                    break;
                case "OpferTag":
                    globalVariables.setCurrentPhase("Audio");
                    audio.playTagS();
                    break;
            }
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
