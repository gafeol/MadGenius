package com.example.madgenius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

public class GameplayAgility extends AppCompatActivity implements RedButtonFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_agility);
        displayFragment();
    }

    public void displayFragment() {
        RedButtonFragment redButtonFragment = RedButtonFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, redButtonFragment)
                .addToBackStack(null).commit();
    }

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

    @Override
    public void onButtonClick() {
        Toast.makeText(this, "Clicou no vermelho!", Toast.LENGTH_SHORT).show();
    }
}
