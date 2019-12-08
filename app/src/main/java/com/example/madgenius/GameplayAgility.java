package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameplayAgility extends AppCompatActivity implements RedButtonFragment.OnFragmentInteractionListener,
                                                                    SwitchFragment.OnFragmentInteractionListener,
                                                                    BlueButtonFragment.OnFragmentInteractionListener,
                                                                    SeekBarFragment.OnFragmentInteractionListener {
    private Boolean gameStatus = true;
    private String[] commands = {"Press the red button", "Press the blue button", "Shake the phone", "Turn your phone upside down", "Tap the front of your phone", "Set bar to ", "Switch the toggle"};
    private String[] codes = {"RED", "BLUE", "SHAKE", "UPSIDE", "PROXIMITY", "SEEK", "SwITCH"};
    private String requiredAction;
    private timeController clock;
    private int[] times = {3, 4, 5, 8, 4, 4, 4};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_agility);

        displayFragments();
        setProximity();
        setShaker();
        setUpsideDown();
        getNewCommand();
    }


    public void getNewCommand(){
        Random rand = new Random();
        int randomNum = rand.nextInt(this.commands.length);
        requiredAction = this.codes[randomNum];
        String actionMessage = this.commands[randomNum];
        if(requiredAction == "SEEK"){ // TODO: make a better randomization, see issue #3 on github
            final int SEEKBAR_MAX = 10;
            int randomVal = rand.nextInt(SEEKBAR_MAX+1);
            requiredAction += randomVal;
            actionMessage += randomVal;
        }
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText(actionMessage);

        ProgressBar time = findViewById(R.id.pgbTime);

        int maxTime = this.times[randomNum];
        time.setMax(maxTime);
        time.setProgress(maxTime);

        clock = new timeController(maxTime);

        clock.setTimeIncrementListener(new timeController.ProgressBarListener() {
            @Override
            public void onTimeIncrement() {
                time.setProgress(time.getProgress()-1);
            }
            @Override
            public void onTimeUp(){
                getNewCommand();
            }
        });
        clock.init();
    }

    private void displayFragments(){
        String[] fragClasses = new String[]{"RedButtonFragment", "SwitchFragment", "BlueButtonFragment", "SeekBarFragment"};
        int[] fragLayouts = new int[]{R.id.fragment_container_1, R.id.fragment_container_2, R.id.fragment_container_3, R.id.fragment_container_5};
        try {
            display(fragClasses, fragLayouts);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void display(String className, int frameLayout) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String fullClassName = "com.example.madgenius."+className;
        Class fragClass = Class.forName(fullClassName);
        Object fragment = fragClass.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameLayout, (Fragment)fragment).commit();
    }

    public void display(String[] className, int[] frameLayouts) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for(int i=0;i<className.length;i++){
            String fullClassName = "com.example.madgenius."+className[i];
            Class fragClass = Class.forName(fullClassName);
            Object fragment = fragClass.newInstance();
            fragmentTransaction.add(frameLayouts[i], (Fragment)fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSwitch(Boolean val) {
        executeAction("SWITCH");

    }

    @Override
    public void onBlueButtonClick() {
        executeAction("BLUE");
    }

    @Override
    public void onRedButtonClick() {
        executeAction("RED");
    }

    @Override
    public void onSeekBarUpdate(int val) {
        executeAction("SEEK"+val);
    }

    private void setProximity() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ProximitySensor proximitySensor = new ProximitySensor(sensorManager);
        proximitySensor.setVariableChangeListener(isClose -> {
            if(isClose)
                executeAction("PROXIMITY");
        });
    }

    private void setUpsideDown() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        UpsideDownSensor upsideDownSensor = new UpsideDownSensor(sensorManager);
        upsideDownSensor.setVariableChangeListener(isUpsideDown -> {
            if(isUpsideDown)
                executeAction("UPSIDE");
        });
    }
    /** Function that sets up a shaker listener.
     */
    private void setShaker() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeSensor shake = new ShakeSensor(sensorManager);
        // Passando o contexto atual (getBaseContext()) permite que a classe ShakeSensor crie toasts
        //ShakeSensor shake = new ShakeSensor(getBaseContext(), sensorManager);
        // Defining the action wanted when shaking is detected.
        final TextView textView = findViewById(R.id.shakeText);
        shake.setVariableChangeListener(isShaking -> {
            if(isShaking)
                executeAction("SHAKING");
        });
    }

    private void executeAction(String code){
        this.clock.cancel(true);
        if(code.equals(requiredAction)){
            getNewCommand();
        }
        else{
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else {
                v.vibrate(500);
            }

        }
    }

    /* Example of closing fragment
    public void closeFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check to see if the fragment is already showing.
        RedButtonFragment redButtonFragment = (RedButtonFragment) fragmentManager
                .findFragmentById(R.id.fragment_container);
        if (redButtonFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(redButtonFragment).commit();
        }
        // Set boolean flag to indicate fragment is closed.
    }
     */
}
