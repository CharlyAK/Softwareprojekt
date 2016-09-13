package soprowerwolf.Activities.PhasesActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import soprowerwolf.R;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;

public class AmorActivity extends AppCompatActivity {

    popup popup = new popup(AmorActivity.this);

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amor);
        globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GameActivity create = new GameActivity();
        create.createObjects();

        Button Info = (Button) findViewById(R.id.buttonInfo);
        Button OK = (Button) findViewById(R.id.buttonOK);
        OK.setEnabled(false);
        globalVariables.setOK(OK);

        assert Info != null;
        Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.PopUpInfo(">Erklärung, was getan werden soll<", "Info").show();
            }
        });

        assert OK != null;
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lover1 = globalVariables.getLover1().getText().toString();
                String  lover2 = globalVariables.getLover2().getText().toString();

                setLovers();
                popup.PopUpInfo(lover1 + " und " + lover2 + " haben sich ineinander verliebt!", "Amor").show();

            }
        });

    }

    public void setLovers()
    {
        databaseCon con = new databaseCon();
        int Lover1ID = globalVariables.getLover1().getId();
        int Lover2ID = globalVariables.getLover2().getId();

        con.Amor(Lover1ID, Lover2ID);

        String[] phases = globalVariables.getPhases();
        phases[globalVariables.getCurrentPhaseID()] = null;
    }

    @Override
    public void onBackPressed() {

    }
}
