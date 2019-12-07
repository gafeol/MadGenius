package com.example.madgenius;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

public class timeController extends AsyncTask<Void, Void, Void> {
    private int maxTime;
    CountDownTimer countdown;
    private ProgressBarListener incrementListener;
    private ProgressBarListener stopListener;
    private ProgressBar pauseListener;

    // Maxtime should be in seconds
    public timeController(int maxtime){
        maxTime = maxtime;
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

    public void setMaxtime(int maxTime){
        this.maxTime = maxTime;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}

