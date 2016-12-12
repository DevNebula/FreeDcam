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

package freed.cam.apis.basecamera.parameters.modes;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ingo on 26.12.2014.
 *
 * This lowlevel class is implemented for all apis as cameraUiWrapper for their parameters
 * only this class should used by the ui
 */
public class AbstractModeParameter implements ModeParameterInterface
{
    //Holds the ui thread to invoke ui items
    Handler uihandler;
    private final String TAG = AbstractModeParameter.class.getSimpleName();

    public AbstractModeParameter()
    {
        events = new ArrayList<>();
        uihandler = new Handler(Looper.getMainLooper());
    }

    /**
     * the Interface that must be included to listen on the background changed events
     * The ui should listen to that events to show dynamic background changes
     */
    public interface I_ModeParameterEvent
    {
        /**
         * Gets fired when the parameter has changed in background
         * @param val the key_value that has changed
         */
        void onParameterValueChanged(String val);

        /**
         * gets fired when the parameter is no more supported due a mode change or because its not supported by the cam
         * @param isSupported if true the paramter should be shown else hidden
         */
        void onParameterIsSupportedChanged(boolean isSupported);

        /**
         * gets fired when a pramater is readonly, the item should be disabled
         * @param isSupported if false parameter is readonly
         */
        void onParameterIsSetSupportedChanged(boolean isSupported);

        /**
         * gets fired when the background values has changed
         * @param values the string array with new values
         */
        void onParameterValuesChanged(String[] values);
    }

    private final List<I_ModeParameterEvent> events;

    public void addEventListner(I_ModeParameterEvent eventListner)
    {
        if (!events.contains(eventListner))
            events.add(eventListner);
    }
    public void removeEventListner(I_ModeParameterEvent parameterEvent)
    {
        if (events.contains(parameterEvent))
            events.remove(parameterEvent);
    }

    /***
     *
     * @return true if the parameter is supported
     */
    @Override
    public boolean IsSupported() {
        return false;
    }

    /**
     *
     * @param valueToSet to the camera
     * @param setToCamera not needed anymore?
     */
    @Override
    public void SetValue(String valueToSet, boolean setToCamera) {

    }

    @Override
    public String GetValue() {
        return null;
    }

    @Override
    public String[] GetValues() {
        return null;
    }

    @Override
    public boolean IsVisible() {
        return true;
    }

    /**
     * Throws the event to all registerd listners that the key_value has changed
     * @param value the new String key_value that should get applied to the listners
     */
    public void onValueHasChanged(final String value)
    {
        if (events == null || events.size() == 0 || value.equals(""))
            return;
        //Log.d(TAG, "BackgroundValueHasCHanged:" + key_value);

        for (int i = 0; i< events.size(); i ++)
        {
            if (events.get(i) == null)
            {
                events.remove(i);
                i--;

            }
            else {
                final int t = i;
                uihandler.post(new Runnable() {
                    @Override
                    public void run() {
                        events.get(t).onParameterValueChanged(value);
                    }
                });
            }
        }



    }
    public void onValuesHasChanged(final String[] value)
    {
        for (int i = 0; i< events.size(); i ++)
        {
            if (events.get(i) == null)
            {
                events.remove(i);
                i--;

            }
            else {
                final int t = i;
                uihandler.post(new Runnable() {
                    @Override
                    public void run() {
                        events.get(t).onParameterValuesChanged(value);
                    }
                });
            }
        }


    }

    /**
     *
     * @param value if true set parameter is supported, else not
     */
    public void onIsSupportedChanged(final boolean value)
    {
        for (int i = 0; i< events.size(); i ++)
        {
            if (events.get(i) == null)
            {
                events.remove(i);
                i--;

            }
            else
            {
                final int t = i;
                uihandler.post(new Runnable() {
                    @Override
                    public void run() {
                        events.get(t).onParameterIsSupportedChanged(value);
                    }
                });
            }
        }


    }

    /**
     *
     * @param value if true the key_value can set to the camera, if false the key_value is read only
     * this affects only sonyapi and camera2
     */
    public void onSetIsSupportedHasChanged(final boolean value)
    {
        for (int i = 0; i< events.size(); i ++)
        {
            if (events.get(i) == null)
            {
                events.remove(i);
                i--;

            }
            else
            {
                final int t = i;
                uihandler.post(new Runnable() {
                    @Override
                    public void run() {
                        events.get(t).onParameterIsSetSupportedChanged(value);
                    }
                });
            }

        }


    }
}
