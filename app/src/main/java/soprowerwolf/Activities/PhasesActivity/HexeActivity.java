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
        Button poison = (Button)findViewById(R.id.buttonGift);
        Button heal = (Button)findViewById(R.id.buttonHeil);
        Button info = (Button) findViewById(R.id.buttonHexeInfo);
        Button ok = (Button)findViewById(R.id.buttonHexeContinue);

        assert InfoHexe != null;
        assert poison != null;
        assert heal != null;
        assert info != null;
        assert ok != null;

        poison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoHexe.setText("So wähle dein Opfer");
            }
        });

        heal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoHexe.setText("Du hast " + victimWer + " gerettet");
                magic("heal");
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.PopUpInfo(">Erklärung, was in Phase getan werden soll<", "Info").show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.nextPhase();
            }
        });

        if(Con.Hexe("ableToSave").equals("0"))
        {
            heal.setEnabled(true);
            InfoHexe.setText("Das Opfer der Werwölfe ist diese Nacht " + victimWer + " - Möchtest du es retten?");
        }
        else
        {
            heal.setEnabled(false);
        }

        if(Con.Hexe("ableToPoison").equals("0")) //ToDo: Frage: bekommt die Hexe das Opfer auch zu sehen, wenn sie keinen Heiltrank mehr hat?
        {
            poison.setEnabled(true);
        }
        else
        {
            poison.setEnabled(false);
        }

        if(Con.Hexe("ableToSave").equals("0") && Con.Hexe("ableToPoison").equals("0"))
        {
            InfoHexe.setText("Du hast alle deine Tränke aufgebraucht");
        }

    }

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
}
