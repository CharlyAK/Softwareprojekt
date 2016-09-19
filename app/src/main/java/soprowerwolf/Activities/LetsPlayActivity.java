package soprowerwolf.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import soprowerwolf.R;

public class LetsPlayActivity extends AppCompatActivity {

    MediaPlayer audio;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_play);

        audio = MediaPlayer.create(this, R.raw.pocahontas);

        timer = new CountDownTimer(audio.getDuration(), 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                audio.start();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(LetsPlayActivity.this, GameActivity.class);
                startActivity(intent);
            }
        }.start();
    }

    public void letsPlay(View view){
        timer.onFinish();
    }
}
