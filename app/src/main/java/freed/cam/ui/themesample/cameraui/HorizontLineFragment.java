/*
 *
 *     Copyright (C) 2015 Ingo Fuchs
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * /
 */

package freed.cam.ui.themesample.cameraui;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.troop.freedcam.R;
import com.troop.freedcam.R.id;
import com.troop.freedcam.R.layout;

import freed.ActivityInterface;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.parameters.modes.AbstractModeParameter.I_ModeParameterEvent;
import freed.cam.ui.themesample.AbstractFragment;
import freed.utils.AppSettingsManager;

/**
 * Created by Ar4eR on 15.01.16.
 */
public class HorizontLineFragment extends AbstractFragment implements I_ModeParameterEvent{

    private View view;

    private ImageView lineImage;
    private ImageView upImage;
    private ImageView downImage;
    private float RotateDegree;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private float roll;
    private float pitch;
    private float rolldegree;
    private float pitchdegree;
    private final float rad2deg = (float)(180.0f/Math.PI);
    private final Handler handler = new Handler();
    private Handler sensorHandler;
    private final MySensorListener msl =new MySensorListener();
    private CompassDrawer compassDrawer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,null);
        fragment_activityInterface = (ActivityInterface)getActivity();
        view = inflater.inflate(layout.cameraui_horizontline, container, false);
        lineImage = (ImageView) view.findViewById(id.horizontlevelline);
        upImage = (ImageView) view.findViewById(id.horizontlevelup);
        downImage = (ImageView) view.findViewById(id.horizontleveldown);
        upImage.setVisibility(View.GONE);
        downImage.setVisibility(View.GONE);
        HandlerThread sensorThread = new HandlerThread("Sensor thread", Thread.MAX_PRIORITY);
        sensorThread.start();
        sensorHandler = new Handler(sensorThread.getLooper());
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        compassDrawer = (CompassDrawer)view.findViewById(id.view_compass);

        return view;
    }

    @Override
    public void onParameterValueChanged(String val) {
        if(fragment_activityInterface.getAppSettings().getString(AppSettingsManager.SETTING_HORIZONT).equals("On"))
        {
            startSensorListing();
            view.setVisibility(View.VISIBLE);
        }
        else
        {
            stopSensorListing();
            view.setVisibility(View.GONE);
        }

    }

    @Override
    public void onParameterIsSupportedChanged(boolean isSupported) {

    }

    @Override
    public void onParameterIsSetSupportedChanged(boolean isSupported) {

    }

    @Override
    public void onParameterValuesChanged(String[] values) {

    }

    @Override
    public void onVisibilityChanged(boolean visible) {

    }

    public void setCameraUiWrapper(CameraWrapperInterface cameraUiWrapper)
    {
        this.cameraUiWrapper = cameraUiWrapper;
        cameraUiWrapper.GetParameterHandler().Horizont.addEventListner(this);
    }
    private void startSensorListing()
    {
        if (fragment_activityInterface.getAppSettings().getString(AppSettingsManager.SETTING_HORIZONT).equals("On")) {
            sensorManager.registerListener(msl, accelerometer, 1000000, sensorHandler);
            sensorManager.registerListener(msl, magnetometer, 1000000, sensorHandler);
        }
    }

    private void stopSensorListing()
    {
        if (sensorManager != null)
            sensorManager.unregisterListener(msl);

    }
    @Override
    public void onPause(){
        super.onPause();
        stopSensorListing();
    }
    @Override
    public void onResume(){
        super.onResume();
        if (fragment_activityInterface.getAppSettings().getString(AppSettingsManager.SETTING_HORIZONT).equals("Off") || fragment_activityInterface.getAppSettings().getString(AppSettingsManager.SETTING_HORIZONT).equals(""))
            view.setVisibility(View.GONE);
        else
            startSensorListing();
    }

    private class MySensorListener implements SensorEventListener {

        static final float ALPHA = 0.2f;

        float[] lowPass(float[] input, float[] output) {
            if ( output == null ) return input;

            for ( int i=0; i<input.length; i++ ) {
                output[i] = input[i] * ALPHA + output[i] * (1.0f - ALPHA);
                //output[i] = output[i] + ALPHA * (input[i] - output[i]);
            }
            return output;
        }

        public void onAccuracyChanged (Sensor sensor, int accuracy) {}

        public void onSensorChanged(final SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = lowPass(event.values.clone(), mGravity);
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values.clone();
            if (mGravity != null && mGeomagnetic != null) {
                //hltheard.run();
                float[] R = new float[9];
                float[] I = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    //SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);
                    float[] orientation = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    roll = orientation[1];
                    pitch = orientation[2];
                    rolldegree = roll * rad2deg;
                    pitchdegree = pitch * rad2deg;
                    float or = ((float)Math.toDegrees(orientation[0])+360 +90)%360;
                    compassDrawer.SetPosition(or);
                   // Logger.d("Sometag", String.valueOf(pitchdegree));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        lineImage.setRotation(((float)Math.toDegrees(roll)+360)%360);
                        /*if (RotateDegree != rolldegree) {

                            RotateAnimation rotateAnimation = new RotateAnimation(RotateDegree, rolldegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            //rotateAnimation.setInterpolator(lineImage.getContext(), android.R.interpolator.accelerate_decelerate);
                            rotateAnimation.setFillAfter(true);
                            lineImage.startAnimation(rotateAnimation);

                            RotateDegree = rolldegree;
                        }*/
                        if (pitchdegree > -89) {
                            if(upImage.getVisibility() != View.VISIBLE)
                                upImage.setVisibility(View.VISIBLE);
                            downImage.setVisibility(View.GONE);
                        }
                        else if (pitchdegree < -91) {
                            upImage.setVisibility(View.GONE);
                            if(downImage.getVisibility() != View.VISIBLE)
                                downImage.setVisibility(View.VISIBLE);
                        }
                        else if (pitchdegree >= -91 && pitchdegree <= -89) {
                            upImage.setVisibility(View.GONE);
                            downImage.setVisibility(View.GONE);
                        }
                    }
                });


            }
        }
    }
}
