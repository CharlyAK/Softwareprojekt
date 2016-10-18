package soprowerwolf.Activities.PhasesActivity;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.killDB;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.Database.showVictimDB;
import soprowerwolf.R;

public class showVictimActivity extends AppCompatActivity {

    showVictimDB showVictimDB = new showVictimDB();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    TextView victim, v1;
    MediaPlayer tag;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_victim);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        victim = (TextView) findViewById(R.id.victim);
        v1 = (TextView) findViewById(R.id.v1);

        tag = MediaPlayer.create(showVictimActivity.this, R.raw.tag_wakeup);

        if (globalVariables.getCurrentPhase().equals("OpferNacht")) {
            if (!globalVariables.getJaegerDies()) {
                final String[] victims = showVictimDB.getVictims();
                victim.setText("Von uns gegangen sind...");

                for (String victim1 : victims) {
                    if (!victim1.equals("0")) {
                        v1.setText(v1.getText().toString() + "\n" + victim1);
                    }
                }

                if (globalVariables.isSpielleiter()) {
                    tag.start();
                }

                timer = new CountDownTimer(tag.getDuration() + 10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        new killDB().execute("");
                        new setNextPhase().execute("");
                    }
                }.start();
            } else {
                final String[] victims = showVictimDB.getVictims();
                v1.setText("Vom Jäger erschossen: ");

                for (String victim1 : victims) {
                    if (!victim1.equals("0")) {
                        v1.setText(v1.getText().toString() + "\n" + victim1);
                    }
                }

                timer = new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        globalVariables.setJaegerDies(false);
                        new killDB().execute("");
                        new setNextPhase().execute("");
                    }
                }.start();
            }
        } else {
            if (!globalVariables.getJaegerDies()) {
                final String[] victims = showVictimDB.getVictims();
                victim.setText("Von uns gegangen sind...");

                for (String victim1 : victims) {
                    if (!victim1.equals("0")) {
                        v1.setText(v1.getText().toString() + "\n" + victim1);
                    }
                }

                timer = new CountDownTimer(tag.getDuration() + 10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        new killDB().execute("");
                        new setNextPhase().execute("");
                    }
                }.start();
            } else {
                final String[] victims = showVictimDB.getVictims();
                v1.setText("Vom Jäger erschossen: ");

                for (String victim1 : victims) {
                    if (!victim1.equals("0")) {
                        v1.setText(v1.getText().toString() + "\n" + victim1);
                    }
                }

                timer = new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        globalVariables.setJaegerDies(false);
                        new killDB().execute("");
                        new setNextPhase().execute("");
                    }
                }.start();
            }
        }


    }
}
