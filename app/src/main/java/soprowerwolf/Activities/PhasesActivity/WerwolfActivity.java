package soprowerwolf.Activities.PhasesActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import org.json.JSONException;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;

public class WerwolfActivity extends AppCompatActivity {

    Audio audio = new Audio();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute();
            timerHandler.postDelayed(this, 2000);
        }
    };

    GameActivity create = new GameActivity();
    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    soprowerwolf.Classes.popup popup = new popup(this);
    int[] playerIDsAndVotes = new int[40];
    int numOfWer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(globalVariables.isSpielleiter()){audio.playWolfW(WerwolfActivity.this);}

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Werwolf")) {
            setContentView(R.layout.activity_werwolf);
            globalVariables.setCurrentContext(this);
            try {
                numOfWer = Con.Werwolf("getNumOfWerAlive")[0];
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //View settings: Fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            GameActivity create = new GameActivity();
            create.createObjects();

            popup.PopUpInfo("Wähle nun eine Person, die du töten möchtest", "Werwolf").show();

            final Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if no player is selected
                    if(globalVariables.getCurrentlySelectedPlayer() == null)
                        return;

                    buttonConfirm.setClickable(false);
                    try {
                        Con.Werwolf("submitChoice");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

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
                int votes = 0;
                //count how many player have already voted
                for (int i = 1; i < playerIDsAndVotes.length - 1; i += 2) {
                    votes += playerIDsAndVotes[i];
                }
                //if all players have voted
                if (votes == numOfWer)
                    getResult(playerIDsAndVotes);

                else
                    handler.postDelayed(this, 2000);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    // @params: playerIDsAndVotes contains {id, numOfVotes,...}
    private void getResult(int[] playerIDsAndVotes){
        stop();
        int victimAndVotes[] = new int[2];
        for (int i=1; i < playerIDsAndVotes.length-1; i+=2){
            //if a player has more votes than the current victim, swap
            if (playerIDsAndVotes[i] > victimAndVotes[1]){ // TODO: implement a draw
                victimAndVotes[0] = playerIDsAndVotes[i-1];
                victimAndVotes[1] = playerIDsAndVotes[i];
            }
        }
        //setting the victim in the database
        Con.setVictims(victimAndVotes[0]);

        new setNextPhase().execute( "");
    }


    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void start() {
        handler.postDelayed(runnable, 2000);
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
