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
        setProximity();
    }

    private void setProximity() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ProximitySensor proximitySensor = new ProximitySensor(sensorManager);
        final TextView textView = findViewById(R.id.shakeText);
        proximitySensor.setVariableChangeListener(new ProximitySensor.VariableChangeListener() {
            @Override
            public void onVariableChanged(boolean isClose){
                if(isClose)
                    textView.setText("CLOSE");
                else
                    textView.setText("NOP");
            }
        });
    }

    private void setUpsideDown() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        UpsideDownSensor upsideDownSensor = new UpsideDownSensor(sensorManager);
        final TextView textView = findViewById(R.id.shakeText);
        upsideDownSensor.setVariableChangeListener(new UpsideDownSensor.VariableChangeListener() {
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
        ShakeSensor shake = new ShakeSensor(sensorManager);
        // Passando o contexto atual (getBaseContext()) permite que a classe ShakeSensor crie toasts
        //ShakeSensor shake = new ShakeSensor(getBaseContext(), sensorManager);

        // Defining the action wanted when shaking is detected.
        final TextView textView = findViewById(R.id.shakeText);
        shake.setVariableChangeListener(new ShakeSensor.VariableChangeListener() {
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
