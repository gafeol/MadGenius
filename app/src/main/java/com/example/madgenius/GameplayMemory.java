package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class GameplayMemory extends AppCompatActivity {
    private int numActions = 2;
    private int i = 0;
    private Boolean isUserRepeating = false;
    private ProgressBar steps;
    private String[] commands = {"Press the red button", "Press the yellow button", "Shake the phone", "Turn your phone upside down"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay_memory);
        steps = findViewById(R.id.pgbSteps);
        steps.setMax(numActions);
        steps.setProgress(numActions);
        getNewCommand();
    }

    public void nextOnClick(View view){
        steps.setProgress(steps.getProgress()-1);
        i++;
        getNewCommand();
    }

    private void getNewCommand() {

        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

        Toast toast;

        TextView commandDisplay = findViewById(R.id.txtCommands);

        if(this.isUserRepeating){
            if (this.i >= this.numActions){
                // checa se os inputs do usuario estao corretos de acordo com o gabarito
                this.isUserRepeating = false;
                i = 0;
                numActions++;
                steps.setMax(numActions);
                steps.setProgress(numActions);

                toast = Toast.makeText(context, "CHECANDO SEQUENCIA... \n. i: " + i +" numactions: " +numActions , duration);
                toast.show();
            }
            else{
                // empilha inputs do usuario para depois conferir...
                toast = Toast.makeText(context, "Empilhando...\n. i: " + i +" numactions: " +numActions , duration);
                toast.show();
            }
        } else {
            if (this.i >= this.numActions){
                this.isUserRepeating = true;
                commandDisplay.setText("Now repeat");
                i = 0;
                steps.setProgress(numActions);

                toast = Toast.makeText(context, "i: " + i +" numactions: " +numActions , duration);
                toast.show();
            }
            else{
                int randomNum = ThreadLocalRandom.current().nextInt(0, this.commands.length);
                commandDisplay.setText(this.commands[randomNum]);

                toast = Toast.makeText(context, "i: " + i +" numactions: " +numActions , duration);
                toast.show();

            }
        }
    }

}
