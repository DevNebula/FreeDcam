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

package freed.cam.apis.camera2.parameters.manual;

import android.annotation.TargetApi;
import android.hardware.camera2.CaptureRequest;
import android.os.Build.VERSION_CODES;
import android.util.Log;

import com.troop.freedcam.R;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.parameters.manual.AbstractManualParameter;
import freed.cam.apis.camera2.CameraHolderApi2;
import freed.utils.StringFloatArray;

/**
 * Created by troop on 28.04.2015.
 */
@TargetApi(VERSION_CODES.LOLLIPOP)
public class ManualFocus extends AbstractManualParameter
{
    private final String TAG = ManualFocus.class.getSimpleName();
    private StringFloatArray focusvalues;

    public ManualFocus(CameraWrapperInterface cameraUiWrapper)
    {
        super(cameraUiWrapper);
        if (cameraUiWrapper.GetAppSettingsManager().manualFocus.isSupported())
        {
            String[] tmplist = cameraUiWrapper.GetAppSettingsManager().manualFocus.getValues();
            isSupported = true;
            focusvalues = new StringFloatArray(tmplist.length);
            int i = 0;
            for (String s :tmplist)
            {
                String[] split = s.split(";");
                focusvalues.add(i++,split[0], Float.parseFloat(split[1]));
            }
            currentInt = 0;
        }

    }

    @Override
    public int GetValue() {
        return currentInt;
    }

    @Override
    public String GetStringValue()
    {
        return focusvalues.getKey(currentInt);
    }


    @Override
    public void SetValue(int valueToSet)
    {
        currentInt = valueToSet;
        if(valueToSet == 0)
        {
            cameraUiWrapper.GetParameterHandler().FocusMode.SetValue(cameraUiWrapper.getContext().getString(R.string.auto), true);
        }
        else
        {
            if (!cameraUiWrapper.GetParameterHandler().FocusMode.GetValue().equals(cameraUiWrapper.getContext().getString(R.string.off)))
            {
                ((CameraHolderApi2) cameraUiWrapper.GetCameraHolder()).SetParameterRepeating(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
                cameraUiWrapper.GetParameterHandler().FocusMode.SetValue(cameraUiWrapper.getContext().getString(R.string.off), true);
            }
            float valtoset= focusvalues.getValue(currentInt);
            Log.d(TAG, "Set MF TO: " + valtoset+ " ValueTOSET: " + valueToSet);
            ((CameraHolderApi2) cameraUiWrapper.GetCameraHolder()).SetParameterRepeating(CaptureRequest.LENS_FOCUS_DISTANCE, valtoset);
        }
    }


    @Override
    public boolean IsSupported()
    {
        return isSupported;
    }

    @Override
    public boolean IsVisible() {
        return isSupported;
    }

    @Override
    public boolean IsSetSupported() {
        return true;
    }

    @Override
    public String[] getStringValues() {
        return focusvalues.getKeys();
    }
}
