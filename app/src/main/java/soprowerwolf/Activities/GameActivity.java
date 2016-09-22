package soprowerwolf.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

import soprowerwolf.Activities.PhasesActivity.AmorActivity;
import soprowerwolf.Activities.PhasesActivity.DiebActivity;
import soprowerwolf.Activities.PhasesActivity.HexeActivity;
import soprowerwolf.Activities.PhasesActivity.SeherinActivity;
import soprowerwolf.Activities.PhasesActivity.TagActivity;
import soprowerwolf.Activities.PhasesActivity.WerwolfActivity;
import soprowerwolf.Classes.GlobalVariables;


public class GameActivity extends AppCompatActivity {

    //string contains phases, counter keeps track of current phase

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        globalVariables.setCurrentContext(this);

        globalVariables.setCurrentlySelectedPlayer(null);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new getCurrentPhase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /*
    * >createObjects<
    *
    * This method creates all objects in the GameView.
    * Like rows, button, ...
    *
    */
    public void createObjects() {
        int numOfPlayers = globalVariables.getNumPlayers();
        Activity context = globalVariables.getCurrentContext();
        int[] playerIDs = Con.getPlayerIDs();

        //create Linear Layouts in gameView
        LinearLayout row1 = (LinearLayout) context.findViewById(R.id.row1);
        LinearLayout row2 = (LinearLayout) context.findViewById(R.id.row2);
        LinearLayout row3 = (LinearLayout) context.findViewById(R.id.row3);
        LinearLayout row4 = (LinearLayout) context.findViewById(R.id.row4);

        //create playerbuttons
        for (int i = 0; i < numOfPlayers; i++) {
            Button button = new Button(context);
            button.setText("player" + i);
            /* ==> funktioniert, ist aber hinderlich, wenn man nur mit einem gerät spielt //ToDo: wieder einbinden
            if(playerIDs[i] == 0) // if playerID = 0 -> player is dead and cannot be selected anymore
            {
                button.setEnabled(false);
            }
            else*/
            button.setId(playerIDs[i]);
            button.setBackgroundColor(0);
            // TODO: JSON - getAllPlayer.php
            //button.setText(player[i]);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerSelected(v);
                    switch (globalVariables.getCurrentPhase()) { //bei manchen Phasen passiert mehr, wenn ein Spieler ausgewählt wurde
                        case "Werwolf":
                            new setNextPhase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ""); // kommt dann in Phase
                            break;

                        case "Seherin":
                            SeherinActivity seherin = new SeherinActivity();
                            seherin.getIdentity(); // die Seherin bekommt die Gesinnung des ausgwählten Spielers gezeigt
                            break;

                        case "Tag":
                            new setNextPhase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ""); //kommt dann in Phase
                            break;
                    }
                }
            };
            button.setOnClickListener(onClickListener);
            //button.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);

            //insert into rows (Linear Layouts)
            if (i < 5)
                row1.addView(button);
            else if (i < 10)
                row2.addView(button);
            else if (i < 15)
                row3.addView(button);
            else
                row4.addView(button);
        }
    }

    /*
    * >playerSelected<
    *
    * This method is called if a playerButton gets selected.
    * It toggles the background transparency and
    * updates currentlySelectedPlayer.
    *
    */

    public void playerSelected(View view) {
        Button button = (Button) view;
        Button currentlySelectedPlayer = globalVariables.getCurrentlySelectedPlayer();
        Button lover1 = globalVariables.getLover1();
        Button lover2 = globalVariables.getLover2();
        Activity context = globalVariables.getCurrentContext();

        //two players can be choosen if the Phase is "Amor"
        if (globalVariables.getCurrentPhase().equals("Amor")) {
            if (lover1 == null) {
                globalVariables.setLover1(button);
                button.setBackgroundColor(context.getResources().getColor(R.color.button_material_dark));

                if (lover2 != null) {
                    globalVariables.getOK().setEnabled(true);
                }
            } else if (lover1 != null && button.equals(lover1)) {
                globalVariables.setLover1(null);
                button.setBackgroundColor(0);
                globalVariables.getOK().setEnabled(false);
            } else if (lover1 != null && lover2 == null) {
                globalVariables.setLover2(button);
                button.setBackgroundColor(context.getResources().getColor(R.color.button_material_dark));
                globalVariables.getOK().setEnabled(true);
            } else if (lover2 != null && button.equals(lover2)) {
                globalVariables.setLover2(null);
                button.setBackgroundColor(0);
                globalVariables.getOK().setEnabled(false);
            }

        } else {
            //make all buttons transparent
            ViewGroup gameView = (ViewGroup) context.findViewById(R.id.gameView);
            for (int i = 0; i < gameView.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) gameView.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    Button currentButton = (Button) row.getChildAt(j);
                    currentButton.setBackgroundColor(0);
                }
            }

            //if button was already selected, unselect it
            if (button.equals(currentlySelectedPlayer)) {
                globalVariables.setCurrentlySelectedPlayer(null);
            }
            //otherwise select it
            else {
                globalVariables.setCurrentlySelectedPlayer(button);
                button.setBackgroundColor(context.getResources().getColor(R.color.button_material_dark));
            }
        }


    }

}
