package soprowerwolf.Activities.PhasesActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.DiebDB;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

public class DiebActivity extends AppCompatActivity {

    String choice1, choice2;
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    Audio audio = new Audio();

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute("");
            timerHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on
        globalVariables.setCurrentContext(this);
        globalVariables.setCurrentPhase("Dieb");

        if(globalVariables.isSpielleiter()){ audio.playAudioWakeup(); }

        //check, if own Role equals Phase -> yes: Activity is shown; no: black screen is shown (activity_wait)
        if (globalVariables.getOwnRole().equals("Dieb")) {
            setContentView(R.layout.activity_dieb);

            //View settings: Fullscreen
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            String[] choices = Con.DiebGetRoles();
            choice1 = choices[0];
            choice2 = choices[1];

            final popup popup = new popup();

            Button choiceLeft = (Button) findViewById(R.id.diebDecisionLeft);
            Button choiceRight = (Button) findViewById(R.id.diebDecisionRight);
            Button stayDor = (Button) findViewById(R.id.ChangeToDorfbewohner);

            // if one choices is "Werwolf" -> Dieb has to become "Werwolf"
            if(choice1.equals("Werwolf") || choice2.equals("Werwolf"))
            {
                choiceLeft.setVisibility(View.INVISIBLE);
                choiceRight.setVisibility(View.INVISIBLE);
                stayDor.setVisibility(View.INVISIBLE);
                popup.PopUpInfo(getString(R.string.becomeWolf), "Dieb").show();
            }



            assert choiceLeft != null;

            // first button gets picture an info of the role (choice1)
            switch (choice1) {
                case "Amor":
                    choiceLeft.setBackgroundResource(R.drawable.amor);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_amor) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
                case "Werwolf":
                    choiceLeft.setBackgroundResource(R.drawable.werwolf);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_werwolf) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
                case "Seherin":
                    choiceLeft.setBackgroundResource(R.drawable.seherin);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_seherin) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
                case "Hexe":
                    choiceLeft.setBackgroundResource(R.drawable.hexe);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_hexe) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
                case "Maedchen":
                    choiceLeft.setBackgroundResource(R.drawable.maedchen);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_maedchen) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
                case "Jaeger":
                    choiceLeft.setBackgroundResource(R.drawable.jaeger);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_jaeger) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
                case "Dorfbewohner":
                    choiceLeft.setBackgroundResource(R.drawable.dorfbewohner);
                    choiceLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_dorfbewohner) + "\n\n" + "Möchtest du von nun an als " + choice1 + " spielen?", choice1, "Dieb", "0").show();
                        }
                    });
                    break;
            }


            assert choiceRight != null;

            //second button gets picture and info of the role (choice2)
            switch (choice2) {
                case "Amor":
                    choiceRight.setBackgroundResource(R.drawable.amor);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_amor) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
                case "Werwolf":
                    choiceRight.setBackgroundResource(R.drawable.werwolf);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_werwolf) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
                case "Seherin":
                    choiceRight.setBackgroundResource(R.drawable.seherin);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_seherin) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
                case "Hexe":
                    choiceRight.setBackgroundResource(R.drawable.hexe);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_hexe) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
                case "Maedchen":
                    choiceRight.setBackgroundResource(R.drawable.maedchen);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_maedchen) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
                case "Jaeger":
                    choiceRight.setBackgroundResource(R.drawable.jaeger);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_jaeger) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
                case "Dorfbewohner":
                    choiceRight.setBackgroundResource(R.drawable.dorfbewohner);
                    choiceRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.PopUpChoice(getString(R.string.description_dorfbewohner) + "\n\n" + "Möchtest du von nun an als " + choice2 + " spielen?", choice2, "Dieb", "1").show();
                        }
                    });
                    break;
            }

            // if one of the coices is "Dorfbewohner" -> the button "stayDof" becomes invisible
            // => if both choices are speciale roles -> Dieb can also stay "Dorfbewohner"
            if (choice1.equals("Dorfbewohner") || choice2.equals("Dorfbewohner")) {
                assert stayDor != null;
                stayDor.setVisibility(View.INVISIBLE);
            }

            assert stayDor != null;
            stayDor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.PopUpChoice(getString(R.string.description_dorfbewohner) + "\n\n" + "Möchtest du Dorfbewohner bleiben?", "Dorfbewohner", "Dieb", "2").show();

                }
            });

        } else {
            setContentView(R.layout.activity_wait);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 3000);
        }
    }

    public void changeRole(String choice) {
        switch (choice) {
            case "0": //first choice was chosen
                globalVariables.setOwnRole(Con.DiebGetRoles()[0]);
                new DiebDB().execute(Con.DiebGetRoles()[0], Con.DiebGetRoles()[1], "");
                break;
            case "1": // second choice was chosen
                globalVariables.setOwnRole(Con.DiebGetRoles()[1]);
                new DiebDB().execute(Con.DiebGetRoles()[1], Con.DiebGetRoles()[0], "");
                break;
            case "2": // none of the roles were chosen
                globalVariables.setOwnRole("Dorfbewohner");
                new DiebDB().execute("Dorfbewohner", Con.DiebGetRoles()[0], Con.DiebGetRoles()[1]);
                break;
            case "Werwolf": // if Dieb has to become "Werwolf"
                globalVariables.setOwnRole("Werwolf");
                String[] choices = Con.DiebGetRoles();
                choice1 = choices[0];
                choice2 = choices[1];

                //ToDo: wenn beide rollen zur wahl werwolf sind - werwolfphase nicht löschen!
                if(choice1.equals("Werwolf"))
                {
                    new DiebDB().execute("Werwolf", choice2, "");
                }
                else new DiebDB().execute("Werwolf", choice1, "");
        }
    }

    public void showInfoDieb(View view) {
        popup.PopUpInfo(getString(R.string.description_dieb), "Info").show();
    }

    public void stop() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void start() {
        timerHandler.postDelayed(timerRunnable, 2000);
    }


    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }


    @Override
    public void onBackPressed() {

    }

}
