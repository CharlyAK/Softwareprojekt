package soprowerwolf.Activities.PhasesActivity;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.killDB;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.Database.showVictimDB;
import soprowerwolf.R;

public class showVictimActivity extends AppCompatActivity {

    showVictimDB showVictimDB = new showVictimDB();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    TextView victim, v1, v2, v3;
    MediaPlayer tag;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_victim);

        victim = (TextView) findViewById(R.id.victim);
        v1 = (TextView) findViewById(R.id.v1);
        v2 = (TextView) findViewById(R.id.v2);
        v3 = (TextView) findViewById(R.id.v3);

        final String[] victims = showVictimDB.getVictims();
        tag = MediaPlayer.create(showVictimActivity.this, R.raw.tag_wakeup);
        timer = new CountDownTimer(tag.getDuration() + 10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(globalVariables.isSpielleiter()){tag.start();}

                victim.setText("Von uns gegangen sind...");

                if(!victims[0].equals(null)){v1.setText(victims[0]);}
                if(!victims[1].equals(null)){v2.setText(victims[1]);}
                if(!victims[2].equals(null)){v3.setText(victims[2]);}
            }

            @Override
            public void onFinish() {
                new killDB().execute("");
                new setNextPhase().execute("");
            }
        }.start();
    }
}
