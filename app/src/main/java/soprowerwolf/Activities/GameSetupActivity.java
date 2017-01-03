package soprowerwolf.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Database.createGameDB;
import soprowerwolf.R;

public class GameSetupActivity extends AppCompatActivity {

    //array containing the cards, Dieb -> two more cards
    String[] cards = new String[22];
    int i = 0;
    int numberWer = 0;
    int numberDor;
    int fillDor;

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        globalVariables.setCurrentContext(this);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + getString(R.string.app_font));

        ((TextView) findViewById(R.id.spieleinstellungen)).setTypeface(font);
        ((TextView) findViewById(R.id.spielerzahl)).setTypeface(font);
        ((TextView) findViewById(R.id.textView4)).setTypeface(font);

        final NumberPicker players = (NumberPicker) findViewById(R.id.numberPicker);
        final Spinner spinnerWer = (Spinner) findViewById(R.id.spinnerWer);


        //set values for NumberPicker
        assert players != null;
        players.setMinValue(8);
        players.setMaxValue(20);

        //if value of the NumberPicker changes
        players.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                setRecommendedNumberOfWer(picker.getValue());
                calculateGame(players);
            }
        });

        //if value of the Spinner (number of Werewolves) changes, recalculate numberDor
        assert spinnerWer != null;

        spinnerWer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateGame(spinnerWer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //initiate values
        spinnerWer.setSelection(1, true);
        calculateGame(players);
    }

    public void setRecommendedNumberOfWer(int players) {

        if (players < 12)
            ((Spinner) findViewById(R.id.spinnerWer)).setSelection(1, true);
        else if (players < 17)
            ((Spinner) findViewById(R.id.spinnerWer)).setSelection(2, true);
        else
            ((Spinner) findViewById(R.id.spinnerWer)).setSelection(3, true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void calculateGame(View view) {
        i = 1;
        cards[0] = "Seherin"; //always a part of the game
        fillDor = ((NumberPicker) findViewById(R.id.numberPicker)).getValue();
        Button startGame = (Button) findViewById(R.id.startGame);
        final Spinner spinnerWer = (Spinner) findViewById(R.id.spinnerWer);

        numberDor = ((NumberPicker) findViewById(R.id.numberPicker)).getValue();


        assert spinnerWer != null;
        //subtract the number of werewolves from the numberDor
        numberDor -= Integer.parseInt((String) spinnerWer.getSelectedItem());


        //calculate number of extra roles
        if (((CheckBox) (findViewById(R.id.checkBoxDie))).isChecked()) {
            numberDor--;
            cards[i++] = "Dieb";
            cards[i++] = "Dorfbewohner";
            cards[i++] = "Dorfbewohner";
            globalVariables.setDiebChoosen(true);

            fillDor = ((NumberPicker) findViewById(R.id.numberPicker)).getValue() + 2;
        }
        if (((CheckBox) (findViewById(R.id.checkBoxAmo))).isChecked()) {
            numberDor--;
            cards[i++] = "Amor";
        }
        if (((CheckBox) (findViewById(R.id.checkBoxHex))).isChecked()) {
            numberDor--;
            cards[i++] = "Hexe";
        }
        if (((CheckBox) (findViewById(R.id.checkBoxMaed))).isChecked()) {
            numberDor--;
            cards[i++] = "Maedchen";
        }
        if (((CheckBox) (findViewById(R.id.checkBoxJaeg))).isChecked()) {
            numberDor--;
            cards[i++] = "Jaeger";
        }

        ((TextView) findViewById(R.id.numberDor)).setText(" " + numberDor);


        assert startGame != null;
        startGame.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {

                Toast.makeText(globalVariables.getCurrentContext(), "Spiel wird erstellt...", Toast.LENGTH_SHORT).show();

                if(numberDor < 0){
                    Toast.makeText(globalVariables.getCurrentContext(),
                            "Diese Spieleinstellungen funktionieren nicht",Toast.LENGTH_LONG).show();
                    return;
                }

                //insert werewolves
                numberWer = Integer.parseInt((String) spinnerWer.getSelectedItem());
                int k = i;
                for (i = k; i < k + numberWer; i++) {
                    cards[i] = "Werwolf";
                }

                //fill with "dorfbewohnern"
                for (; i < fillDor; i++) {
                    cards[i] = "Dorfbewohner";
                }

                String[] cardsShuffled = new String[i];
                //shuffle roles
                for (int j = 0; j < i; j++) {
                    //the last two roles are for the decision of the thief - none of them has to be the "Dieb" itself
                    // + if there is only one "Werwolf" and the Role "Dieb" contains to the game - non of the decisionCards for the "Dieb" has to be a "Werwolf"
                    // -> otherwise it could be possible to have no "Werwolf" in the game
                    if (cards[j] == "Dieb" || (cards[j] == "Werwolf" && numberWer == 1 && globalVariables.getDiebChoosen())) {
                        int h = i - 2;
                        //pick a random number in array
                        Random random = new Random();
                        int value = random.nextInt(h);
                        //move on until an empty slot is found
                        while (cardsShuffled[value] != null)
                            value = (value + 1) % h;

                        cardsShuffled[value] = cards[j];
                    } else {
                        //pick a random number in array
                        Random random = new Random();
                        int value = random.nextInt(i);
                        //move on until an empty slot is found
                        while (cardsShuffled[value] != null)
                            value = (value + 1) % i;

                        cardsShuffled[value] = cards[j];
                    }

                }

                //set up the phases
                String[] searchFor = {"Dieb", "Amor", "Werwolf", "Seherin", "Hexe"};
                String[] phase = {"Dieb", "Amor", "Werwolf", "Seherin", "Hexe", "Tag"};

                int j;
                for (j = 0; j < searchFor.length; j++) {
                    boolean inGame = false;
                    for (int i = 0; i < cardsShuffled.length; i++) {
                        if (cardsShuffled[i].equals(searchFor[j])) {
                            inGame = true;
                        }
                    }

                    if (!inGame) {
                        int i = phase.length - 1;
                        while (!phase[i].equals(searchFor[j])) {
                            i--;
                        }

                        phase[i] = "";
                    }
                }

                globalVariables.setPhases(phase);
                globalVariables.setNextPhase(phase[0]);
                globalVariables.setNumPlayers(((NumberPicker) findViewById(R.id.numberPicker)).getValue());
                globalVariables.setCards(cardsShuffled);
                globalVariables.setOwnRole(cardsShuffled[0]);
                globalVariables.setSpielleiter(true);

                globalVariables.setWinner("nobody");
                new createGameDB().execute();
            }

        });
    }
}
