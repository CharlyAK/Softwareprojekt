package soprowerwolf.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Database.joinGameDB;
import soprowerwolf.R;


// Scanner benötigt Kamerazugriff (evtl. im Smartphone erlauben)
// Einstellungen -> Anwendungsmanager -> [App] -> Berechtigungen
public class JoinGameActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    Button scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/" + getString(R.string.app_font));

        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRScanner(v);
            }
        });
        scan.setTypeface(font);
        ((TextView) findViewById(R.id.scanCode)).setTypeface(font);
    }

    public void QRScanner(View v){
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @Override
    public void handleResult(Result result){

        String scanningText = result.getText();

        globalVariables.setGameID(Integer.parseInt(scanningText));
        globalVariables.setSpielleiter(false);

        new joinGameDB().execute();

        Intent intent = new Intent(this, GetRole.class);
        startActivity(intent);
        finish();
    }

}
