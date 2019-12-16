package com.example.madgenius;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Sensor class to check for the shake event.
 * The sensor analysed here is the accelerometer.
 * The "shake event" was defined as detecting a resulting acceleration of more than 3.5 the gravity of earth.
 * This threshold was defined empirically.
 * A VariableChangeListener interface was created so that other classes can implement any action
 *  desired when the sensor value "isShaking" is changed.
 */
public class ShakeSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lstUpdate;
    private static final float SHAKE_THRESHOLD_GRAVITY = 3.5F;
    public boolean isShaking = false;


    public ShakeSensor(SensorManager systemService){
        lstUpdate = 0;
        sensorManager = systemService;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void setValue( boolean value ) {
        if (value != isShaking) {
            isShaking = value;
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
            variableChangeListener.onVariableChanged(isShaking);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lstUpdate) > 200) {
            float[] acc = new float[3];

            for(int axis=0;axis<3;axis++)
                acc[axis] = event.values[axis]/SensorManager.GRAVITY_EARTH;
            float gForce = (float)Math.sqrt(acc[0] * acc[0] + acc[1] * acc[1] + acc[2] * acc[2]);

            if(gForce > SHAKE_THRESHOLD_GRAVITY){
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
