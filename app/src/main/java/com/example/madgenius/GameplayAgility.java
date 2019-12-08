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
import android.widget.Toast;

import java.util.Random;

public class GameplayAgility extends AppCompatActivity implements RedButtonFragment.OnFragmentInteractionListener,
        SwitchFragment.OnFragmentInteractionListener,
        BlueButtonFragment.OnFragmentInteractionListener,
        SeekBarFragment.OnFragmentInteractionListener {
    private CountDownTimer countdown;
    private ProgressBar time;
    private boolean fragmentsDisplayed = false;
    private final String FRAG_DISPLAY_TAG = "fragments_displayed";
    private final String POINTS_TAG = "points";
    private String[] commands = {"Press the red button", "Press the blue button", "Shake the phone", "Turn your phone upside down", "Tap the front of your phone", "Set bar to ", "Switch the toggle"};
    private String[] codes = {"RED", "BLUE", "SHAKE", "UPSIDE", "PROXIMITY", "SEEK", "SWITCH"};
    private String requiredAction;
    private int points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_agility);
        time = findViewById(R.id.pgbTime);

        points = 0;
        fragmentsDisplayed = false;
        if(savedInstanceState != null){
            fragmentsDisplayed = savedInstanceState.getBoolean(FRAG_DISPLAY_TAG);
            points = savedInstanceState.getInt(POINTS_TAG);
        }
        if (!fragmentsDisplayed) {
            displayFragments();
        }
        setProximity();
        setShaker();
        setUpsideDown();
        getNewCommand();

        Context context = getApplicationContext();
        CharSequence text = "eh async mesmo?!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    private void onTimeIncrement() {
        time.setProgress(time.getProgress() - 1);
    }

    private void onTimeUp() {
        getNewCommand();
    }

    public void getNewCommand() {
        Random rand = new Random();
        int randomNum = rand.nextInt(this.commands.length);
        requiredAction = this.codes[randomNum];
        String actionMessage = this.commands[randomNum];
        if (requiredAction == "SEEK") {
            SeekBarFragment sbFrag = new SeekBarFragment();
            int previousValue = sbFrag.seekBarValue;
            int randomVal = 0;
            final int SEEKBAR_MAX = 10;
            do {
                randomVal = rand.nextInt(SEEKBAR_MAX + 1);
            } while (previousValue == randomNum);
            requiredAction += randomVal;
            actionMessage += randomVal;
        }
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText(actionMessage);

        int maxTime = 4;
        time.setMax(maxTime);
        time.setProgress(maxTime);

        countdown = new CountDownTimer(maxTime * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                onTimeIncrement();
            }

            public void onFinish() {
                onTimeUp();
            }
        };

        countdown.start();
    }

    private void displayFragments() {
        String[] fragClasses = new String[]{"RedButtonFragment", "SwitchFragment", "BlueButtonFragment", "SeekBarFragment"};
        int[] fragLayouts = new int[]{R.id.fragment_container_1, R.id.fragment_container_2, R.id.fragment_container_3, R.id.fragment_container_5};
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

    private void display(String className, int frameLayout) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String fullClassName = "com.example.madgenius." + className;
        Class fragClass = Class.forName(fullClassName);
        Object fragment = fragClass.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayout, (Fragment) fragment).commit();
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
    public void onSwitch(Boolean val) { executeAction("SWITCH"); }
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
        executeAction("SEEK" + val);
    }

    private void setProximity() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ProximitySensor proximitySensor = new ProximitySensor(sensorManager);
        proximitySensor.setVariableChangeListener(isClose -> {
            if (isClose)
                executeAction("PROXIMITY");
        });
    }

    private void setUpsideDown() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        UpsideDownSensor upsideDownSensor = new UpsideDownSensor(sensorManager);
        upsideDownSensor.setVariableChangeListener(isUpsideDown -> {
            if (isUpsideDown)
                executeAction("UPSIDE");
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
                executeAction("SHAKE");
        });
    }

    private void executeAction(String code) {
        this.countdown.cancel();
        if(requiredAction == null || requiredAction.isEmpty())
            return;
        Log.d("ACTION", "required " + requiredAction + " action executed " + code);
        if(code.equals(requiredAction)){
            points++;
            getNewCommand();
        }
        else{
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
        requiredAction = "";
        scoreMessage();
    }

    private void scoreMessage(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameplayAgility.this);
        View messageView = getLayoutInflater().inflate(R.layout.dialog_save_score, null);
        EditText usernameEditText = messageView.findViewById(R.id.usernameEditText);
        TextView pointsMessage = messageView.findViewById(R.id.scoreTextView);
        pointsMessage.setText("You got " + points + " points!");

        Button saveButton = messageView.findViewById(R.id.saveButton);
        Button cancelButton = messageView.findViewById(R.id.cancelButton);

        mBuilder.setView(messageView);
        AlertDialog dialog = mBuilder.create();

        saveButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            if(username.isEmpty()){
                Toast.makeText(GameplayAgility.this, "Please fill your username", Toast.LENGTH_SHORT).show();
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
