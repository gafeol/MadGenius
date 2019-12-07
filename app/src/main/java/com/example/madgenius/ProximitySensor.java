package com.example.madgenius;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ProximitySensor  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean isClose = false;
    private static final int SENSOR_SENSITIVITY = 4;

    public ProximitySensor(SensorManager systemService){
        sensorManager = systemService;
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void setValue( boolean value ) {
        if(value != isClose) {
            isClose = value;
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
        if(variableChangeListener != null)
            variableChangeListener.onVariableChanged(isClose);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.d("prox", "Sensor value " + event.values[0]);
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                setValue(true);

            } else {
                //far
                setValue(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
