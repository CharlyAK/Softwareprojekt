package soprowerwolf.Activities.PhasesActivity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.JaegerDB;
import soprowerwolf.Database.getCurrentPhase;
import soprowerwolf.R;

public class JaegerActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup(this);

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
        setContentView(R.layout.activity_jaeger);

        globalVariables.setCurrentContext(this);
        globalVariables.setCurrentPhase("Jaeger");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // screen stays on

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (globalVariables.getOwnRole().equals("Jaeger")) {
            GameActivity create = new GameActivity();
            create.createObjects();

            popup.PopUpInfo(getResources().getString(R.string.AufforderungJaeger), "Jäger").show();
            Button ok = (Button) findViewById(R.id.buttonJaegerContinue);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    globalVariables.setVictimJaeger(true);
                    popup.PopUpInfo(globalVariables.getCurrentlySelectedPlayer().getText().toString() + " " + getResources().getString(R.string.EndeJaeger), "Jäger").show();
                }
            });
        } else {
            setContentView(R.layout.activity_show_victim);
            TextView victim = (TextView) findViewById(R.id.victim);

            victim.setText("Der Jäger richtet mit seinem letzten Atemzug sein Gewehr auf einen von euch...");

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

}
