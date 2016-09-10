package soprowerwolf.Activities.PhasesActivity;

import android.app.Activity;
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

public class DiebActivity extends AppCompatActivity {

    String choice1, choice2;
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieb);
        globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String[]cards = globalVariables.getCards();
        choice1 = cards[cards.length-2];
        choice2 = cards[cards.length-1];

        final popup popUp = new popup(this);

        Button choiceLeft = (Button)findViewById(R.id.diebDecisionLeft);
        Button choiceRight = (Button)findViewById(R.id.diebDecisionRight);
        Button stayDor = (Button)findViewById(R.id.ChangeToDorfbewohner);

        assert choiceLeft != null;
        choiceLeft.setText(choice1);
        choiceLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.PopUpChoice("Möchtest du von nun an als " + choice1 + " spielen?", "Dieb", choice1).show();
            }
        });
        assert choiceRight != null;
        choiceRight.setText(choice2);
        choiceRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.PopUpChoice("Möchtest du von nun an als " + choice2 + " spielen?", "Dieb", choice2).show();
            }
        });

        if(choice1.equals("Dorfbewohner") || choice2.equals("Dorfbewohner"))
        {
            assert stayDor != null;
            stayDor.setVisibility(View.INVISIBLE);
        }

        assert stayDor != null;
        stayDor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.PopUpChoice("Möchtest du Dorfbewohner bleiben?", "Dieb", "Dorfbewohner").show();
            }
        });
    }

    public void changeRole(String choice, Activity context)
    {
        //ToDo: update database: change Role
        databaseCon Con = new databaseCon();
        Con.Dieb(choice);

        String[]phases = globalVariables.getPhases();
        int currentPhase = globalVariables.getCurrentPhaseID();
        phases[currentPhase] = null;

        switch (choice)
        {
            case "Amor":
                phases[1] = "Amor";
                break;

            case "Seherin":
                phases[3] = "Seherin";
                break;

            case "Hexe":
                phases[4] = "Hexe";
                break;

            default:
                break;
        }

        globalVariables.setPhases(phases);


        GameActivity create = new GameActivity();
        create.nextPhase();
    }
}
