package soprowerwolf.Activities.PhasesActivity;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import soprowerwolf.Classes.Audio;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.killDB;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.Database.showVictimDB;
import soprowerwolf.R;

public class showVictimActivity extends AppCompatActivity {

    showVictimDB showVictimDB = new showVictimDB();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    TextView InfoVictim, v1, v2;
    MediaPlayer tag;
    CountDownTimer timer;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute();
            timerHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_victim);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        InfoVictim = (TextView) findViewById(R.id.victim);
        v1 = (TextView) findViewById(R.id.v1);
        v2 = (TextView) findViewById(R.id.v2);

        tag = MediaPlayer.create(showVictimActivity.this, R.raw.tag_wakeup);


        String[] victims = showVictimDB.getVictims();

        /* victims[0] = victimDor; victim[1] = [victimDor = good] or [victimDor = bad]
         * victims[2] = victimWer; victim[3] = [victimWer = good] or [victimWer = bad]
         * victims[4] = victimHex; victim[5] = [victimHex = good] or [victimHex = bad]
         * victims[6] = Lover; victim[7] = [Lover = good] or [Lover = bad]
         * victims[8] = victimJaeg; victim[9] = [victimJaeg = good] or [victimJaeg = bad]
         * victims[10] = LoverVictimJaeg; victim[11] = [LoverVictimJaeg = good] or [LoverVictimJaeg = bad]
         * victims[12] = Lover of victim[6] --> easier for showing (see below)
         * victims[13] = number of victims --> for a better Text
         */

        switch (victims[13]) {
            case "0": // no victim
                if (globalVariables.getCurrentPhase().equals("OpferNacht")) {
                    InfoVictim.setText(getString(R.string.showNoVictimNight));
                }
                else {
                    InfoVictim.setText(getString(R.string.showNoVictimDay));
                }
                break;
            case "1": // one victim
                if (globalVariables.getCurrentPhase().equals("OpferNacht")) {
                    InfoVictim.setText(getString(R.string.showOneVictimNight));
                }
                else {
                    InfoVictim.setText(getString(R.string.showOneVictimDay));
                }
                for (int i = 0; i < 6; i = i + 2) {
                    if (!victims[i].equals("0")) { // was the victim good or bad?
                        v1.setText(v1.getText().toString() + "\n" + victims[i] + " " + getString(R.string.victimWas) + " " + victims[i + 1]);
                    }
                }

                if (!victims[8].equals("0")) { //"Jaeger" died and shot somebody
                    v2.setText(victims[8] + " " + getString(R.string.shotByJaeger) + " " + victims[9]);
                    if (!victims[10].equals("0")) { // the victim of "Jaeger" had a lover
                        v2.setText(v2.getText().toString() + "\n" + victims[10] + " " + getString(R.string.victimLover) + " " + victims[8] + " " + getString(R.string.victimLoverWas) + " " + victims[11]);
                    }

                    globalVariables.setJaegerDies(false);
                }
                break;
            default:// more than one victim
                if (globalVariables.getCurrentPhase().equals("OpferNacht")) {
                    InfoVictim.setText(getString(R.string.showTwoVictimsNight));
                }
                else {
                    InfoVictim.setText(getString(R.string.showOneVictimDay));
                }
                for (int i = 0; i < 6; i = i + 2) {
                    if (!victims[i].equals("0")) { // was the victim good or bad?
                        v1.setText(v1.getText().toString() + "\n" + victims[i] + " " + getString(R.string.victimWas) + " " + victims[i + 1]);
                    }
                }

                if (!victims[6].equals("0")) { // the victim and its lover died --> was the lover good or bad?
                    v1.setText(v1.getText().toString() + "\n" + victims[6] + " " + getString(R.string.victimLover) + " " + victims[12] + " " + getString(R.string.victimLoverWas) + " " + victims[7]);
                }

                if (!victims[8].equals("0")) { //"Jaeger" died and shot somebody
                    v2.setText(victims[8] + " " + getString(R.string.shotByJaeger) + " " + victims[9]);
                    if (!victims[10].equals("0")) { // the victim of "Jaeger" had a lover
                        v2.setText(v2.getText().toString() + "\n" + victims[10] + " " + getString(R.string.victimLover) + " " + victims[8] + " " + getString(R.string.victimLoverWas) + " " + victims[11]);
                    }

                    globalVariables.setJaegerDies(false);
                }
                break;
        }

        timer = new CountDownTimer(tag.getDuration() + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (globalVariables.isSpielleiter() && globalVariables.getJaegerDies()) {
                    // if "Jaeger" dies -> don't kill, call "JaegerActivity"
                    new setNextPhase().execute("");
                } else if (globalVariables.isSpielleiter() && !globalVariables.getJaegerDies()) {
                    // kill and set next phase
                    new killDB().execute("");
                    if(globalVariables.getCurrentPhase().equals("OpferTag"))
                        new setNextPhase().execute("audio");
                    else
                        new setNextPhase().execute("");
                } else
                    timerHandler.postDelayed(timerRunnable, 3000);
            }
        }.start();
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

}
