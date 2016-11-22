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

package freed.cam.apis.camera1.parameters.manual;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera.Parameters;

import freed.cam.apis.KEYS;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.modules.ModuleChangedEvent;
import freed.cam.apis.basecamera.parameters.manual.AbstractManualParameter;
import freed.cam.apis.basecamera.parameters.modes.AbstractModeParameter.I_ModeParameterEvent;
import freed.cam.apis.camera1.parameters.ParametersHandler;
import freed.cam.apis.camera1.parameters.modes.PictureFormatHandler;
import freed.utils.Logger;

/**
 * Created by troop on 17.08.2014.
 */
public class BaseManualParameter extends AbstractManualParameter
{

    private final String TAG = BaseManualParameter.class.getSimpleName();
    /**
     * Holds the list of Supported parameters
     */
    protected Parameters  parameters;
    /*
     * The name of the current key_value to get like brightness
     */
    protected String key_value;

    /**
     * The name of the current value to get like brightness-max
     */
    protected String key_max_value;
    /**
     * The name of the current value to get like brightness-min
     */
    protected String key_min_value;

    protected float step;

    private int default_value;
    public void Set_Default_Value(int val){
        default_value = val; Logger.d(TAG, "set default to:" + val);}
    public int Get_Default_Value(){return default_value;}

    public void ResetToDefault()
    {
        if (isSupported)
        {
            Logger.d(TAG,"Reset Back from:" + currentInt + " to:" + default_value);
            SetValue(default_value);
            ThrowCurrentValueChanged(default_value);
        }
    }

    public BaseManualParameter(Parameters parameters, CameraWrapperInterface cameraUiWrapper, float step)
    {
        super(cameraUiWrapper);
        this.parameters = parameters;
        this.step =step;
    }

    /**
     *  @param @parameters
     * @param @key_value
     * @param @key_max_value
     * @param @key_min_value
     * @param @parametersHandler
     */
    public BaseManualParameter(Parameters parameters, String key_value, String maxValue, String MinValue, CameraWrapperInterface cameraUiWrapper, float step) {
        this(parameters,cameraUiWrapper,step);
        this.key_value = key_value;
        key_max_value = maxValue;
        key_min_value = MinValue;
        if (!this.key_value.equals("") && !key_max_value.equals("") && !key_min_value.equals(""))
        {
            if (parameters.get(this.key_value) != null && parameters.get(key_max_value) != null && parameters.get(key_min_value) != null)
            {
                Logger.d(TAG, "parameters contains all 3 parameters " + key_value +" " + key_min_value +" " + key_max_value);
                if (!parameters.get(key_min_value).equals("") && !parameters.get(key_max_value).equals(""))
                {
                    Logger.d(TAG, "parameters get "+key_min_value +"/" +key_max_value+" success");
                    stringvalues = createStringArray(Integer.parseInt(parameters.get(key_min_value)), Integer.parseInt(parameters.get(key_max_value)), step);
                    currentString = parameters.get(this.key_value);
                    if (parameters.get(key_min_value).contains("-"))
                    {
                        Logger.d(TAG, "processing negative values");
                        currentInt = stringvalues.length /2 + Integer.parseInt(currentString);
                        default_value = currentInt;
                        isSupported = true;
                        isVisible = isSupported;
                    }
                    else
                    {
                        Logger.d(TAG, "processing positiv values");
                        for (int i = 0; i < stringvalues.length; i++) {
                            if (stringvalues[i].equals(currentString)) {
                                currentInt = i;
                                default_value = i;

                            }
                            isSupported = true;
                            isVisible = isSupported;
                        }
                    }

                }
                else
                    Logger.d(TAG, "min or max is empty in parameters");
            }
            else
                Logger.d(TAG, "parameters does not contain value, key_max_value or key_min_value");
        }
        else
            Logger.d(TAG, "failed to lookup values, "+ key_max_value + " or "+ key_min_value + " are empty");
    }

    @Override
    public boolean IsSetSupported() {
        return true;
    }

    @Override
    public void SetValue(int valueToset)
    {
        currentInt = valueToset;
        Logger.d(TAG, "set " + key_value + " to " + valueToset);
        if(stringvalues == null || stringvalues.length == 0)
            return;
        parameters.set(key_value, stringvalues[valueToset]);
        ThrowCurrentValueChanged(valueToset);
        ThrowCurrentValueStringCHanged(stringvalues[valueToset]);
        try
        {
            ((ParametersHandler) cameraUiWrapper.GetParameterHandler()).SetParametersToCamera(parameters);
        }
        catch (Exception ex)
        {
            Logger.exception(ex);
        }
    }


    public I_ModeParameterEvent GetPicFormatListner()
    {
        return picformatListner;
    }

    private final I_ModeParameterEvent picformatListner = new I_ModeParameterEvent()
    {

        @Override
        public void onParameterValueChanged(String val)
        {
           if (val.equals(PictureFormatHandler.CaptureMode[PictureFormatHandler.JPEG]) && isSupported)
           {
               isVisible = true;
               ThrowBackgroundIsSupportedChanged(true);
           }
            else {
               isVisible = false;
               ThrowBackgroundIsSupportedChanged(false);
               ResetToDefault();
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
    };
}
