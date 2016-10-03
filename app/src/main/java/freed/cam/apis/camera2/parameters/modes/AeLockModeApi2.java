package freed.cam.apis.camera2.parameters.modes;

import android.annotation.TargetApi;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;

import freed.cam.apis.KEYS;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.camera2.CameraHolderApi2;

/**
 * Created by Ingo on 03.10.2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AeLockModeApi2 extends BaseModeApi2 {
    public AeLockModeApi2(CameraWrapperInterface cameraUiWrapper) {
        super(cameraUiWrapper);
    }

    @Override
    public boolean IsSupported() {
        return true;
    }


    @Override
    public String GetValue() {
        if (((CameraHolderApi2)cameraUiWrapper.GetCameraHolder()).get(CaptureRequest.CONTROL_AE_LOCK))
            return KEYS.TRUE;
        else
            return KEYS.FALSE;
    }

    @Override
    public String[] GetValues() {
        return new String[]{KEYS.FALSE, KEYS.TRUE};
    }

    @Override
    public void SetValue(String valueToSet, boolean setToCamera) {
        if (valueToSet.equals(KEYS.TRUE))
            ((CameraHolderApi2)cameraUiWrapper.GetCameraHolder()).SetParameterRepeating(CaptureRequest.CONTROL_AE_LOCK,true);
        else
            ((CameraHolderApi2)cameraUiWrapper.GetCameraHolder()).SetParameterRepeating(CaptureRequest.CONTROL_AE_LOCK,false);
        //BackgroundValueHasChanged(valueToSet);
    }
}
