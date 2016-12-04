package soprowerwolf.Activities.PhasesActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Database.HexeDB;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

public class HexeActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    popup popup = new popup();
    Audio audio = new Audio();
    CountDownTimer timer;
    Boolean alive = Con.alive(globalVariables.getOwnPlayerID());

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
        globalVariables.setCurrentPhase("Hexe");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        if (globalVariables.isSpielleiter()) {
            audio.playHexeW();
        }

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Hexe") && alive) {
            setContentView(R.layout.activity_hexe);
            globalVariables.setCurrentContext(this);

            //View settings: Fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            final GameActivity create = new GameActivity();
            create.createObjects();

            final String victimWer = Con.Hexe("getVictimWer");

            final TextView InfoHexe = (TextView) findViewById(R.id.TextHexe);
            Button info = (Button) findViewById(R.id.buttonHexeInfo);
            Button ok = (Button) findViewById(R.id.buttonHexeContinue);

            assert InfoHexe != null;
            assert info != null;
            assert ok != null;

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // TODO: 04.12.16 Da muss noch was rein 
                    popup.PopUpInfo(">Erklärung, was in Phase getan werden soll<", "Info").show();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if a player is selected it will be killed (this can't happen if no poison is available)
                    if (globalVariables.getCurrentlySelectedPlayer() != null) {
                        String csp = String.valueOf(globalVariables.getCurrentlySelectedPlayer().getId());
                        new HexeDB().execute("kill", csp);
                    }

                    new setNextPhase().execute("audio");
                }
            });

            popup.PopUpInfo("Das Opfer der Werwölfe diese Nacht ist " + victimWer, "Hexe").show();

        } else if (globalVariables.getOwnRole().equals("Hexe") && !alive){
            setContentView(R.layout.activity_wait);

            timer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {}

                @Override
                public void onFinish() {
                    new setNextPhase().execute("audio");
                }
            }.start();
        }

        else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 3000);
        }
    }

    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void start() {
        timerHandler.postDelayed(timerRunnable, 2000);
    }


    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }



    @Override
    public void onBackPressed() {

    }
}
