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

package freed.cam.apis.sonyremote.parameters.manual;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.sonyremote.parameters.ParameterHandler;
import freed.utils.FreeDPool;

/**
 * Created by troop on 03.01.2015.
 */
public class ExposureCompManualParameterSony extends BaseManualParameterSony
{
    private final String TAG = ExposureCompManualParameterSony.class.getSimpleName();
    public ExposureCompManualParameterSony(CameraWrapperInterface cameraUiWrapper) {
        super("getExposureCompensation", "getAvailableExposureCompensation", "setExposureCompensation", cameraUiWrapper);
        currentInt = -200;
    }

    @Override
    public void SetValue(final int valueToSet)
    {
        currentInt = valueToSet;
        FreeDPool.Execute(new Runnable() {
            @Override
            public void run() {
                //String val = valueToSet +"";
                JSONArray array = null;
                if (stringvalues == null)
                {
                    getMinMaxValues();
                    return;
                }
                try {
                    int toset;
                    Log.d(TAG, "SetValue " + valueToSet);
                    if (valueToSet > stringvalues.length)
                        toset = stringvalues.length -1;
                    else
                        toset = valueToSet;
                    array = new JSONArray().put(0, Integer.parseInt(stringvalues[toset]));
                    JSONObject object =  ((ParameterHandler) cameraUiWrapper.GetParameterHandler()).mRemoteApi.setParameterToCamera(VALUE_TO_SET, array);

                        //ThrowCurrentValueChanged(valueToSet);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    Log.e(TAG, "Error SetValue " + valueToSet);
                } catch (IOException ex)
                {
                    Log.e(TAG, "Error SetValue " + valueToSet);
                    ex.printStackTrace();
                }
            }
        });
    }

    private void getMinMaxValues()
    {
        if (stringvalues == null)
        {
            FreeDPool.Execute(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        Log.d(TAG, "try get min max values ");
                        JSONObject object =  ((ParameterHandler) cameraUiWrapper.GetParameterHandler()).mRemoteApi.getParameterFromCamera(VALUES_TO_GET);
                        JSONArray array = object.getJSONArray("result");
                        int min = array.getInt(2);
                        int max = array.getInt(1);
                        stringvalues = createStringArray(min,max,1);
                    } catch (IOException | JSONException ex)
                    {

                        Log.e(TAG, "Error getMinMaxValues ");
                        ex.printStackTrace();

                    }
                }
            });
        }
    }

    @Override
    public int GetValue()
    {
        if (currentInt == -100) {
            FreeDPool.Execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = mRemoteApi.getParameterFromCamera(VALUE_TO_GET);
                        JSONArray array = object.getJSONArray("result");
                        currentInt = array.getInt(0);
                        //onCurrentValueChanged(val);
                    } catch (IOException | JSONException ex) {
                        ex.printStackTrace();
                        Log.e(TAG, "Error GetValue() ");

                    }
                }
            });
        }
        return currentInt;
    }


    @Override
    public void onCurrentValueChanged(int current) {
        currentInt = current;
    }

    public String[] getStringValues()
    {
        if (stringvalues == null)
            getMinMaxValues();
        return stringvalues;
    }

    @Override
    public String GetStringValue()
    {
        if (stringvalues != null && currentInt != -100)
            return stringvalues[currentInt];
        return 0+"";
    }
}
