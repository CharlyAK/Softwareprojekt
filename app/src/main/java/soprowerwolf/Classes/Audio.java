package soprowerwolf.Classes;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;

import soprowerwolf.Database.setNextPhase;
import soprowerwolf.R;

/**
 * Created by Gina on 23.09.2016.
 */

public class Audio {

    GlobalVariables globalVariables = GlobalVariables.getInstance();

    MediaPlayer amor_wakeUp, amor_sleep,
                dieb_wakeUp, dieb_sleep,
                hexe_wakeUp, hexe_sleep,
                seherin_wakeUp, seherin_sleep,
                wolf_wakeUp, wolf_sleep,
                tag_wakeUp, tag_sleep;

    CountDownTimer amorW, amorS, diebW, diebS, hexeW, hexeS, seherinW, seherinS, wolfW, wolfS, tagW, tagS;

    public void playAmorW(Context context){
        amor_wakeUp = MediaPlayer.create(context, R.raw.amor_wakeup);
        amor_wakeUp.start();
    }

    public void playAmorS(Context context){
        amor_sleep = MediaPlayer.create(context, R.raw.amor_sleep);
        amorS = new CountDownTimer(amor_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(globalVariables.isSpielleiter()){amor_sleep.start();}
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }

    public void playDiebW(Context context){
        dieb_wakeUp = MediaPlayer.create(context, R.raw.dieb_wakeup);
        dieb_wakeUp.start();
    }

    public void playDiebS(Context context){
        dieb_sleep = MediaPlayer.create(context, R.raw.dieb_sleep);
        diebS = new CountDownTimer(dieb_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (globalVariables.isSpielleiter()){dieb_sleep.start();}
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }

    public void playHexeW(Context context){
        hexe_wakeUp = MediaPlayer.create(context, R.raw.hexe_wakeup);
        hexe_wakeUp.start();
    }

    public void playHexeS(Context context){
        hexe_sleep = MediaPlayer.create(context, R.raw.hexe_sleep);
        hexeS = new CountDownTimer(hexe_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (globalVariables.isSpielleiter()){hexe_sleep.start();}
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }

    public void playSeherinW(Context context){
        seherin_wakeUp = MediaPlayer.create(context, R.raw.seherin_wakeup);
        seherin_wakeUp.start();
    }

    public void playSeherinS(Context context){
        seherin_sleep = MediaPlayer.create(context, R.raw.seherin_sleep);
        seherinS = new CountDownTimer(seherin_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (globalVariables.isSpielleiter()){seherin_sleep.start();}
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }

    public void playWolfW(Context context){
        wolf_wakeUp = MediaPlayer.create(context, R.raw.wolf_wakeup);
        wolf_wakeUp.start();;
    }

    public void playWolfS(Context context){
        wolf_sleep = MediaPlayer.create(context, R.raw.wolf_sleep);
        wolfS = new CountDownTimer(wolf_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (globalVariables.isSpielleiter()){wolf_sleep.start();}
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }

    public void playTagW(Context context){
        tag_wakeUp = MediaPlayer.create(context, R.raw.tag_wakeup);
        tag_wakeUp.start();
    }

    public void playTagS(Context context){
        tag_sleep = MediaPlayer.create(context, R.raw.tag_sleep);
        tagS = new CountDownTimer(tag_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (globalVariables.isSpielleiter()){tag_sleep.start();}
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }
}
