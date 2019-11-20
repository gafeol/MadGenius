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
        setShaker();
    }

    /** Function that sets up a shaker listener.
     *
     */
    private void setShaker() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Shake shake = new Shake(getBaseContext(), sensorManager);

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
