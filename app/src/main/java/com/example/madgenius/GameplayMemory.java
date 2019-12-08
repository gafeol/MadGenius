package com.example.madgenius;

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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class GameplayMemory extends AppCompatActivity implements  RedButtonFragment.OnFragmentInteractionListener,
                                                                    SwitchFragment.OnFragmentInteractionListener,
                                                                    BlueButtonFragment.OnFragmentInteractionListener,
                                                                    SeekBarFragment.OnFragmentInteractionListener {
    private int numActions = 1;
    private final String FRAG_DISPLAY_TAG = "fragments_displayed";
    private boolean fragmentsDisplayed;
    private Boolean isUserRepeating = false;
    private ProgressBar steps;
    private String[] commands = {"Press the red button", "Press the blue button", "Shake the phone", "Turn your phone upside down", "Tap the front of your phone", "Set bar to ", "Switch the toggle"};
    private String[] codes = {"RED", "BLUE", "SHAKE", "UPSIDE", "PROXIMITY", "SEEK", "SWITCH"};
    String[] fragClasses = new String[]{"RedButtonFragment", "SwitchFragment", "BlueButtonFragment", "SeekBarFragment"};
    int[] fragLayouts = new int[]{R.id.fragment_container_1, R.id.fragment_container_2, R.id.fragment_container_3, R.id.fragment_container_5};
    private Queue<String> requiredActions, trainActions;
    private Queue<String> trainMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_memory);
        fragmentsDisplayed = false;
        requiredActions = new ArrayDeque<String>();
        trainActions = new ArrayDeque<String>();
        trainMessages = new ArrayDeque<String>();

        if(savedInstanceState != null){
            fragmentsDisplayed = savedInstanceState.getBoolean(FRAG_DISPLAY_TAG);
        }
        if(!fragmentsDisplayed) {
            shuffle(fragLayouts, fragLayouts.length-1);
            displayFragments();
        }
        setProximity();
        setShaker();
        setUpsideDown();
        steps = findViewById(R.id.pgbSteps);
        steps.setMax(numActions);
        steps.setProgress(numActions);
        getNewCommands(); // Talvez isso tambem tenha que ser verificado no saveInstanceState
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


    private void displayFragments(){
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

    public void display(String[] className, int[] frameLayouts) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for(int i=0;i<className.length;i++){
            String fullClassName = "com.example.madgenius."+className[i];
            Class fragClass = Class.forName(fullClassName);
            Object fragment = fragClass.newInstance();
            fragmentTransaction.replace(frameLayouts[i], (Fragment)fragment);
        }
        fragmentTransaction.commit();
    }

    private void resetUI() {
        displayFragments();
        steps.setMax(numActions);
    }

    private void teachCommands() {
        isUserRepeating = true;
        steps.setProgress(numActions);
        trainActions.addAll(requiredActions);
        stepLearn();
    }

    private void stepLearn() {
        steps.setProgress(trainActions.size());
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText(trainMessages.peek());

    }

    private void startGame() {
        resetUI();
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText("Now repeat");
        isUserRepeating = false;
        steps.setMax(numActions);
        steps.setProgress(numActions);
    }

    private void stepGame() {
        steps.setProgress(requiredActions.size());
    }

    private void getNewCommands() {
        Random rand = new Random();
        requiredActions.clear();
        trainMessages.clear();
        for(int i=0;i<numActions;i++){
            int randNum = rand.nextInt(this.commands.length);
            String requiredAction = codes[randNum];
            String actionMessage = commands[randNum];
            if(requiredAction == "SEEK"){
                final int SEEKBAR_MAX = 10;
                int randomVal = rand.nextInt(SEEKBAR_MAX+1);
                requiredAction += randomVal;
                actionMessage += randomVal;
            }
            requiredActions.add(requiredAction);
            trainMessages.add(actionMessage);
        }
        teachCommands();
    }

    private void finishGame(){
        requiredActions.clear();
        scoreMessage();
    }

    private void scoreMessage(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(GameplayMemory.this);
        View messageView = getLayoutInflater().inflate(R.layout.dialog_save_score, null);
        EditText usernameEditText = messageView.findViewById(R.id.usernameEditText);
        TextView pointsMessage = messageView.findViewById(R.id.scoreTextView);
        int points = numActions-1;
        pointsMessage.setText("You memorized up to " + points + " instructions!");

        Button saveButton = messageView.findViewById(R.id.saveButton);
        Button cancelButton = messageView.findViewById(R.id.cancelButton);

        mBuilder.setView(messageView);
        AlertDialog dialog = mBuilder.create();

        saveButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            if(username.isEmpty()){
                Toast.makeText(GameplayMemory.this, "Please fill your username", Toast.LENGTH_SHORT).show();
            }
            else {
                ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
                scoreViewModel.insert(new Score(username, points, true));
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

    private void executeAction(String code){
        if(isUserRepeating){
            if(trainActions.isEmpty())
                return;
            if (code.equals(trainActions.peek())) {
                trainActions.remove();
                trainMessages.remove();
                if(trainActions.isEmpty())
                    startGame();
                else
                    stepLearn();
            } else {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(200);
                }
            }
        }
        else {
            Log.d("ACTION", "required " + requiredActions.peek() + " action executed " + code);
            if(requiredActions.isEmpty())
                return;
            if (code.equals(requiredActions.peek())) {
                requiredActions.remove();
                if (requiredActions.isEmpty()) {
                    numActions++;
                    resetUI();
                    getNewCommands();
                }
                else
                    stepGame();
            } else {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(200);
                }
                finishGame();
            }
        }
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

    private void setShaker() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeSensor shake = new ShakeSensor(sensorManager);
        shake.setVariableChangeListener(isShaking -> {
            if(isShaking)
                executeAction("SHAKE");
        });
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
    public void onSeekBarUpdate(int val) {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.click);
        mp.start();
        executeAction("SEEK"+val);
    }

}
