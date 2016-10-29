package soprowerwolf.Activities.PhasesActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

public class LoverActivity extends AppCompatActivity {

    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    TextView lover;
    MediaPlayer audio;
    CountDownTimer timer;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().execute("");
            timerHandler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        globalVariables.setCurrentContext(this);
        globalVariables.setCurrentPhase("Lover");
        setContentView(R.layout.activity_lover);

        lover = (TextView) findViewById(R.id.loverInfo);

        //TODO: change Audio
        //audio = MediaPlayer.create(this, R.raw.first_night);

        //lover.setText("(automatisch weiter in 5s)\nDu bist verliebt in " + Con.getLover() + " verliebt." );

        //let the phones of the players that are not in love vibrate
        if (Con.getLover().equals("niemanden")){
            Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(20);
        }

        if(globalVariables.isSpielleiter()){
           timer = new CountDownTimer(5000, 1000){
               @Override
               public void onTick(long millisUntilFinished) {
                   //audio.start();
               }

               @Override
               public void onFinish() {
                   new setNextPhase().execute("");
               }
           }.start();
        }
        else {
            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 2000);
        }
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
