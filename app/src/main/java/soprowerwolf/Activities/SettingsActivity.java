package soprowerwolf.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.InputStream;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText playerName;
    ImageView playerImage;
    Button Image_button;
    Intent chooseImage;
    final int requode = 42;
    Uri image_uri;
    Bitmap bm;
    InputStream is;

    databaseCon Con = new databaseCon();
    GlobalVariables globalVariables = GlobalVariables.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + getString(R.string.app_font));

        //Name auswählen
        playerName = (EditText) findViewById(R.id.SettingsUsername);

        //Bild auswählen
        playerImage = (ImageView) findViewById(R.id.imageView);
        try {
            playerImage.setImageDrawable(new BitmapDrawable(getResources(),Con.getImage()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Image_button = (Button) findViewById(R.id.button);
        Image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
                chooseImage.setType("image/*"); // Bilder aller Formate können ausgewählt werden
                startActivityForResult(chooseImage, requode);

            }
        });
        Image_button.setTypeface(font);

        //Open SharedPref-File
        SharedPreferences mySPR_Name = getSharedPreferences("MySPFile", 0);

        playerName.setText(Con.getName());

        ((Button) findViewById(R.id.deleteAccount)).setTypeface(font);
        ((Button) findViewById(R.id.SettingsSaveUsername)).setTypeface(font);
    }

    // Wird ausgeführt, sobald von einem Intent in diese Activity gesendet wird
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == requode) {
                //Dateipfad auslesen
                image_uri = data.getData();
                try {
                    is = getContentResolver().openInputStream(image_uri);
                    //create bitmap from Stream
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    bm = BitmapFactory.decodeStream(is, null, options);
                    playerImage.setImageBitmap(bm);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveSettings(View view) {

        //Open SharedPref-File
        SharedPreferences mySPR = getSharedPreferences("MySPFile", 0);

        //initializing Editorclass
        SharedPreferences.Editor editor = mySPR.edit();

        //get text from text field and write it into editor instance
        editor.putString("KeyName", playerName.getText().toString());

        //save
        editor.commit();
        Con.setImage(bm);
        Snackbar.make(view, "Dein Bild wurde gespeicher", Snackbar.LENGTH_LONG).show();
    }

    public void deleteAccount(View view) {
        popup popup = new popup();
        GlobalVariables globalVariables = GlobalVariables.getInstance();
        globalVariables.setCurrentContext(this);
        popup.PopUpChoice("Möchtest du deinen Account wirklich löschen?", "Account löschen?", "Account", null).show();
    }

}
