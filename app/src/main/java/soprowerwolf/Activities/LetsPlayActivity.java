package soprowerwolf.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.R;

public class LetsPlayActivity extends AppCompatActivity {

    MediaPlayer audio;
    CountDownTimer timer;

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        audio = MediaPlayer.create(this, R.raw.first_night);

        timer = new CountDownTimer(audio.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(globalVariables.isSpielleiter()){audio.start();}
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(LetsPlayActivity.this, GameActivity.class);
                startActivity(intent);
            }
        }.start();
    }

    //TODO: zum testen; später entfernen
    public void letsPlay(View view){
        //audio.stop();
        //timer.onFinish();
    }
}
