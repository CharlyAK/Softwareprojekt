package soprowerwolf.Activities.PhasesActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

public class SeherinActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup(this);

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

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Seherin")) {
            setContentView(R.layout.activity_seherin);
            globalVariables.setCurrentContext(this);

            //View settings: Fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            globalVariables.setCurrentlySelectedPlayer(null);

            GameActivity create = new GameActivity();
            create.createObjects();

            globalVariables.setPopUpSeherinIdentity(popup.PopUpInfo(null, "Seherin"));

            popup.PopUpInfo(getString(R.string.AufforderungSeherin), "Seherin").show();

        } else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    public void getIdentity() {
        //ToDo: database
        databaseCon Con = new databaseCon();
        String GoB = Con.Seherin();

        if (globalVariables.getCurrentlySelectedPlayer() != null) {
            globalVariables.getPopUpSeherinIdentity().setMessage(globalVariables.getCurrentlySelectedPlayer().getText().toString() + " ist " + GoB);
            globalVariables.getPopUpSeherinIdentity().show();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
