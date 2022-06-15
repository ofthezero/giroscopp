package com.example.giroscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sysmanager;
    private Sensor sensor;
    private ImageView img;
    private TextView txt;
    private SensorEventListener sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txt);
        img = findViewById(R.id.img);

        sysmanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sysmanager != null)
            sensor = sysmanager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values
                );
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix
                );
                float[] orientations = new float[3];
                SensorManager.getOrientation(remappedRotationMatrix, orientations);
                for(int i = 0; i < 3; i++){
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }

                txt.setText("z: " + String.valueOf((int)-orientations[2]) + " y: " + String.valueOf((int)-orientations[0]) + " x: " + String.valueOf((int)-orientations[1]));

                img.setRotation(-orientations[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume(){
        super.onResume();
        sysmanager.registerListener(sv, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sysmanager.unregisterListener(sv);
    }
}