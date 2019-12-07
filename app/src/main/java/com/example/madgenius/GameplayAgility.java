package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.madgenius.BlueButtonFragment;

public class GameplayAgility extends AppCompatActivity implements RedButtonFragment.OnFragmentInteractionListener,
                                                                    SwitchFragment.OnFragmentInteractionListener,
                                                                    BlueButtonFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_agility);
        displayRedButtonFragment();
        displaySwitchFragment();
        try {
            display("BlueButtonFragment");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ProgressBar time = findViewById(R.id.pgb_time);

        time.setMax(30);
        timeController clock = new timeController(30);
        clock.setTimeIncrementListener(new timeController.ProgressBarListener() {
            @Override
            public void onTimeIncrement() {
                time.setProgress(time.getProgress()-1);
            }
            @Override
            public void onTimeUp(){
                Context context = getApplicationContext();
                CharSequence text = "Time's up";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        clock.init();
    }

    public void display(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String fullClassName = "com.example.madgenius."+className;
        Class fragClass = Class.forName(fullClassName);
        Object fragment = fragClass.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_3, (Fragment)fragment).addToBackStack(null).commit();
    }

    public void displayRedButtonFragment() {
        SwitchFragment switchFragment = SwitchFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_switch_container, switchFragment)
                .addToBackStack(null).commit();
    }

    public void displaySwitchFragment() {
        RedButtonFragment redButtonFragment = RedButtonFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, redButtonFragment)
                .addToBackStack(null).commit();
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
