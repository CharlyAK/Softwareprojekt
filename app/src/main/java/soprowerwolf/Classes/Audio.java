package soprowerwolf.Classes;

import android.content.Context;
import android.media.MediaPlayer;
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
                tag_wakeUp, tag_sleep,
                lover;

    CountDownTimer amorS, diebS, hexeS, seherinS, wolfS, tagS;

    public void playAmorW(Context context){
        amor_wakeUp = MediaPlayer.create(context, R.raw.amor_wakeup);
        amor_wakeUp.start();
    }

    public void playAmorS(){
        amor_sleep = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.amor_sleep);
        amorS = new CountDownTimer(amor_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {amor_sleep.start();}
            @Override
            public void onFinish() {
                new setNextPhase().execute("phase");
            }
        }.start();
    }


    public void playDiebW(Context context){
        dieb_wakeUp = MediaPlayer.create(context, R.raw.dieb_wakeup);
        dieb_wakeUp.start();
    }

    public void playDiebS(){
        dieb_sleep = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.dieb_sleep);
        diebS = new CountDownTimer(dieb_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {dieb_sleep.start();}
            @Override
            public void onFinish() {
                new setNextPhase().execute("phase");
            }
        }.start();
    }


    public void playHexeW(){
        hexe_wakeUp = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.hexe_wakeup);
        hexe_wakeUp.start();
    }

    public void playHexeS(){
        hexe_sleep = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.hexe_sleep);
        hexeS = new CountDownTimer(hexe_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {hexe_sleep.start();}
            @Override
            public void onFinish() {
                new setNextPhase().execute("phase");
            }
        }.start();
    }


    public void playSeherinW(){
        seherin_wakeUp = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.seherin_wakeup);
        seherin_wakeUp.start();
    }

    public void playSeherinS(){
        seherin_sleep = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.seherin_sleep);
        seherinS = new CountDownTimer(seherin_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {seherin_sleep.start();}
            @Override
            public void onFinish() {
                new setNextPhase().execute("phase");
            }
        }.start();
    }


    public void playWolfW(){
        wolf_wakeUp = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.wolf_wakeup);
        wolf_wakeUp.start();
    }

    public void playWolfS(){
        wolf_sleep = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.wolf_sleep);
        wolfS = new CountDownTimer(wolf_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {wolf_sleep.start();}
            @Override
            public void onFinish() {
                new setNextPhase().execute("phase");
            }
        }.start();
    }


    public void playTagW(){
        tag_wakeUp = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.tag_wakeup);
        tag_wakeUp.start();
    }

    public void playTagS(){
        tag_sleep = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.tag_sleep);
        tagS = new CountDownTimer(tag_sleep.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {tag_sleep.start();}
            @Override
            public void onFinish() {
                new setNextPhase().execute("phase");
            }
        }.start();
    }

}
