package soprowerwolf.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import soprowerwolf.R;

public class waitForAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_all);

        //ToDo: Anzeigen (textNumJoined), wie viele Spieler dem Spiel bereits beigetreten sind
    }

    public void startGame(View view)
    {
        //ToDo:erst auswählbar, wenn 100% Beitritt erreicht"
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}
