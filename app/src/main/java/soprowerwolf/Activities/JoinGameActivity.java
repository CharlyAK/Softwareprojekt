package soprowerwolf.Activities;

import android.graphics.Camera;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import soprowerwolf.R;

// Scanner benötigt Kamerazugriff (evtl. im Smartphone erlauben)
// Einstellungen -> Anwendungsmanager -> [App] -> Berechtigungen
public class JoinGameActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    String scanningURL;
    private QRCodeReaderView mydecoderview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        scanningURL = text;

        Toast.makeText(getApplicationContext()," scanned text: " + scanningURL, Toast.LENGTH_LONG).show();

        //TODO: add player to game where id = scanningURL
    }

    @Override
    public void cameraNotFound() {  }

    @Override
    public void QRCodeNotFoundOnCamImage() {   }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

}
