package soprowerwolf.Activities.PhasesActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.DiebDB;
import soprowerwolf.Database.checkingForUpdate;
import soprowerwolf.R;

public class DiebActivity extends AppCompatActivity {

    String choice1, choice2;
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();

    Handler timerHandler = new Handler();
    Runnable timerRunnable= new Runnable() {
        @Override
        public void run() {
            new checkingForUpdate().execute();
            timerHandler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ToDo: wieder einbinden - nur hinderlich, wenn man mit nur einem gerät arbeitet
        //if(globalVariables.getOwnRole().equals("Dieb")) {
            setContentView(R.layout.activity_dieb);
            globalVariables.setCurrentContext(this);

            //View settings: Fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            choice1 = Con.DiebGetRoles()[0];
            choice2 = Con.DiebGetRoles()[1];

            final popup popUp = new popup(this);

            Button choiceLeft = (Button) findViewById(R.id.diebDecisionLeft);
            Button choiceRight = (Button) findViewById(R.id.diebDecisionRight);
            Button stayDor = (Button) findViewById(R.id.ChangeToDorfbewohner);

            assert choiceLeft != null;

            switch (choice1) {
                case "Amor":
                    choiceLeft.setBackgroundResource(R.drawable.amor);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_amor) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
                case "Werwolf":
                    choiceLeft.setBackgroundResource(R.drawable.werwolf);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_werwolf) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
                case "Seherin":
                    choiceLeft.setBackgroundResource(R.drawable.seherin);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_seherin) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
                case "Hexe":
                    choiceLeft.setBackgroundResource(R.drawable.hexe);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_hexe) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
                case "Maedchen":
                    choiceLeft.setBackgroundResource(R.drawable.maedchen);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_maedchen) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
                case "Jaeger":
                    choiceLeft.setBackgroundResource(R.drawable.jaeger);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_jaeger) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
                case "Dorfbewohner":
                    choiceLeft.setBackgroundResource(R.drawable.dorfbewohner);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_dorfbewohner) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, choice1).show();
                        }
                    });
                    break;
            }


            assert choiceRight != null;

            switch (choice2) {
                case "Amor":
                    choiceRight.setBackgroundResource(R.drawable.amor);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_amor) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
                case "Werwolf":
                    choiceRight.setBackgroundResource(R.drawable.werwolf);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_werwolf) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
                case "Seherin":
                    choiceRight.setBackgroundResource(R.drawable.seherin);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_seherin) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
                case "Hexe":
                    choiceRight.setBackgroundResource(R.drawable.hexe);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_hexe) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
                case "Maedchen":
                    choiceRight.setBackgroundResource(R.drawable.maedchen);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_maedchen) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
                case "Jaeger":
                    choiceRight.setBackgroundResource(R.drawable.jaeger);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_jaeger) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
                case "Dorfbewohner":
                    choiceRight.setBackgroundResource(R.drawable.dorfbewohner);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUp.PopUpChoice(getString(R.string.description_dorfbewohner) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, choice2).show();
                        }
                    });
                    break;
            }

            if (choice1.equals("Dorfbewohner") || choice2.equals("Dorfbewohner")) {
                assert stayDor != null;
                stayDor.setVisibility(View.INVISIBLE);
            }

            assert stayDor != null;
            stayDor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUp.PopUpChoice(getString(R.string.description_dorfbewohner) + "\n\n" + "Möchtest du Dorfbewohner bleiben?", "Dorfbewohner", "Dorfbewohner").show();

                }
            });
       /* }

        else
        {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 0);
        }*/
    }

    public void changeRole(String choice)
    {
        //ToDo: update database: change Role
        new DiebDB().execute(choice);

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

    @Override
    public void onBackPressed() {

    }
}
