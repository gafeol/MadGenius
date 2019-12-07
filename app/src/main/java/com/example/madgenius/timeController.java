package com.example.madgenius;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class timeController extends AsyncTask<Void, Void, Void> {
    private int max_time;
    private int elapsed_time;
    CountDownTimer countdown;
    private ProgressBarListener incrementListener;
    private ProgressBarListener stopListener;
    private ProgressBar pauseListener;



    // Maxtime should be in seconds
    public timeController(int maxtime){
        max_time = maxtime;
        //bar = findViewById(R.id.pgb_time);

        countdown = new CountDownTimer(maxtime*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                incrementListener.onTimeIncrement();
            }

            public void onFinish() {
                incrementListener.onTimeUp();
            }
        };

    }

    public void init(){
        countdown.start();
    }

    public void pause(){

    }

    public void stop(){

    }

    public interface ProgressBarListener {
        void onTimeIncrement();
        void onTimeUp();
    }

    public void setTimeIncrementListener(ProgressBarListener incrementListener) {
        this.incrementListener = incrementListener;
    }

    public void setTimeUpListener(ProgressBarListener stopListener) {
        this.stopListener = stopListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }



    public interface TimeStoppedListenner {
        void onTimeStop(boolean isTimeUp);
    }
}

