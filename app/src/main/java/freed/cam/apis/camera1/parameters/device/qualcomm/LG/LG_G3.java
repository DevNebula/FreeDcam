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

package freed.cam.apis.camera1.parameters.device.qualcomm.LG;

import android.hardware.Camera.Parameters;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import freed.cam.apis.KEYS;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.parameters.manual.AbstractManualParameter;
import freed.cam.apis.basecamera.parameters.modes.MatrixChooserParameter;
import freed.cam.apis.camera1.parameters.manual.focus.BaseFocusManual;
import freed.cam.apis.camera1.parameters.manual.lg.FocusManualParameterLG;
import freed.dng.DngProfile;

/**
 * Created by troop on 01.06.2016.
 */
public class LG_G3 extends LG_G2
{

    public LG_G3(Parameters parameters, CameraWrapperInterface cameraUiWrapper) {
        super(parameters, cameraUiWrapper);
        parameters.set("lge-camera","1");
    }

    //not supported by device
    @Override
    public AbstractManualParameter getExposureTimeParameter() {
        return null;
    }

    //not supported by device
    @Override
    public AbstractManualParameter getIsoParameter() {
        return null;
    }

    public boolean IsDngSupported() {
        return true;
    }
    @Override
    public AbstractManualParameter getManualFocusParameter() {
        if (VERSION.SDK_INT >= VERSION_CODES.M)
        {
            return new BaseFocusManual(parameters, KEYS.KEY_MANUAL_FOCUS_POSITION,0,1023,KEYS.KEY_FOCUS_MODE_MANUAL, cameraUiWrapper,10,1);
        }
        else if (VERSION.SDK_INT < 21)
            return new FocusManualParameterLG(parameters, cameraUiWrapper);
        else
            return null;
    }

    @Override
    public AbstractManualParameter getCCTParameter() {
        return null;
    }

    @Override
    public DngProfile getDngProfile(int filesize) {
        switch (filesize)
        {
            case 2658304: //g3 front mipi
                return new DngProfile(64, 1212, 1096, DngProfile.Mipi, DngProfile.BGGR, 2424,
                        matrixChooserParameter.GetCustomMatrix(MatrixChooserParameter.NEXUS6));
            case 2842624://g3 front qcom
                //TODO somethings wrong with it;
                return new DngProfile(64, 1296, 1096, DngProfile.Qcom, DngProfile.BGGR, 0,
                        matrixChooserParameter.GetCustomMatrix(MatrixChooserParameter.NEXUS6));
            case 16224256:
				return new DngProfile(64, 4208, 3082, DngProfile.Mipi, DngProfile.BGGR, DngProfile.ROWSIZE, matrixChooserParameter.GetCustomMatrix(MatrixChooserParameter.NEXUS6));
            case 16424960:
                return new DngProfile(64, 4208, 3120, DngProfile.Mipi, DngProfile.BGGR, DngProfile.ROWSIZE, matrixChooserParameter.GetCustomMatrix(MatrixChooserParameter.NEXUS6));
            case 17326080://qcom g3
                return new DngProfile(64, 4164, 3120, DngProfile.Qcom, DngProfile.BGGR, DngProfile.ROWSIZE, matrixChooserParameter.GetCustomMatrix(MatrixChooserParameter.NEXUS6));
            case 17522688:
                return new DngProfile(64, 4212, 3082, DngProfile.Qcom, DngProfile.BGGR, DngProfile.ROWSIZE, matrixChooserParameter.GetCustomMatrix(MatrixChooserParameter.NEXUS6));
        }
        return null;
    }
}
