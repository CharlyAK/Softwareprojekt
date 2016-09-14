package soprowerwolf.Activities.PhasesActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

public class HexeActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    popup popup = new popup(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexe);
        globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final GameActivity create = new GameActivity();
        create.createObjects();

        final String victimWer = Con.Hexe("getVictimWer");

        final TextView InfoHexe = (TextView)findViewById(R.id.TextHexe);
        Button info = (Button) findViewById(R.id.buttonHexeInfo);
        Button ok = (Button)findViewById(R.id.buttonHexeContinue);

        assert InfoHexe != null;
        assert info != null;
        assert ok != null;

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.PopUpInfo(">Erklärung, was in Phase getan werden soll<", "Info").show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if a player is selected it will be killed (this can't happen if no poison is available)
                if (globalVariables.getCurrentlySelectedPlayer() != null)
                    Con.Hexe("kill");
                create.nextPhase();
            }
        });

        popup.PopUpInfo("Das Opfer der Werwölfe ist diese Nacht " + victimWer, "Hexe").show();

    }

    //just kept this for GameActivity - actually not needed
    public void magic(String magic)
    {
        switch(magic){
            case "heal":
                Con.Hexe("saveVictim");
                break;

            case "poison":
                Con.Hexe("kill");
                break;
        }

    }

    @Override
    public void onBackPressed() {

    }
}
