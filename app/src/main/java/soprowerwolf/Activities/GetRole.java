package soprowerwolf.Activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_role);

        ImageButton role = (ImageButton) findViewById(R.id.imageButtonRole);

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
    }

    public void Roledescription(View view) {
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

    public void showRole(View view) {
        ImageButton role = (ImageButton) findViewById(R.id.imageButtonRole);
        Button show = (Button) findViewById(R.id.buttonShowRole);
        role.setVisibility(View.VISIBLE);
        show.setVisibility(View.INVISIBLE);
    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            ready = Con.getReady();

            Snackbar.make(findViewById(R.id.activityGetRole), "Die Dorfbewohner sammeln sich... " + String.valueOf(ready) + "/" + String.valueOf(numPlayers) + " sind bereit.", Snackbar.LENGTH_INDEFINITE).show();

            //if (ready == numPlayers) { // hinderlich, wenn mit nur einem gerät getestet wird
                Intent intent = new Intent(GetRole.this, LetsPlayActivity.class);
                startActivity(intent);
            /*} else
                timerHandler.postDelayed(this, 2000);*/
        }
    };

    public void ready(View view) {
        new setReadyDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        numPlayers = Con.getNumPlayers();

        //check frequently who many players joind the game
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onBackPressed() {

    }
}
