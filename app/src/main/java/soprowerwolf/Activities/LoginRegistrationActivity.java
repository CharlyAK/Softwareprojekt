package soprowerwolf.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.JSONParser;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.R;

public class LoginRegistrationActivity extends AppCompatActivity {

    Button bStartLogin, bStartRegistration, bLogin, bRegistration, bChooseImage;
    EditText textUsername, textEMail, textPassword;
    TextView welcome;
    Intent chooseImage;
    ImageButton bStartScreen;
    ImageView PlayerImage;
    View startLayout;
    final int requode = 42;
    Uri image_uri;
    Bitmap bm = null;
    InputStream is;
    databaseCon Con = new databaseCon();
    int playerID = 0;

    private static final String url_set_login = "http://www-e.uni-magdeburg.de/jkloss/setLoginState.php";

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        globalVariables.setCurrentContext(this);
        globalVariables.setSharedPrefContext(this); // for sharedPref (staying logged in)
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + getString(R.string.app_font));

        bStartScreen = (ImageButton) findViewById(R.id.imageButtonStart);

        bStartLogin = (Button) findViewById(R.id.buttonStartLogin);
        bStartLogin.setTypeface(font);
        bLogin = (Button) findViewById(R.id.buttonLogin);
        bLogin.setTypeface(font);
        bRegistration = (Button) findViewById(R.id.buttonRegistration);
        bRegistration.setTypeface(font);
        bStartRegistration = (Button) findViewById(R.id.buttonStartRegistration);
        bStartRegistration.setTypeface(font);
        textEMail = (EditText) findViewById(R.id.TextEMail);
        textPassword = (EditText) findViewById(R.id.TextPassword);
        textUsername = (EditText) findViewById(R.id.TextUsername);
        PlayerImage = (ImageView) findViewById(R.id.imageViewPlayerImage);
        bChooseImage = (Button) findViewById(R.id.buttonSelectImageStart);
        bChooseImage.setTypeface(font);
        welcome = (TextView) findViewById(R.id.welcome);
        welcome.setTypeface(font);

        startLayout = findViewById(R.id.startLayout);

        // check if, PlayerID exists (if somebody is logged in)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains("PlayerID"))
        {
             playerID = preferences.getInt("PlayerID", -1);
        }

        assert bStartScreen != null;
        bStartScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if PlayerID exists -> continue with Menu
                if(playerID != 0)
                {
                    globalVariables.setOwnPlayerID(playerID);
                    Intent intent = new Intent(globalVariables.getCurrentContext(), MenuActivity.class);
                    startActivity(intent);
                    welcome.setVisibility(View.INVISIBLE);
                }
                // else continue with login/ registration
                else
                {
                    welcome.setVisibility(View.INVISIBLE);
                    bStartScreen.setVisibility(View.INVISIBLE);
                    startLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.login_registration).setBackground(getResources().getDrawable(R.drawable.start));
                }
            }
        });

        bStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLayout.setVisibility(View.INVISIBLE);
                bLogin.setVisibility(View.VISIBLE);
                textEMail.setVisibility(View.VISIBLE);
                textPassword.setVisibility(View.VISIBLE);
            }
        });

        bStartRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLayout.setVisibility(View.INVISIBLE);
                bRegistration.setVisibility(View.VISIBLE);
                textUsername.setVisibility(View.VISIBLE);
                textEMail.setVisibility(View.VISIBLE);
                textPassword.setVisibility(View.VISIBLE);
                PlayerImage.setVisibility(View.VISIBLE);
                bChooseImage.setVisibility(View.VISIBLE);
            }
        });

        bChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
                chooseImage.setType("image/*"); // Bilder aller Formate können ausgewählt werden
                startActivityForResult(chooseImage, requode);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == requode) {
                //Dateipfad auslesen
                image_uri = data.getData();
                try {
                    is = getContentResolver().openInputStream(image_uri);
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    bm = BitmapFactory.decodeStream(is, null, options);
                    PlayerImage.setImageBitmap(bm);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void login(View view) {
        JSONParser jsonParser = new JSONParser();
        if (Con.login(textEMail.getText().toString(), textPassword.getText().toString())) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));
            params.add(new BasicNameValuePair("login", "1"));
            jsonParser.makeHttpRequest(url_set_login, "POST", params);
            // if success -> save PlayerID in sharedPref
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("PlayerID", globalVariables.getOwnPlayerID());
            editor.apply();

            Toast.makeText(getApplicationContext(), "Login erfolgreich", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Login fehlgeschlagen. Bitte überprüfe deine Daten", Toast.LENGTH_SHORT).show();
            textEMail.getText().clear();
            textPassword.getText().clear();
        }

    }

    public void registration(View view) {
        JSONParser jsonParser = new JSONParser();
        //ToDo: Registrierung
        String name = textUsername.getText().toString();
        String email = textEMail.getText().toString();
        String password = textPassword.getText().toString();

        if (Con.registration(name, email, password)) {
            if (!(bm == null))
                Con.setImage(bm);

            Toast.makeText(getApplicationContext(), "Spieler wurde erstellt", Toast.LENGTH_LONG).show();

            // if success -> save PlayerID in sharedPref
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("PlayerID", globalVariables.getOwnPlayerID());
            editor.apply();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("playerID", String.valueOf(globalVariables.getOwnPlayerID())));
            params.add(new BasicNameValuePair("login", "1"));
            jsonParser.makeHttpRequest(url_set_login, "POST", params);

            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Spieler existiert bereits. Bitte wähle einen anderen Nutzernamen", Toast.LENGTH_LONG).show();

            textPassword.getText().clear();
            textEMail.getText().clear();
            textUsername.getText().clear();

        }

    }

    @Override
    public void onBackPressed() {
        bStartScreen.setVisibility(View.VISIBLE);
        welcome.setVisibility(View.VISIBLE);
        findViewById(R.id.login_registration).setBackground(getResources().getDrawable(R.drawable.start));
        bLogin.setVisibility(View.INVISIBLE);
        bRegistration.setVisibility(View.INVISIBLE);
        startLayout.setVisibility(View.INVISIBLE);
        textEMail.setVisibility(View.INVISIBLE);
        textPassword.setVisibility(View.INVISIBLE);
        textUsername.setVisibility(View.INVISIBLE);
        PlayerImage.setVisibility(View.INVISIBLE);
        bChooseImage.setVisibility(View.INVISIBLE);
    }
}
