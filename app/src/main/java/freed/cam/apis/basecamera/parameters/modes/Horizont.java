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


/**
 * Created by Ar4eR on 14.01.16.
 */
public class Horizont extends AbstractModeParameter {

    private String value;

    public Horizont() {
    }



    @Override
    public boolean IsSupported() {
        return true;
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCam)
    {
        value = valueToSet;
        onValueHasChanged(valueToSet);
    }

    @Override
    public String GetValue()
    {
        if (value == null || value.equals(""))
            return "Off";
        else
            return value;
    }

    @Override
    public String[] GetValues()
    {
    return new String[]{"Off","On"};
    }

}
