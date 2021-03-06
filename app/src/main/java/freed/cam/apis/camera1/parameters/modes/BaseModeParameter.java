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

package freed.cam.apis.camera1.parameters.modes;

import android.hardware.Camera.Parameters;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.modules.ModuleChangedEvent;
import freed.cam.apis.basecamera.parameters.modes.AbstractModeParameter;
import freed.cam.apis.basecamera.parameters.modes.AbstractModeParameter.I_ModeParameterEvent;
import freed.cam.apis.camera1.parameters.ParametersHandler;
import freed.utils.AppSettingsManager;
import freed.utils.Log;

/**
 * Created by troop on 17.08.2014.
 * That class handel basic parameter logic and
 * expect a key_value String like "antibanding" and a values String "antibanding-values"
 * if one of the key is empty the parameters is set as unsupported
 * when extending that class make sure you set isSupported and isVisible
 */
public class BaseModeParameter extends AbstractModeParameter implements ModuleChangedEvent, I_ModeParameterEvent
{
    /*
    The Key to set/get a value from the parameters
     */
    protected String key_value;
    /*
    The Key to get the supported values from the parameters
     */
    protected String key_values;
    //if the parameter is supported
    boolean isSupported;
    //if the parameter is visibile to ui
    boolean isVisible = true;
    //the parameters from the android.Camera
    protected Parameters  parameters;
    protected CameraWrapperInterface cameraUiWrapper;
    private final String TAG = BaseModeParameter.class.getSimpleName();

    /*
    The stored StringValues from the parameter
     */
    protected String[] valuesArray;

    public BaseModeParameter(Parameters  parameters, CameraWrapperInterface cameraUiWrapper)
    {
        this.parameters = parameters;
        this.cameraUiWrapper = cameraUiWrapper;
    }

    public BaseModeParameter(Parameters  parameters, CameraWrapperInterface cameraUiWrapper, AppSettingsManager.SettingMode settingMode)
    {
        this(parameters,cameraUiWrapper);
        this.key_value = settingMode.getKEY();
        this.valuesArray = settingMode.getValues();
        isSupported = settingMode.isSupported();
    }

    @Override
    public boolean IsSupported()
    {
        return isSupported;
    }

    @Override
    public boolean IsVisible() {
        return isVisible;
    }

    @Override
    public void SetValue(String valueToSet,  boolean setToCam)
    {
        if (valueToSet == null)
            return;
        parameters.set(key_value, valueToSet);
        Log.d(TAG, "set " + key_value + " to " + valueToSet);
        onValueHasChanged(valueToSet);
        if (setToCam) {

            ((ParametersHandler) cameraUiWrapper.getParameterHandler()).SetParametersToCamera(parameters);
        }
    }


    @Override
    public String GetValue()
    {
        return parameters.get(key_value);
    }
    @Override
    public String[] GetValues()
    {
        return valuesArray;
    }

    @Override
    public void onModuleChanged(String module) {

    }

    @Override
    public void onParameterValueChanged(String val) {

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
}
