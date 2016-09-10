package soprowerwolf.Classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Activities.PhasesActivity.DiebActivity;

/**
 * Created by Alex on 29.08.2016.
 */
public class popup {

    private Activity context;
    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    public popup(Activity context){
        this.context = context;

    }

    /* creates a popup containing information */
    public AlertDialog PopUpInfo(String infotext, final String titel) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle(titel);
        helpBuilder.setMessage(infotext);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if((globalVariables.getCurrentPhaseID() == 3 && globalVariables.getCurrentlySelectedPlayer() != null)
                                || (globalVariables.getCurrentPhaseID() == 1 && titel != "Info")
                                || (globalVariables.getCurrentPhaseID() == 4 && !globalVariables.getDiebChoosen() && Con.Hexe("ableToPoison").equals("1")))
                        {
                            GameActivity create = new GameActivity();
                            create.nextPhase();
                        }
                    }
                });

        AlertDialog helpDialog = helpBuilder.create();
        return helpDialog;
    }

    /* creates a popup offering you a choice */
    public AlertDialog PopUpChoice(String infotext, final String Role, final String toBeDecided) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle(Role);
        helpBuilder.setMessage(infotext);
        helpBuilder.setPositiveButton("Ja",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        popup_callback(true, toBeDecided, Role);
                    }
                });

        helpBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                popup_callback(false, toBeDecided, Role);
            }
        });

        AlertDialog helpDialog = helpBuilder.create();
        return helpDialog;
    }


    /* action based on chosen value in popup */
    public void popup_callback(Boolean choice, String toBeDecided, String Role){
        switch (Role){
            case "Dieb":
                if(choice)
                {
                    DiebActivity dieb = new DiebActivity();
                    dieb.changeRole(toBeDecided, context);
                }
                break;
        }

    }




}
