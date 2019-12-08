package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ThreadLocalRandom;

public class GameplayMemory extends AppCompatActivity {
    private int numActions = 2;
    private int listenningActions = numActions;
    private int i = 0;
    private Boolean isUserRepeating = false;
    private String[] commands = {"Press the red button", "Press the yellow button", "Shake the phone", "Turn your phone upside down"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_memory);
        getNewCommand();
    }

    public void nextOnClick(View view){
        getNewCommand();
    }

    private void getNewCommand() {
        TextView commandDisplay = findViewById(R.id.txtCommands);
        if (this.i >= this.numActions && !this.isUserRepeating) {
            this.isUserRepeating = true;
            commandDisplay.setText("Now repeat");
            i = 0;
            listenningActions = numActions;
            numActions++;
        } else {
            int randomNum = ThreadLocalRandom.current().nextInt(0, this.commands.length);
            commandDisplay.setText(this.commands[randomNum]);
            i++;
        }

    }

}
