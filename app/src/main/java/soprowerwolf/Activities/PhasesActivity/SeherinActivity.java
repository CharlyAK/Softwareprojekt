package soprowerwolf.Activities.PhasesActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

public class SeherinActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seherin);
        globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        globalVariables.setCurrentlySelectedPlayer(null);

        GameActivity create = new GameActivity();
        create.createObjects();

        globalVariables.setPopUpSeherinIdentity(popup.PopUpInfo(null, "Seherin"));

        popup.PopUpInfo("Wähle eine Person deren Identität du erfaheren möchtest", "Seherin").show();
    }

    public void getIdentity()
    {
        //ToDo: database
        databaseCon Con = new databaseCon();
        String GoB = Con.Seherin();

        if(globalVariables.getCurrentlySelectedPlayer() != null)
        {
            globalVariables.getPopUpSeherinIdentity().setMessage(globalVariables.getCurrentlySelectedPlayer().getText().toString() + " ist " + GoB);
            globalVariables.getPopUpSeherinIdentity().show();
        }
    }
}
