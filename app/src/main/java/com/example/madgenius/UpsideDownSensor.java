package com.example.madgenius;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import static java.lang.Math.abs;

public class UpsideDownSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lstUpdate;
    private Context context = null;
    public boolean isUpsideDown = false;


    public UpsideDownSensor(SensorManager systemService){
        lstUpdate = 0;
        sensorManager = systemService;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public UpsideDownSensor(Context ctxt, SensorManager systemService){
        this(systemService);
        context = ctxt;
    }

    public void setValue( boolean value ) {
        if (value != isUpsideDown) {
            isUpsideDown = value;
            signalChanged();
        }
    }

    public interface VariableChangeListener {
        void onVariableChanged(boolean variableThatHasChanged);
    }

    private VariableChangeListener variableChangeListener;
    public void setVariableChangeListener(VariableChangeListener variableChangeListener) {
        this.variableChangeListener = variableChangeListener;
    }

    private void signalChanged() {
        if (variableChangeListener != null)
            variableChangeListener.onVariableChanged(isUpsideDown);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lstUpdate) > 500) {
            float x = event.values[0]/SensorManager.GRAVITY_EARTH;
            float y = event.values[1]/SensorManager.GRAVITY_EARTH;
            float z = event.values[2]/SensorManager.GRAVITY_EARTH;
            // Uses the values of x and z to differentiate between shaking and upside down
            if(y < -0.80 && abs(x) < 0.7 && abs(z) < 0.7) {
                if (context != null)
                    Toast.makeText(context, "Upside down with y "+y, Toast.LENGTH_SHORT).show();
                setValue(true);
            }
            else
                setValue(false);
            lstUpdate = curTime;
        }
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }
}
