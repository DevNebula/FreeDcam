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

package freed.cam.apis.camera1.cameraholder;

import com.lge.hardware.LGCamera;

import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera1.CameraHolder;

/**
 * Created by troop on 08.06.2016.
 */
public class CameraHolderLG extends CameraHolder
{
    private LGCamera lgCamera;
    public CameraHolderLG(CameraWrapperInterface cameraUiWrapper, Frameworks frameworks) {
        super(cameraUiWrapper,frameworks);
    }

    @Override
    public boolean OpenCamera(int camera)
    {

        try {
            if (appSettingsManager.openCamera1Legacy())
                lgCamera = new LGCamera(camera, 256);
            else
                lgCamera = new LGCamera(camera);
            mCamera = lgCamera.getCamera();
            isRdy = true;
        }
        catch (RuntimeException ex)
        {
            cameraUiWrapper.onCameraError("Fail to connect to camera service");
            isRdy = false;
        }

        cameraUiWrapper.onCameraOpen("");
        return isRdy;
    }
}
