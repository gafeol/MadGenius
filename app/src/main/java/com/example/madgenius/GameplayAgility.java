package com.example.madgenius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class GameplayAgility extends AppCompatActivity implements RedButtonFragment.OnFragmentInteractionListener,
        SwitchFragment.OnFragmentInteractionListener,
        BlueButtonFragment.OnFragmentInteractionListener,
        SeekBarFragment.OnFragmentInteractionListener,
        YellowButtonFragment.OnFragmentInteractionListener,
        GreenButtonFragment.OnFragmentInteractionListener {
    private CountDownTimer countdown;
    private ProgressBar time;
    private TextView pointTextView, speedTextView;
    private double speed = 0.9;
    private boolean fragmentsDisplayed = false;
    private final String FRAG_DISPLAY_TAG = "fragments_displayed";
    private final String POINTS_TAG = "points";
    private String[] commands = {"Press the red button", "Press the blue button", "Press the yellow button", "Press the green button", "Shake the phone", "Turn your phone upside down", "Tap the front of your phone", "Set bar to ", "Switch the toggle"};
    private String[] codes = {"RED", "BLUE", "YELLOW", "GREEN", "SENSOR_SHAKE", "SENSOR_UPSIDE", "SENSOR_PROXIMITY", "SEEK", "SWITCH"};

    String[] fragClasses = new String[]{"RedButtonFragment", "SwitchFragment", "BlueButtonFragment", "GreenButtonFragment", "YellowButtonFragment", "SeekBarFragment"};
    int[] fragLayouts = new int[]{R.id.fragment_container_1, R.id.fragment_container_2, R.id.fragment_container_3, R.id.fragment_container_4, R.id.fragment_container_8,  R.id.fragment_container_5};
    private String requiredAction;
    private int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_agility);
        pointTextView = findViewById(R.id.pointTextView);
        time = findViewById(R.id.pgbTime);
        speedTextView = findViewById(R.id.speedTextView);

        setPoints(0);
        fragmentsDisplayed = false;
        if(savedInstanceState != null){
            fragmentsDisplayed = savedInstanceState.getBoolean(FRAG_DISPLAY_TAG);
            points = savedInstanceState.getInt(POINTS_TAG);
            setPoints(points);
        }
        if (!fragmentsDisplayed) {
            shuffle(fragLayouts, fragLayouts.length-1);
            int[] longFragLayouts = new int[] {R.id.fragment_container_5, R.id.fragment_container_6, R.id.fragment_container_7};
            Random rand = new Random();
            fragLayouts[fragLayouts.length-1] = longFragLayouts[rand.nextInt(longFragLayouts.length)];
            displayFragments();
        }
        setProximity();
        setShaker();
        setUpsideDown();
        getNewCommand();
    }

    private void setPoints(int p) {
        points = p;
        if(p == 1)
            pointTextView.setText("Point: "+p);
        else
            pointTextView.setText("Points: "+p);
    }

    private void onTimeIncrement() {
        time.setProgress(time.getProgress() - 1);
    }

    private void onTimeUp() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(200);
        }
        finishGame();
    }

    public void getNewCommand() {
        speed += 0.1;
        speedTextView.setText(String.format("Speed: %.1f", speed));
        Random rand = new Random();
        int randomNum = rand.nextInt(this.commands.length);
        requiredAction = this.codes[randomNum];
        String actionMessage = this.commands[randomNum];
        if (requiredAction == "SEEK") {
            SeekBarFragment sbFrag = new SeekBarFragment();
            int previousValue = sbFrag.seekBarValue;
            int randomVal;
            final int SEEKBAR_MAX = 10;
            do {
                randomVal = rand.nextInt(SEEKBAR_MAX + 1);
            } while (previousValue == randomVal);
            requiredAction += randomVal;
            actionMessage += randomVal;
        }
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText(actionMessage);

        double maxTime = 5 - speed + 1;
        time.setMax((int)(maxTime*10));
        time.setProgress((int)(maxTime*10));

        countdown = new CountDownTimer((int)(maxTime*1000)+100, 100) {
            public void onTick(long millisUntilFinished) {
                onTimeIncrement();
            }
            public void onFinish() {
                onTimeUp();
            }
        };

        countdown.start();
    }

    private void shuffle(int[] values, int maxPosition){
        Random rnd = new Random();
        for(int i=0;i<maxPosition;i++){
            int index = rnd.nextInt(i+1);
            int aux = values[i];
            values[i] = values[index];
            values[index] = aux;
        }
    }

    private void displayFragments() {
        try {
            display(fragClasses, fragLayouts);
            fragmentsDisplayed = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(FRAG_DISPLAY_TAG, fragmentsDisplayed);
        super.onSaveInstanceState(outState);
    }

    public void display(String[] className, int[] frameLayouts) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < className.length; i++) {
            String fullClassName = "com.example.madgenius." + className[i];
            Class fragClass = Class.forName(fullClassName);
            Object fragment = fragClass.newInstance();
            fragmentTransaction.replace(frameLayouts[i], (Fragment) fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSwitch(Boolean val) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("SWITCH");
    }
    @Override
    public void onBlueButtonClick() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("BLUE");
    }

    @Override
    public void onRedButtonClick() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("RED");
    }

    @Override
    public void onGreenButtonClick() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("GREEN");
    }

    @Override
    public void onYellowButtonClick() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("YELLOW");
    }

    @Override
    public void onSeekBarUpdate(int val) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("SEEK" + val);
    }

    private void setProximity() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ProximitySensor proximitySensor = new ProximitySensor(sensorManager);
        proximitySensor.setVariableChangeListener(isClose -> {
            if (isClose)
                executeAction("SENSOR_PROXIMITY");
        });
    }

    private void setUpsideDown() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        UpsideDownSensor upsideDownSensor = new UpsideDownSensor(sensorManager);
        upsideDownSensor.setVariableChangeListener(isUpsideDown -> {
            if (isUpsideDown)
                executeAction("SENSOR_UPSIDE");
        });
    }

    /**
     * Function that sets up a shaker listener.
     */
    private void setShaker() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeSensor shake = new ShakeSensor(sensorManager);
        shake.setVariableChangeListener(isShaking -> {
            if(isShaking)
                executeAction("SENSOR_SHAKE");
        });
    }

    private void executeAction(String code) {
        if(requiredAction == null || requiredAction.isEmpty())
            return;
        Log.d("ACTION", "required " + requiredAction + " action executed " + code);
        if(code.equals(requiredAction)){
            this.countdown.cancel();
            setPoints(points+1);
            getNewCommand();
        }
        else{
            if(code.contains("SENSOR"))
                return;
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(200);
            }
            finishGame();
        }
    }

    private void finishGame(){
        this.countdown.cancel();
        requiredAction = "";
        scoreMessage();
    }

    private void scoreMessage(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameplayAgility.this);
        View messageView = getLayoutInflater().inflate(R.layout.dialog_save_score, null);
        EditText usernameEditText = messageView.findViewById(R.id.usernameEditText);
        TextView pointsMessage = messageView.findViewById(R.id.scoreTextView);
        pointsMessage.setText("You solved " + points + " actions!");

        Button saveButton = messageView.findViewById(R.id.saveButton);
        Button cancelButton = messageView.findViewById(R.id.cancelButton);

        mBuilder.setView(messageView);
        AlertDialog dialog = mBuilder.create();

        saveButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            if(username.isEmpty()){
                usernameEditText.setError("Please fill your username");
            }
            else {
                ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
                scoreViewModel.insert(new Score(username, points, false));
                dialog.dismiss();
                finish();
            }
        });

        cancelButton.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });
        dialog.show();
    }

}
