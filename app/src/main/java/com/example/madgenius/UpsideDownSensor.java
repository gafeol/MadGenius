package com.example.madgenius;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import static java.lang.Math.abs;

/**
 * Sensor class to check for the Upside Down event.
 * The sensor analysed here is the accelerometer.
 * The "upside down event" was defined as detecting an acceleration on the y axis of less than -0.8
 *  of the gravity acceleration, and absolute accelerations of less than 0.7 on the x and z axis.
 *
 * These thresholds were defined empirically.
 * A VariableChangeListener interface was created so that other classes can implement any action
 *  desired when the sensor value "isUpsideDown "isClose"" is changed.
 */
public class UpsideDownSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lstUpdate;
    public boolean isUpsideDown = false;


    public UpsideDownSensor(SensorManager systemService){
        lstUpdate = 0;
        sensorManager = systemService;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
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
        if ((curTime - lstUpdate) > 200) {
            float x = event.values[0]/SensorManager.GRAVITY_EARTH;
            float y = event.values[1]/SensorManager.GRAVITY_EARTH;
            float z = event.values[2]/SensorManager.GRAVITY_EARTH;
            // Uses the values of x and z to differentiate between shaking and upside down
            if(y < -0.80 && abs(x) < 0.7 && abs(z) < 0.7) {
                setValue(true);
            }
            else
                setValue(false);
            lstUpdate = curTime;
        }
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
