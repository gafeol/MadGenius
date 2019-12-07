package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class GameplayAgility extends AppCompatActivity implements RedButtonFragment.OnFragmentInteractionListener,
                                                                    SwitchFragment.OnFragmentInteractionListener,
                                                                    BlueButtonFragment.OnFragmentInteractionListener,
                                                                    SeekBarFragment.OnFragmentInteractionListener {
    private Boolean gameStatus = true;
    private String[] commands = {"Press the red button", "Press the yellow button", "Shake the phone", "Turn your phone upside down"};
    private int[] times = {3, 4, 5, 8};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_agility);

        displayFragments();
        getNewCommand();
        Context context = getApplicationContext();
        CharSequence text = "It's really asyncronous!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void getNewCommand(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, this.commands.length);
        TextView commandDisplay = findViewById(R.id.txtCommands);
        commandDisplay.setText(this.commands[randomNum]);

        ProgressBar time = findViewById(R.id.pgbTime);

        int maxTime = this.times[randomNum];
        time.setMax(maxTime);
        time.setProgress(maxTime);
        timeController clock = new timeController(maxTime);

        clock.setTimeIncrementListener(new timeController.ProgressBarListener() {
            @Override
            public void onTimeIncrement() {
                time.setProgress(time.getProgress()-1);
            }
            @Override
            public void onTimeUp(){
                Context context = getApplicationContext();
                CharSequence text = "Time's up!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
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
        Toast.makeText(this, "Switchou  com "+val, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBlueButtonClick() {
        Toast.makeText(this, "Clicou no azul!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRedButtonClick() {
        Toast.makeText(this, "Clicou no vermelho!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSeekBarUpdate(int val) {
        Toast.makeText(this, "Seek bar updated to "+val, Toast.LENGTH_SHORT).show();
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
