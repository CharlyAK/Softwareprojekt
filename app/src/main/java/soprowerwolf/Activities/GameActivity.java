package soprowerwolf.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import soprowerwolf.Classes.Audio;
import org.json.JSONException;

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

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    Audio audio = new Audio();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        globalVariables.setCurrentContext(this);

        globalVariables.setCurrentlySelectedPlayer(null);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        new getCurrentPhase().execute();
    }

    /*
    * >createObjects<
    *
    * This method creates all objects in the GameView.
    * Like rows, buttons, ...
    *
    */
    public void createObjects() {
        int numOfPlayers = globalVariables.getNumPlayers();
        Activity context = globalVariables.getCurrentContext();
        int[] playerIDs = Con.getPlayerIDs();
        String[] playerNames = Con.getPlayerNames();

        //create Linear Layouts in gameView
        LinearLayout row1 = (LinearLayout) context.findViewById(R.id.row1);
        LinearLayout row2 = (LinearLayout) context.findViewById(R.id.row2);
        LinearLayout row3 = (LinearLayout) context.findViewById(R.id.row3);
        LinearLayout row4 = (LinearLayout) context.findViewById(R.id.row4);

        //create playerbuttons
        for (int i = 0; i < numOfPlayers; i++) {
            Button button = new Button(context);

            //limit the buttons to a maximum size
            DisplayMetrics dm = new DisplayMetrics();
            button.setMaxHeight(dm.heightPixels/4);
            button.setMaxWidth(dm.widthPixels/5);

            button.setText(playerNames[i]);
            /*try {
                button.setBackground(new BitmapDrawable(getResources(), Con.getImage()));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
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

                        case "Seherin":
                            SeherinActivity seherin = new SeherinActivity();
                            seherin.getIdentity(); // die Seherin bekommt die Gesinnung des ausgwählten Spielers gezeigt
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
                for (int j=0; j < row.getChildCount(); j++){
                    if (row.getChildAt(j).isClickable()) {
                        Button currentButton = (Button) row.getChildAt(j);
                        currentButton.setBackgroundColor(0);
                    }
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

    public void setUnclickable(){

        Activity context = globalVariables.getCurrentContext();
        ViewGroup gameView = (ViewGroup) context.findViewById(R.id.gameView);
        for (int i = 0; i < gameView.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) gameView.getChildAt(i);
            for (int j=0; j < row.getChildCount(); j++){
                if (row.getChildAt(j).isClickable()) {
                    Button currentButton = (Button) row.getChildAt(j);
                    currentButton.setClickable(false);
                }
            }
        }
    }

}
