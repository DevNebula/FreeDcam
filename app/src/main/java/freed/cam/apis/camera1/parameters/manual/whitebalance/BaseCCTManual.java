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

package freed.cam.apis.camera1.parameters.manual.whitebalance;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.util.Log;

import com.troop.freedcam.R;

import java.util.ArrayList;

import freed.cam.apis.KEYS;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera1.CameraHolder;
import freed.cam.apis.camera1.parameters.ParametersHandler;
import freed.cam.apis.camera1.parameters.manual.BaseManualParameter;

/**
 * Created by Ingo on 06.03.2016.
 */
public class BaseCCTManual extends BaseManualParameter
{
    private final String TAG = BaseCCTManual.class.getSimpleName();

    private final String manual_WbMode;


    public BaseCCTManual(final Parameters parameters,final CameraWrapperInterface cameraUiWrapper) {
        super(parameters, "", "", "", cameraUiWrapper, 0);
        manual_WbMode = cameraUiWrapper.GetAppSettingsManager().manualWhiteBalance.getMode();
        stringvalues = cameraUiWrapper.GetAppSettingsManager().manualWhiteBalance.getValues();
        isSupported = true;
        isVisible = false;

        //wait 800ms to give awb a chance to set the ct value to the parameters
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //get fresh parameters from camera
                Camera.Parameters parameters1 = ((CameraHolder)cameraUiWrapper.GetCameraHolder()).GetCameraParameters();
                String wbcur = "";
                //lookup if ct value is avail
                if (parameters1.get(cameraUiWrapper.getResString(R.string.wb_current_cct))!=null)
                    wbcur = cameraUiWrapper.getResString(R.string.wb_current_cct);
                else if (parameters1.get(cameraUiWrapper.getResString(R.string.wb_cct)) != null)
                    wbcur =cameraUiWrapper.getResString(R.string.wb_cct);
                else if (parameters1.get(cameraUiWrapper.getResString(R.string.wb_ct)) != null)
                    wbcur = cameraUiWrapper.getResString(R.string.wb_ct);
                else if (parameters1.get(cameraUiWrapper.getResString(R.string.wb_manual_cct)) != null)
                    wbcur = cameraUiWrapper.getResString(R.string.wb_manual_cct);
                else if (parameters1.get(cameraUiWrapper.getResString(R.string.manual_wb_value)) != null)
                    wbcur = cameraUiWrapper.getResString(R.string.manual_wb_value);
                if (wbcur != "")
                {
                    //update our stored parameters with ct
                    parameters.set(wbcur, parameters1.get(wbcur));
                    isSupported = true;
                    isVisible = true;
                    key_value = wbcur;
                    BaseCCTManual.this.ThrowBackgroundIsSupportedChanged(true);
                }
            }
        }, 800);
    }

    /**
     * @param parameters
     * @param value
     * @param maxValue
     * @param MinValue
     * @param cameraUiWrapper
     * @param step
     */
    public BaseCCTManual(Parameters parameters, String value, String maxValue, String MinValue
            , CameraWrapperInterface cameraUiWrapper, float step,
                         String wbmode) {
        super(parameters, value, maxValue, MinValue, cameraUiWrapper, step);
        manual_WbMode = wbmode;
    }


    @Override
    public void SetValue(int valueToSet) {
        currentInt = valueToSet;
        //set to auto
        if (currentInt == 0) {
            set_to_auto();
        } else //set manual wb mode and key_value
        {
            set_manual();
        }
        try {
            ((ParametersHandler) cameraUiWrapper.GetParameterHandler()).SetParametersToCamera(parameters);
        }
        catch (RuntimeException ex)
        {
            ThrowBackgroundIsSupportedChanged(false);
        }

    }

    protected void set_manual()
    {
        if (cameraUiWrapper.GetParameterHandler().WhiteBalanceMode.GetValues().toString().contains("manual")&& parameters.get("manual-wb-modes")!=null)
        {
            cameraUiWrapper.GetParameterHandler().WhiteBalanceMode.SetValue(manual_WbMode, true);
            parameters.set(cameraUiWrapper.getResString(R.string.manual_wb_type),0);
            parameters.set(cameraUiWrapper.getResString(R.string.manual_wb_value),stringvalues[currentInt]);

        }
        else {
            if (!cameraUiWrapper.GetParameterHandler().WhiteBalanceMode.GetValue().equals(manual_WbMode) && manual_WbMode != "")
                cameraUiWrapper.GetParameterHandler().WhiteBalanceMode.SetValue(manual_WbMode, true);
            parameters.set(key_value, stringvalues[currentInt]);
        }
        Log.d(TAG, "Set "+ key_value +" to : " + stringvalues[currentInt]);

    }

    protected void set_to_auto()
    {
        cameraUiWrapper.GetParameterHandler().WhiteBalanceMode.SetValue("auto", true);
        Log.d(TAG, "Set  to : auto");
    }

    @Override
    protected String[] createStringArray(int min, int max, float step)
    {
        ArrayList<String> t = new ArrayList<>();
        t.add(KEYS.AUTO);
        for (int i = min; i<=max;i+=step)
        {
            t.add(i+"");
        }
        stringvalues = new String[t.size()];
        t.toArray(stringvalues);
        return stringvalues;
    }
}
