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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

/**
 * Class that implements the main logic behind the Memory game mode.
 * Implements the listeners for all fragments used in the game.
 *
 * Controls the actions required to be done using queue of Strings (requiredActions), containing the
 *  codes of the actions that must be performed.
 *
 * Each step of this game has a fixed number of actions (numActions).
 * In the beginning of each step the method getNewCommands is invoked.
 *
 * Every step of the game has two phases, starting with a repetition of the actions the user must
 *  perform, followed by the testing phase, where the user must execute the learned actions.
 * The phase of the game is kept by "isUserRepeating". If true, the game is in the repetition phase,
 *  otherwise, the game is in the testing phase.
 */
public class GameplayMemory extends AppCompatActivity implements  RedButtonFragment.OnFragmentInteractionListener,
                                                                    SwitchFragment.OnFragmentInteractionListener,
                                                                    BlueButtonFragment.OnFragmentInteractionListener,
                                                                    SeekBarFragment.OnFragmentInteractionListener,
                                                                    YellowButtonFragment.OnFragmentInteractionListener,
                                                                    GreenButtonFragment.OnFragmentInteractionListener {
    private TextView pointTextView;
    private int numActions = 1;
    private final String FRAG_DISPLAY_TAG = "fragments_displayed", NUM_ACTIONS_TAG= "num_actions";
    private boolean fragmentsDisplayed;
    private Boolean isUserRepeating = false;
    private ProgressBar steps;
    private String[] commands = {"Press the red button", "Press the blue button", "Press the yellow button", "Press the green button", "Shake the phone", "Turn your phone upside down", "Tap the front of your phone", "Set bar to ", "Switch the toggle"};
    private String[] codes = {"RED", "BLUE", "YELLOW", "GREEN", "SENSOR_SHAKE", "SENSOR_UPSIDE", "SENSOR_PROXIMITY", "SEEK", "SWITCH"};
    String[] fragClasses = new String[]{"GreenButtonFragment", "YellowButtonFragment", "RedButtonFragment", "SwitchFragment", "BlueButtonFragment", "SeekBarFragment"};
    int[] fragLayouts = new int[]{R.id.fragment_container_1, R.id.fragment_container_2, R.id.fragment_container_3, R.id.fragment_container_4, R.id.fragment_container_8, R.id.fragment_container_5};
    private Queue<String> requiredActions, trainActions;
    private Queue<String> trainMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_memory);
        pointTextView = findViewById(R.id.pointTextView);
        fragmentsDisplayed = false;
        requiredActions = new ArrayDeque<String>();
        trainActions = new ArrayDeque<String>();
        trainMessages = new ArrayDeque<String>();

        if(savedInstanceState != null){
            fragmentsDisplayed = savedInstanceState.getBoolean(FRAG_DISPLAY_TAG);
            numActions = savedInstanceState.getInt(NUM_ACTIONS_TAG);
        }
        if(!fragmentsDisplayed) {
            numActions = 1;
            setPoints(0);
            shuffle(fragLayouts, fragLayouts.length-1);
            int[] longFragLayouts = new int[] {R.id.fragment_container_5, R.id.fragment_container_6, R.id.fragment_container_7};
            Random rand = new Random();
            fragLayouts[fragLayouts.length-1] = longFragLayouts[rand.nextInt(longFragLayouts.length)];
            displayFragments();
        }
        setProximity();
        setShaker();
        setUpsideDown();
        steps = findViewById(R.id.pgbSteps);
        steps.setMax(numActions);
        steps.setProgress(0);
        getNewCommands();
    }

    /**
     * Sets the points message during the game
      */
    private void setPoints(int p) {
        if(p == 1)
            pointTextView.setText("Point: "+p);
        else
            pointTextView.setText("Points: "+p);
    }


    /**
     * Shuffles a given array "values" from the position 0 until the position "maxPosition"
     * @param values
     * @param maxPosition
     */
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

    /**
     * Function that displays a given array of classes "className", on given FrameLayouts "frameLayouts".
     * Dinamically inserts each classe on its corresponding frame layout.
     * @param className
     * @param frameLayouts
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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


    /**
     * Resets UI, reinserting fragments and clearing the step bar.
     */
    private void resetUI() {
        displayFragments();
        steps.setMax(numActions);
    }

    private void teachCommands() {
        isUserRepeating = true;
        steps.setProgress(0);
        trainActions.addAll(requiredActions);
        stepLearn();
    }

    private void stepLearn() {
        steps.setProgress(numActions - trainActions.size());
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText(trainMessages.peek());

    }

    private void startGame() {
        resetUI();
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText("Now repeat");
        isUserRepeating = false;
        steps.setMax(numActions);
        steps.setProgress(0);
    }

    private void stepGame() {
        steps.setProgress(numActions - requiredActions.size());
    }

    private void getNewCommands() {
        Random rand = new Random();
        requiredActions.clear();
        trainMessages.clear();
        for(int i=0;i<numActions;i++){
            int randNum = rand.nextInt(this.commands.length);
            String requiredAction = codes[randNum];
            String actionMessage = commands[randNum];
            if(requiredAction == "SEEK"){ // Generating random number to set the seekbar.
                SeekBarFragment sbFrag = new SeekBarFragment();
                int previousValue = sbFrag.seekBarValue;
                final int SEEKBAR_MAX = 10;
                int randomVal;
                do {
                    randomVal = rand.nextInt(SEEKBAR_MAX + 1);
                } while (previousValue == randomVal); // Doesn't repeat the same seekbar value two times in a row.
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
        usernameEditText.setText(SavedInfo.getUsername(getApplicationContext()));
        TextView pointsMessage = messageView.findViewById(R.id.scoreTextView);
        CheckBox saveUsernameCheckBox = messageView.findViewById(R.id.saveUsernameCheckBox);
        int points = numActions-1;
        pointsMessage.setText("You memorized up to " + points + " instructions!");

        Button saveButton = messageView.findViewById(R.id.saveButton);
        Button cancelButton = messageView.findViewById(R.id.cancelButton);

        mBuilder.setView(messageView);
        AlertDialog dialog = mBuilder.create();

        //Makes user unable to click out of the alert dialog
        dialog.setCanceledOnTouchOutside(false);

        saveButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            if(username.isEmpty()){
                usernameEditText.setError("Please fill your username");
            }
            else {
                ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
                scoreViewModel.insert(new Score(username, points, true));
                if(saveUsernameCheckBox.isChecked())
                    SavedInfo.saveUsername(getApplicationContext(), username);
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
                if(code.contains("SENSOR"))
                    playSound(R.raw.correct);
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
                if(code.contains("SENSOR"))
                    playSound(R.raw.correct);
                if (requiredActions.isEmpty()) {
                    setPoints(numActions);
                    numActions++;
                    nextEpisodeFragment dialog = new nextEpisodeFragment();
                    dialog.setNumActions(numActions);
                    dialog.show(getSupportFragmentManager(), "test");
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
                executeAction("SENSOR_PROXIMITY");
        });
    }

    private void setUpsideDown() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        UpsideDownSensor upsideDownSensor = new UpsideDownSensor(sensorManager);
        upsideDownSensor.setVariableChangeListener(isUpsideDown -> {
            if(isUpsideDown)
                executeAction("SENSOR_UPSIDE");
        });
    }

    private void setShaker() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ShakeSensor shake = new ShakeSensor(sensorManager);
        shake.setVariableChangeListener(isShaking -> {
            if(isShaking)
                executeAction("SENSOR_SHAKE");
        });
    }


    public void playSound(int soundID){
        MediaPlayer.create(this, soundID).start();
    }

    @Override
    public void onSwitch(Boolean val) {
        playSound(R.raw.click);
        executeAction("SWITCH");
    }
    @Override
    public void onBlueButtonClick() {
        playSound(R.raw.click);
        executeAction("BLUE");
    }
    @Override
    public void onRedButtonClick() {
        playSound(R.raw.click);
        executeAction("RED");
    }

    @Override
    public void onGreenButtonClick() {
        playSound(R.raw.click);
        executeAction("GREEN");
    }

    @Override
    public void onYellowButtonClick() {
        playSound(R.raw.click);
        executeAction("YELLOW");
    }

    @Override
    public void onSeekBarUpdate(int val) {
        playSound(R.raw.click);
        executeAction("SEEK"+val);
    }

}
