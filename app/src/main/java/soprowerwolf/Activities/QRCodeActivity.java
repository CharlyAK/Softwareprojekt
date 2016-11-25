package soprowerwolf.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;

import soprowerwolf.Classes.GlobalVariables;
import soprowerwolf.Classes.databaseCon;
import soprowerwolf.Classes.popup;
import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

public class QRCodeActivity extends AppCompatActivity {

    ImageView qrCodeImageview;
    String QRcode;
    int numPlayersIn, numPlayers;
    public final static int WIDTH = 500;
    GlobalVariables globalVariables = GlobalVariables.getInstance();
    databaseCon Con = new databaseCon();
    Snackbar info;
    popup popup = new popup();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        numPlayers = globalVariables.getNumPlayers();
        globalVariables.setCurrentContext(this);
        getID();

        // create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {

            public void run() {

                // this is the msg which will be encode in QRcode
                QRcode = String.valueOf(globalVariables.getGameID());

                try {
                    synchronized (this) {
                        wait(100);

                        // runOnUiThread method used to do UI task in main thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcode);
                                    qrCodeImageview.setImageBitmap(bitmap);

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block

                            } // end of run method
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        t.start();

        info = Snackbar.make(findViewById(R.id.qrCode), getString(R.string.joined) + " " + String.valueOf(numPlayersIn) + "/" + String.valueOf(numPlayers), Snackbar.LENGTH_INDEFINITE);
        info.show();
        start();

    }

    private void getID() {
        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code_image);
    }

    // this is method call from on create and return bitmap image of QRCode.
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } /// end of this method

    //this checks the database every second
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            info.setText(getString(R.string.joined) + " " + String.valueOf(numPlayersIn) + "/" + String.valueOf(numPlayers));
            numPlayersIn = Con.getPlayerInGame();

            //if all players in Game
            if (numPlayersIn == numPlayers)
            {
                stop();
                Intent intent = new Intent(QRCodeActivity.this, GetRole.class);
                startActivity(intent);
            }

            else
                handler.postDelayed(this, 2000);

        }
    };


    public void stop() {
        handler.removeCallbacks(runnable);
    }

    public void start() {
        handler.postDelayed(runnable, 2000);
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

    @Override
    public void onBackPressed() {
        //TODO: game wieder löschen
        popup.PopUpChoice(getString(R.string.backToMenu), "Wirklich zurückkehren?", "back", "").show();
    }

}
