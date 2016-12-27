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

package freed.cam.ui.themesample.settings.childs;

import android.content.Context;
import android.util.AttributeSet;

import freed.ActivityInterface;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.utils.AppSettingsManager;

/**
 * Created by GeorgeKiarie on 10/4/2015.
 */
public class SettingsChildMenuIntervalDuration extends SettingsChildMenu
{
    private CameraWrapperInterface cameraUiWrapper;

    public SettingsChildMenuIntervalDuration(Context context) {
        super(context);
    }

    public SettingsChildMenuIntervalDuration(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void SetStuff(ActivityInterface fragment_activityInterface, String settingvalue) {
        super.SetStuff(fragment_activityInterface, settingvalue);
        //onParameterValueChanged(appSettingsManager.getApiString(AppSettingsManager.SETTING_INTERVAL_DURATION));
    }

    public void SetCameraUIWrapper(CameraWrapperInterface cameraUiWrapper)
    {
        this.cameraUiWrapper = cameraUiWrapper;
        SetParameter(cameraUiWrapper.GetParameterHandler().IntervalDuration);
    }

    @Override
    public String[] GetValues() {

        //return new String[] {StringUtils.ON, StringUtils.OFF};
        return parameter.GetValues();
    }

    @Override
    public void SetValue(String value)
    {
        fragment_activityInterface.getAppSettings().setApiString(AppSettingsManager.SETTING_INTERVAL_DURATION, value);
        onParameterValueChanged(value);
        parameter.SetValue(value,true);
    }
}