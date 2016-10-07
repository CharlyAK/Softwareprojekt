package soprowerwolf.Activities.PhasesActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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
    popup popup = new popup(this);
    Audio audio = new Audio();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute();
            timerHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (globalVariables.isSpielleiter()) {
            audio.playHexeW(HexeActivity.this);
        }

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Hexe")) {
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
                public void onClick(View v) {
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

                    new setNextPhase().execute("");
                    //audio.playHexeS(HexeActivity.this);
                }
            });

            popup.PopUpInfo("Das Opfer der Werwölfe ist diese Nacht " + victimWer, "Hexe").show();

        } else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 3000);
        }
    }


    @Override
    public void onBackPressed() {

    }
}
