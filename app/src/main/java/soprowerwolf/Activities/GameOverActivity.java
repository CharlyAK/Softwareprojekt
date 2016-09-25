package soprowerwolf.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.popup;
import soprowerwolf.R;

public class GameOverActivity extends AppCompatActivity {

    GlobalVariables globalVariables = GlobalVariables.getInstance();
    popup popup = new popup(GameOverActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        globalVariables.setCurrentContext(this);

        /*
        if((globalVariables.getOwnRole().equals("Werwolf") && globalVariables.getWinner().equals("Werwölfe")) ||
                (!globalVariables.getOwnRole().equals("Werwolf") && globalVariables.getWinner().equals("Dorfbewohner"))){
            popup.PopUpInfo("Die " + globalVariables.getWinner()+ " haben gewonnen! :)", "Glückwunsch!").show();
        }
        else {
            popup.PopUpInfo("Die " + globalVariables.getWinner() + " haben euch leider ausgerottet.", "Dumm gelaufen!").show();
        }
        */

        popup.PopUpInfo("...hat gewonnen", "Gewinner").show();
    }

    public void backToMenu(View view){
        Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
