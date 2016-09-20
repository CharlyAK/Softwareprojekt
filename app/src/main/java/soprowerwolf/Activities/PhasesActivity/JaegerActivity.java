package soprowerwolf.Activities.PhasesActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import soprowerwolf.Activities.GameActivity;
import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

public class JaegerActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    popup popup = new popup(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jaeger);
        globalVariables.setCurrentContext(this);

        //View settings: Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        String callingPhase = intent.getStringExtra("callingPhase"); // important for setNextPhase -> needed to know if nextPhase of "Jaeger" is "Tag" or "Werwolf"

        final GameActivity create = new GameActivity();
        create.createObjects();

        final TextView InfoJaeger = (TextView) findViewById(R.id.TextJaeger);
        //Button ok = (Button) findViewById(R.id.buttonJaegerContinue);
        //Button info = (Button) findViewById(R.id.buttonJaegerInfo);



        //new setNextPhase().execute(callingPhase): // -> set next Phase
    }
}
