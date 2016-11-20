package soprowerwolf.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONException;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.R;

public class LetsPlayActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    MediaPlayer audio;
    CountDownTimer timer;
    String[] images = new String[globalVariables.getNumPlayers()];
    databaseCon Con = new databaseCon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_play);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        Con.getPlayerIDs();
        Con.getPlayerNames();

        int[] playerIDs = globalVariables.getPlayerIDs();

        audio = MediaPlayer.create(this, R.raw.first_night);

        for (int i=0; i<playerIDs.length; i++){
            try {
                images[i] = Con.getImagesAsString(playerIDs[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        globalVariables.setImages(images);

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

}
