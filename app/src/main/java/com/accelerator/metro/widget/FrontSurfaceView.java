package com.accelerator.metro.widget;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nicholas on 2016/7/16.
 */
public class FrontSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private static final String TAG=FrontSurfaceView.class.getName();
    /**
     * 控制相机方向
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private SurfaceHolder holder;
    private Camera camera;
    private AppCompatActivity activity;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public FrontSurfaceView(AppCompatActivity activity) {
        super(activity);
        this.activity=activity;
        holder=getHolder();
        holder.addCallback(this);
    }

    /**
     * 刷新相机
     */
    private void refreshCamera() {

        if (camera != null) {
            requestLayout();
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            camera.setDisplayOrientation(ORIENTATIONS.get(rotation));
            camera.setParameters(settingParameters());
        }
    }

    public void setCamera(Camera camera) {
        if (camera==null){
            return;
        }
        this.camera = camera;
    }

    /**
     * 配置相机参数
     * @return
     */
    private Camera.Parameters settingParameters() {
        // 获取相机参数
        Camera.Parameters params = camera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();

        //设置持续的对焦模式
        if (focusModes.contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //设置闪光灯自动开启
//        if (focusModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
//            params.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
//        }

        return params;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if(camera != null) {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        refreshCamera();

        // start preview with new settings
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (camera!=null){
            camera.release();
            camera=null;
        }
    }

}
