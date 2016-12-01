package soprowerwolf.Activities.PhasesActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;

public class TagActivity extends AppCompatActivity {

    GameActivity create = new GameActivity();
    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup();
    Boolean alive = Con.alive(globalVariables.getOwnPlayerID());
    int[] playerIDsAndVotes = new int[40];

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
        setContentView(R.layout.activity_tag);
        globalVariables.setCurrentContext(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //check for results
        if (globalVariables.isSpielleiter())
            start();
        else//wait for next phase
            timerHandler.postDelayed(timerRunnable, 3000);

        if(!alive){
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 3000);
            return;
        }

        create.createObjects();

        popup.PopUpInfo("Wähle nun eine Person, die du hängen möchtest", "Tägliche Abstimmung").show();

        final Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if no player is selected
                if(globalVariables.getCurrentlySelectedPlayer() == null)
                    return;

                buttonConfirm.setClickable(false);
                GameActivity gameActivity = new GameActivity();
                //makes all buttons unclickable
                gameActivity.setUnclickable();
                Toast.makeText(globalVariables.getCurrentContext(),
                        "Deine Wahl wurde gespeichert", Toast.LENGTH_LONG).show();
                try {
                    Con.Tag("submitChoice");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //this checks the database every second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                playerIDsAndVotes = Con.Tag("update");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int votes = 0;
            //count how many player have already voted
            for (int i=1; i < playerIDsAndVotes.length-1; i+=2){
                votes += playerIDsAndVotes[i];
            }
            //if all players alive have voted
            if (votes >= globalVariables.getNumPlayersAlive())
                getResult(playerIDsAndVotes);

            else
                handler.postDelayed(this, 2000);

        }
    };


    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void start() {
        handler.postDelayed(runnable, 2000);
    }

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

        Con.setVictims(victimAndVotes[0]);

        new setNextPhase().execute("");
    }

    protected void onResume() {
        super.onResume();
        if (globalVariables.isSpielleiter())
            start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
        if (globalVariables.isSpielleiter())
            stop();

    }

    @Override
    public void onBackPressed() {

    }
}
