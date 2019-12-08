package com.example.madgenius;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

/** Class that retrieves shaking events.
 *
 */
public class ShakeSensor implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lstUpdate;
    private float[] lstAcc = new float[3];
    private static final float SHAKE_THRESHOLD_GRAVITY = 4.0F;
    private Context context = null;
    public boolean isShaking = false;


    public ShakeSensor(SensorManager systemService){
        lstUpdate = 0;
        for(int i=0;i<3;i++)
            lstAcc[i] = 0;
        sensorManager = systemService;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public ShakeSensor(Context ctxt, SensorManager systemService){
        this(systemService);
        context = ctxt;
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
                if(context != null)
                    Toast.makeText(context, "shake detected w/ gForce: " + gForce, Toast.LENGTH_SHORT).show();
                setValue(true);
            }
            else
                setValue(false);
            for(int axis=0;axis<3;axis++)
                lstAcc[axis] = acc[axis];
            lstUpdate = curTime;
        }
    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }
}
