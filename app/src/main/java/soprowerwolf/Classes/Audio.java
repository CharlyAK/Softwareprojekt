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

    MediaPlayer audio;
    CountDownTimer timer;

    public void playAudioSleep(String currentPhase) {
        switch (currentPhase) {
            case "Amor":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.amor_sleep);
                break;
            case "Dieb":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.dieb_sleep);
                break;
            case "Hexe":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.hexe_sleep);
                break;
            case "Seherin":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.seherin_sleep);
                break;
            case "OpferTag":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.tag_sleep);
                break;
            case "Werwolf":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.wolf_sleep);
                break;
        }

        timer = new CountDownTimer(audio.getDuration(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                audio.start();
            }

            @Override
            public void onFinish() {
                new setNextPhase().execute("");
            }
        }.start();
    }


    public void playAudioWakeup(){
        switch(globalVariables.getCurrentPhase()){
            case "Amor":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.amor_wakeup);
                break;
            case "Dieb":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.dieb_wakeup);
                break;
            case "Hexe":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.hexe_wakeup);
                break;
            case "Seherin":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.seherin_wakeup);
                break;
            case "OpferNacht":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.tag_wakeup);
                break;
            case "Werwolf":
                audio = MediaPlayer.create(globalVariables.getCurrentContext(), R.raw.wolf_wakeup);
                break;
        }
        audio.start();
    }
}