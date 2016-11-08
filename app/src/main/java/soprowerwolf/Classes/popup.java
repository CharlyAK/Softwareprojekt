package soprowerwolf.Classes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import soprowerwolf.Activities.LoginRegistrationActivity;
import soprowerwolf.Activities.PhasesActivity.DiebActivity;
import soprowerwolf.Database.HexeDB;
import soprowerwolf.Database.JaegerDB;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

/**
 * Created by Alex on 29.08.2016.
 */
public class popup {

    private static Activity context;
    private static databaseCon Con = new databaseCon();
    private static GlobalVariables globalVariables = GlobalVariables.getInstance();
    static Audio audio = new Audio();

    public popup() {
    }

    /* creates a popup containing information */
    public static AlertDialog PopUpInfo(String infotext, final String titel) {

        context = globalVariables.getCurrentContext();
        final String victimWer = Con.Hexe("getVictimWer");
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle(titel);
        helpBuilder.setMessage(infotext);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if ((globalVariables.getCurrentPhase().equals("Seherin") && globalVariables.getCurrentlySelectedPlayer() != null)) {
                            new setNextPhase().execute("audio");
                        }
                        ////this is called after the info screen for the hexe if heal and poison are unavailable
                        if (globalVariables.getCurrentPhase().equals("Hexe") && Con.Hexe("ableToSave").equals("1") && Con.Hexe("ableToPoison").equals("1")) {
                            new setNextPhase().execute("audio");
                        }
                        if (globalVariables.getCurrentPhase().equals("Amor") && titel != "Info") {
                            new setNextPhase().execute("audio");
                        }
                        if (globalVariables.getCurrentPhase().equals("Dieb") && titel != "Info") {
                            new setNextPhase().execute("audio");
                        }
                        //this is called after the info screen for the hexe if the heal is available
                        else if (globalVariables.getCurrentPhase().equals("Hexe") && Con.Hexe("ableToSave").equals("0")) {
                            popup popup = new popup();
                            popup.PopUpChoice("Möchtest du " + victimWer + " retten?", "Hexe", "HexeH", "safe").show();
                        }
                        //this is called after the info screen for the hexe if heal isn't available and poison is available
                        else if (globalVariables.getCurrentPhase().equals("Hexe") && Con.Hexe("ableToSave").equals("1") && Con.Hexe("ableToPoison").equals("0")) {
                            popup popup = new popup();
                            popup.PopUpChoice("Möchtest du deinen Gifttrank verwenden?", "Hexe", "HexeP", "poison").show();
                        }
                        else if (globalVariables.getCurrentPhase().equals("Jaeger") && globalVariables.getVictimJaeger()) {
                            new JaegerDB().execute(String.valueOf(globalVariables.getCurrentlySelectedPlayer().getId()));
                        }
                    }
                });

        AlertDialog helpDialog = helpBuilder.create();
        return helpDialog;
    }

    /* creates a popup offering you a choice */
    public AlertDialog PopUpChoice(String infotext, String titel, final String Role, final String toBeDecided) {

        context = globalVariables.getCurrentContext();

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setTitle(titel);
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

        return helpBuilder.create();
    }

    /**
     * This Method creates a View that shows a player's image.
     * It is called by a long click on a player button
     * @return it returns the AlertDialog
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlertDialog imageView(String playerImageString){
        context = globalVariables.getCurrentContext();
        //create a ImageView that will contain the playerImage
        ImageView imageView = new ImageView(context);
        byte[] decodedString = Base64.decode(playerImageString, Base64.DEFAULT);
        Bitmap bitmapResult = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageDrawable(new BitmapDrawable(context.getResources(), bitmapResult));

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(context);
        helpBuilder.setView(imageView);

        return helpBuilder.create();
    }


    /* action based on chosen value in popup */
    public void popup_callback(Boolean choice, String toBeDecided, String Role) {
        switch (Role) {
            case "Account":
                if (choice) {
                    if (Con.deleteAccount()) {
                        Toast.makeText(context.getApplicationContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginRegistrationActivity.class);
                        context.startActivity(intent);
                    } else
                        Toast.makeText(context.getApplicationContext(), "delete Account failed", Toast.LENGTH_SHORT).show();
                }
                break;

            case "Dieb":
                if (choice) {
                    DiebActivity dieb = new DiebActivity();
                    dieb.changeRole(toBeDecided);
                }
                break;

            case "HexeH":
                if (choice) {
                    new HexeDB().execute("saveVictim");
                    PopUpInfo(Con.Hexe("getVictimWer") + " wurde gerettet", "Hexe - Heiltrank").show();
                } else {
                    //if no heal selected and poison is available
                    if (Con.Hexe("ableToPoison").equals("0")) {
                        PopUpChoice("Möchtest du deinen Gifttrank verwenden?", "Hexe", "HexeP", "poison").show();
                    }
                    //if no heal selected and poison is unavailable
                    else {
                        new setNextPhase().execute("audio");
                    }
                }
                break;

            case "HexeP":
                //if no poison is used the next phase starts
                if (!choice) {
                    new setNextPhase().execute("audio");
                }
                break;
        }


    }


}
