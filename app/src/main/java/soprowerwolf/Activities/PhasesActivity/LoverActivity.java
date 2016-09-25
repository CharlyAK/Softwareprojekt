package soprowerwolf.Activities.PhasesActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

public class LoverActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    Vibrator love;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            new getCurrentPhase().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            timerHandler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(false){
            setContentView(R.layout.activity_lover);
            globalVariables.setCurrentContext(this);
        }

        else {
            setContentView(R.layout.activity_wait);

            love = (Vibrator) LoverActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            love.vibrate(5000);

            //check frequently if phase has been changed
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
