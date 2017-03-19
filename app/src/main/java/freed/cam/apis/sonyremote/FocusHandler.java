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

package freed.cam.apis.sonyremote;

import freed.utils.Log;
import android.view.MotionEvent;

import freed.cam.apis.basecamera.AbstractFocusHandler;
import freed.cam.apis.basecamera.CameraWrapperInterface;
import freed.cam.apis.basecamera.FocusEvents;
import freed.cam.apis.basecamera.FocusRect;

/**
 * Created by troop on 31.01.2015.
 */
public class FocusHandler extends AbstractFocusHandler implements FocusEvents
{
    private final String TAG = FocusHandler.class.getSimpleName();
    private boolean isFocusing;

    public FocusHandler(CameraWrapperInterface cameraUiWrapper)
    {
        super(cameraUiWrapper);
    }

    @Override
    public void StartFocus() {

    }

    @Override
    public void StartTouchToFocus(FocusRect rect, int width, int height)
    {
        if (this.cameraUiWrapper.GetParameterHandler() == null)
            return;
        if (this.isFocusing)
        {
            this.cameraUiWrapper.GetCameraHolder().CancelFocus();
            Log.d(this.TAG, "Canceld Focus");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Log.WriteEx(ex);
            }
        }

        double x = rect.left + (rect.right - rect.left)/2  ;
        double y = rect.top + (rect.bottom - rect.top )  /2;
        double xproz = x / (double)width * 100;
        double yproz = y / (double)height *100;
        Log.d(this.TAG, "set focus to: x: " + xproz + " y: " +yproz);
        ((CameraHolderSony) this.cameraUiWrapper.GetCameraHolder()).StartFocus(this);
        ((CameraHolderSony) this.cameraUiWrapper.GetCameraHolder()).SetTouchFocus(xproz, yproz);
        this.isFocusing = true;
        if (this.focusEvent != null)
            this.focusEvent.FocusStarted(rect);
    }

    @Override
    public void SetMeteringAreas(FocusRect meteringRect, int width, int height) {

    }

    @Override
    public boolean isAeMeteringSupported() {
        return false;
    }

    @Override
    public void SetMotionEvent(MotionEvent event) {
        this.cameraUiWrapper.getSurfaceView().onTouchEvent(event);
    }


    @Override
    public void onFocusEvent(boolean success)
    {
        this.isFocusing = false;
        if (this.focusEvent != null) {
            this.focusEvent.FocusFinished(success);
            this.focusEvent.FocusLocked(((CameraHolderSony) this.cameraUiWrapper.GetCameraHolder()).canCancelFocus());
        }

    }

    @Override
    public void onFocusLock(boolean locked) {
        if (this.focusEvent != null) {
            focusEvent.FocusLocked(locked);
        }
    }

}


