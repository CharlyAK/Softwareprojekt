package soprowerwolf.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.setReadyDB;
import soprowerwolf.R;

public class GetRole extends AppCompatActivity {
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    popup popup = new popup(this);
    int ready;
    int numPlayers;
    Snackbar info;
    boolean firstClick = true;
    boolean secondClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_role);

        globalVariables.setCurrentPhase("getRole");


        final ImageButton role = (ImageButton) findViewById(R.id.imageButtonRole);

        assert role != null;
        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firstClick && !secondClick) {
                    switch (globalVariables.getOwnRole()) {
                        case "Dieb":
                            role.setBackgroundResource(R.drawable.dieb);
                            break;
                        case "Amor":
                            role.setBackgroundResource(R.drawable.amor);
                            break;
                        case "Werwolf":
                            role.setBackgroundResource(R.drawable.werwolf);
                            break;
                        case "Seherin":
                            role.setBackgroundResource(R.drawable.seherin);
                            break;
                        case "Hexe":
                            role.setBackgroundResource(R.drawable.hexe);
                            break;
                        case "Dorfbewohner":
                            role.setBackgroundResource(R.drawable.dorfbewohner);
                            break;
                        case "Maedchen":
                            role.setBackgroundResource(R.drawable.maedchen);
                            break;
                        case "Jaeger":
                            role.setBackgroundResource(R.drawable.jaeger);
                    }
                    firstClick = false;
                    secondClick = true;
                } else if (!firstClick && secondClick) {
                    Roledescription();
                    secondClick = false;
                } else {
                    role.setBackgroundResource(R.drawable.back);

                    firstClick = true;
                }

            }
        });


    }

    public void Roledescription() {
        String role = globalVariables.getOwnRole();

        switch (role) {
            case "Dorfbewohner":
                popup.PopUpInfo(getString(R.string.description_dorfbewohner), role).show();
                break;

            case "Werwolf":
                popup.PopUpInfo(getString(R.string.description_werwolf), role).show();
                break;

            case "Dieb":
                popup.PopUpInfo(getString(R.string.description_dieb), role).show();
                break;

            case "Amor":
                popup.PopUpInfo(getString(R.string.description_amor), role).show();
                break;

            case "Seherin":
                popup.PopUpInfo(getString(R.string.description_seherin), role).show();
                break;

            case "Hexe":
                popup.PopUpInfo(getString(R.string.description_hexe), role).show();
                break;

            case "Maedchen":
                popup.PopUpInfo(getString(R.string.description_maedchen), role).show();
                break;

            case "Jaeger":
                popup.PopUpInfo(getString(R.string.description_jaeger), role).show();
                break;
        }

    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            ready = Con.getReady();

            info.setText("Die Dorfbewohner sammeln sich... " + String.valueOf(ready) + "/" + String.valueOf(numPlayers) + " sind bereit.");

            if (ready == numPlayers || ready == numPlayers + 2) { // ToDo: ändern
                Intent intent = new Intent(GetRole.this, LetsPlayActivity.class);
                startActivity(intent);
            } else
                timerHandler.postDelayed(this, 2000);
        }
    };

    public void ready(View view) {
        new setReadyDB().execute();
        globalVariables.setNumPlayers(Con.getNumPlayers());
        numPlayers = globalVariables.getNumPlayers();

        info = Snackbar.make(findViewById(R.id.activityGetRole), "Die Dorfbewohner sammeln sich... " + String.valueOf(ready) + "/" + String.valueOf(numPlayers) + " sind bereit.", Snackbar.LENGTH_INDEFINITE);
        info.show();

        //check frequently who many players joined the game
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onBackPressed() {

    }
}
