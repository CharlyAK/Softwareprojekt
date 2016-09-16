package soprowerwolf.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import soprowerwolf.Classes.databaseCon;
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

        String[] cards = globalVariables.getCards();

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set up the phases
        String[] searchFor = {"Dieb", "Amor", "Werwolf", "Seherin", "Hexe"};
        String[] phase = {"Dieb", "Amor", "Werwolf", "Seherin", "Hexe", "Tag"};

        int j;
        for (j=0; j<searchFor.length; j++) {
            boolean inGame = false;
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].equals(searchFor[j])) {
                    inGame = true;
                }
            }

            if(!inGame)
            {
                int i = phase.length-1;
                while(!phase[i].equals(searchFor[j]))
                {
                    i--;
                }

                phase[i] = null;
            }
        }

        if(this.globalVariables.getDiebChoosen()) // if "Dieb" is one of the Roles -> the last two Roles aren't allocated yet - so they don't belong into the phaseArray
        {
            for (j=1; j < 3; j++) {
                for (int i = 0; i < phase.length-1; i++) {
                    if(phase[i] != null)
                    {
                        if (phase[i].equals(cards[cards.length-j])) {
                            phase[i] = null;
                        }
                    }
                }
            }
        }

        this.globalVariables.setPhases(phase);
        this.globalVariables.setCurrentPhaseID(-1);

        nextPhase();
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
        int[]playerIDs = Con.getPlayerIDs();

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
                    switch (globalVariables.getCurrentPhaseID()){ //bei manchen Phasen passiert mehr, wenn ein Spieler ausgewählt wurde
                        case 2:
                            nextPhase();
                            break;

                        case 3:
                            SeherinActivity seherin = new SeherinActivity();
                            seherin.getIdentity(); // die Seherin bekommt die Gesinnung des ausgwählten Spielers gezeigt
                            break;

                        case 4:
                            HexeActivity hexe = new HexeActivity();
                            hexe.magic("poison"); // bei der Hexe wird das ausgewählte Opfer in Datenbank geladen
                            break;

                        case 5:
                            nextPhase();
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

    public void playerSelected(View view){
        Button button = (Button) view;
        Button currentlySelectedPlayer = globalVariables.getCurrentlySelectedPlayer();
        Button lover1 = globalVariables.getLover1();
        Button lover2 = globalVariables.getLover2();
        Activity context = globalVariables.getCurrentContext();

        if(globalVariables.getCurrentPhaseID() == 1)
        {
            if(lover1 == null)
            {
                globalVariables.setLover1(button);
                button.setBackgroundColor(context.getResources().getColor(R.color.button_material_dark));

                if(lover2 != null)
                    globalVariables.getOK().setEnabled(true);
            }
            else if(lover1 != null && button.equals(lover1))
            {
                globalVariables.setLover1(null);
                button.setBackgroundColor(0);
                globalVariables.getOK().setEnabled(false);
            }
            else if(lover1 != null && lover2 == null)
            {
                globalVariables.setLover2(button);
                button.setBackgroundColor(context.getResources().getColor(R.color.button_material_dark));
                globalVariables.getOK().setEnabled(true);
            }
            else if(lover2 != null && button.equals(lover2))
            {
                globalVariables.setLover2(null);
                button.setBackgroundColor(0);
                globalVariables.getOK().setEnabled(false);
            }

        }
        else
        {
            //make all buttons transparent
            ViewGroup gameView =(ViewGroup) context.findViewById(R.id.gameView);
            for (int i=0; i < gameView.getChildCount(); i++){
                LinearLayout row = (LinearLayout) gameView.getChildAt(i);
                for (int j=0; j < row.getChildCount(); j++){
                    Button currentButton = (Button) row.getChildAt(j);
                    currentButton.setBackgroundColor(0);
                }
            }

            //if button was already selected, unselect it
            if(button.equals(currentlySelectedPlayer)) {
                globalVariables.setCurrentlySelectedPlayer(null);
            }
            //otherwise select it
            else{
                globalVariables.setCurrentlySelectedPlayer(button);
                button.setBackgroundColor(context.getResources().getColor(R.color.button_material_dark));
            }
        }


    }

    public void nextPhase()
    {
        Activity context = globalVariables.getCurrentContext();
        String[] phases = globalVariables.getPhases();
        int currentPhaseID = globalVariables.getCurrentPhaseID();

        currentPhaseID = (currentPhaseID+1) % phases.length;
        while(phases[currentPhaseID] == null)
            currentPhaseID = (currentPhaseID+1) % phases.length;



        switch (currentPhaseID){
            case 0:
                globalVariables.setCurrentPhaseID(currentPhaseID);
                Intent dieb = new Intent(context, DiebActivity.class);
                context.startActivity(dieb);
                break;

            case 1:
                globalVariables.setCurrentPhaseID(currentPhaseID);
                Intent amor = new Intent(context, AmorActivity.class);
                context.startActivity(amor);
                break;

            case 2:
                globalVariables.setCurrentPhaseID(currentPhaseID);
                Intent werwolf = new Intent(context, WerwolfActivity.class);
                context.startActivity(werwolf);
                break;

            case 3:
                globalVariables.setCurrentPhaseID(currentPhaseID);
                Intent seherin = new Intent(context, SeherinActivity.class);
                context.startActivity(seherin);
                break;

            case 4:
                globalVariables.setCurrentPhaseID(currentPhaseID);
                Intent hexe = new Intent(context, HexeActivity.class);
                context.startActivity(hexe);
                break;

            case 5:
                globalVariables.setCurrentPhaseID(currentPhaseID);
                Intent tag = new Intent(context, TagActivity.class);
                context.startActivity(tag);
                break;
        }
    }

}
