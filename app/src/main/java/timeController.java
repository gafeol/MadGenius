import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madgenius.R;

public class timeController extends AsyncTask<Void, Void, Void> {
    private int max_time;
    private int elapsed_time;
    CountDownTimer countdown;
    //private ProgressBar bar;

    // Maxtime should be in seconds
    public timeController(int maxtime){
        max_time = maxtime;
        //bar = findViewById(R.id.pgb_time);
        /*
        countdown = new CountDownTimer(maxtime*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                ProgressBarIncrementListener.onProgressBarIncrement();

            }

            public void onFinish() {
                mTextField.setText("done!");
            }
        };
        */

    }

    public void init(){


    }

    public void pause(){

    }

    public void stop(){

    }

    public interface ProgressBarIncrementListener{
        default void onProgressBarIncrement() {

        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }



    public interface TimeStoppedListenner {
        void onTimeStop(boolean isTimeUp);
    }
}

