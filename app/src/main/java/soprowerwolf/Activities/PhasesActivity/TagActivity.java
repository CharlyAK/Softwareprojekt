package soprowerwolf.Activities.PhasesActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.games.Game;

import org.json.JSONException;

import java.util.Random;
import java.util.TimerTask;

import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.GameOverDB;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;

public class TagActivity extends AppCompatActivity {

    GameActivity create = new GameActivity();
    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup(this);
    int[] playerIDsAndVotes = new int[40];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        globalVariables.setCurrentContext(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                try {
                    Con.Tag("submitChoice");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (globalVariables.isSpielleiter())
            start();

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
            //if all players have voted
            if (votes >= globalVariables.getNumPlayers())
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


        //TODO: if spielleiter nextPhase else TimerRunnable getCurrentPhase
        //setting the victim in the database
        Con.setVictims(victimAndVotes[0]);

        new setNextPhase().execute("");
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
