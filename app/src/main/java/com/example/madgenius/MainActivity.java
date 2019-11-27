package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the shake detector
        setShaker();
        setUpsideDown();
    }

    private void setUpsideDown() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        UpsideDown upsideDown = new UpsideDown(sensorManager);
        final TextView textView = findViewById(R.id.shakeText);
        upsideDown.setVariableChangeListener(new UpsideDown.VariableChangeListener() {
            @Override
            public void onVariableChanged(boolean isUpsideDown) {
                if (isUpsideDown)
                    textView.setText("UPSIDE!");
                else
                    textView.setText("NOP");
            }
        });
    }
    /** Function that sets up a shaker listener.
     */
    private void setShaker() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Shake shake = new Shake(sensorManager);
        // Passando o contexto atual (getBaseContext()) permite que a classe Shake crie toasts
        //Shake shake = new Shake(getBaseContext(), sensorManager);

        // Defining the action wanted when shaking is detected.
        final TextView textView = findViewById(R.id.shakeText);
        shake.setVariableChangeListener(new Shake.VariableChangeListener() {
            @Override
            public void onVariableChanged(boolean isShaking) {
                if (isShaking)
                    textView.setText("SHAKING!");
                else
                    textView.setText("NOP");
            }
        });
    }
}
