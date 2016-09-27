package soprowerwolf.Activities.PhasesActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Database.AmorDB;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

public class AmorActivity extends AppCompatActivity {

    popup popup = new popup(AmorActivity.this);
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    Audio audio = new Audio();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute();
            timerHandler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        if (globalVariables.isSpielleiter()){audio.playAmorW(AmorActivity.this);}

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Amor")) {
            setContentView(R.layout.activity_amor);
            globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GameActivity create = new GameActivity();
        create.createObjects();

            Button OK = (Button) findViewById(R.id.buttonOK);
            OK.setEnabled(false);
            globalVariables.setOK(OK);

            assert OK != null;
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lover1 = globalVariables.getLover1().getText().toString();
                    String lover2 = globalVariables.getLover2().getText().toString();

                    popup.PopUpInfo(lover1 + " und " + lover2 + " haben sich ineinander verliebt!", "Amor").show();
                    setLovers();

                }
            });

        } else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    public void setLovers() {
        int Lover1ID = globalVariables.getLover1().getId();
        int Lover2ID = globalVariables.getLover2().getId();

        new AmorDB().execute(String.valueOf(Lover1ID), String.valueOf(Lover2ID));
    }

    public void showInfoAmor(View view) {
        popup.PopUpInfo(getString(R.string.description_amor), "Info").show();
    }

    @Override
    public void onBackPressed() {

    }
}
