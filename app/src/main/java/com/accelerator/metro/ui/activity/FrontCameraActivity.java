package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.accelerator.metro.R;
import com.accelerator.metro.utils.CameraUtil;
import com.accelerator.metro.utils.FileUtil;
import com.accelerator.metro.utils.PictureUtil;
import com.accelerator.metro.utils.ToastUtil;
import com.accelerator.metro.widget.FrontSurfaceView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nicholas on 2016/7/16.
 */
public class FrontCameraActivity extends AppCompatActivity implements Camera.PictureCallback{

    private static final String TAG = FrontCameraActivity.class.getName();

    @Bind(R.id.camera_preview)
    FrameLayout cameraPreview;
    @Bind(R.id.img_button_capture)
    ImageButton btnCapture;
    @Bind(R.id.img_button_close)
    ImageButton btnClose;

    private FrontSurfaceView preview;
    private Camera camera;

    public static final String IMG_URI="img_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);
        ButterKnife.bind(this);
        preview = new FrontSurfaceView(this);
        cameraPreview.addView(preview);
    }

    @OnClick(R.id.img_button_capture)
    public void onCapture(View view) {
        camera.takePicture(null,null,this);
        btnCapture.setClickable(false);
    }

    @OnClick(R.id.img_button_close)
    public void onClose(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();
    }

    private void initCamera() {
        if (CameraUtil.checkCameraHardware(this)) {
            int cameraId = CameraUtil.findFrontFacingCamera();

            //如果没有前置摄像头，就打开后置摄像头
            if (cameraId==CameraUtil.INVALID_CAMERA_ID){
                cameraId=CameraUtil.findBackFacingCamera();
            }
            if (CameraUtil.isCameraIdValid(cameraId)) {
                if (safeCameraOpen(cameraId)) {
                    camera.startPreview();
                    preview.setCamera(camera);
                } else {
                    ToastUtil.Short(R.string.front_camera_open_fail);
                }
            } else {
                Log.e(TAG, "The camera id is not valid.");
            }
        } else {
            ToastUtil.Short(R.string.register_camera_null);
        }
    }

    //id是否安全
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;
        try {
            releaseCameraAndPreview();
            camera = Camera.open(id);
            qOpened = (camera != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qOpened;
    }

    //重置相机
    private void resetCamera() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void releaseCameraAndPreview() {
        preview.setCamera(null);
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

        File pictureFile = FileUtil.ImageUriFilePath();

        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        //旋转跳跃~我闭着眼~~！
        Bitmap newBitmap=PictureUtil.rotaingImageView(270,bitmap);

        Uri uri=PictureUtil.saveImg2SDCard(newBitmap,pictureFile);

        Intent result=new Intent();
        result.putExtra(IMG_URI,uri.toString());
        setResult(RESULT_OK,result);

        finish();
    }
}
