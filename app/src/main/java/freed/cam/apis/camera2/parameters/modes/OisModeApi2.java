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

package freed.cam.apis.camera2.parameters.modes;

import android.annotation.TargetApi;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.os.Build.VERSION_CODES;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera2.CameraHolderApi2;

/**
 * Created by troop on 23.04.2016.
 */
@TargetApi(VERSION_CODES.LOLLIPOP)
public class OisModeApi2 extends BaseModeApi2
{
    public enum OISModes
    {
        off,
        on,
    }

    public OisModeApi2(CameraWrapperInterface cameraUiWrapper) {
        super(cameraUiWrapper);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    @Override
    public boolean IsSupported()
    {
        return ((CameraHolderApi2) cameraUiWrapper.GetCameraHolder()).characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION ) != null;
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCamera)
    {
        if (valueToSet.contains("unknown Focus"))
            return;
        OISModes sceneModes = Enum.valueOf(OISModes.class, valueToSet);
        ((CameraHolderApi2) cameraUiWrapper.GetCameraHolder()).SetParameterRepeating(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE, sceneModes.ordinal());
        onValueHasChanged(valueToSet);
        //cameraHolder.mPreviewRequestBuilder.build();
    }

    @Override
    public String GetValue()
    {
        //workaround for oems lazyness seen on Hibook
        //it returns not null from characteristics but the call to the CaptureRequest returns null.
        try {
            int i = ((CameraHolderApi2) cameraUiWrapper.GetCameraHolder()).get(CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE);
            OISModes sceneModes = OISModes.values()[i];
            return sceneModes.toString();
        }
        catch (NullPointerException ex)
        {
            return  OISModes.off.toString();
        }



    }

    @Override
    public String[] GetValues()
    {
        int[] values = ((CameraHolderApi2) cameraUiWrapper.GetCameraHolder()).characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION );
        String[] retvals = new String[values.length];
        for (int i = 0; i < values.length; i++)
        {
            try {
                OISModes sceneModes = OISModes.values()[values[i]];
                retvals[i] = sceneModes.toString();
            }
            catch (Exception ex)
            {
                retvals[i] = "unknown Ois mode" + values[i];
            }

        }
        return retvals;
    }
}
