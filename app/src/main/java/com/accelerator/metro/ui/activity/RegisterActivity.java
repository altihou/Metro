package com.accelerator.metro.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.User;
import com.accelerator.metro.contract.RegisterContract;
import com.accelerator.metro.presenter.RegisterPresenter;
import com.accelerator.metro.utils.CameraUtil;
import com.accelerator.metro.utils.CipherUtil;
import com.accelerator.metro.utils.FileUtil;
import com.accelerator.metro.utils.PictureUtil;
import com.accelerator.metro.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Nicholas on 2016/7/13.
 */
public class RegisterActivity extends BaseDialogActivity implements RegisterContract.View {

    private static final String TAG = RegisterActivity.class.getName();

    private static final int TAKE_PHOTO = 0;
    private static final int TAKE_ALBUM = 1;
    private static final int PHOTO_REQUEST_CUT = 2;
    private static final int ALBUM_REQUEST_CUT = 3;
    public static final int FRONT_CAMERA_FACE1 = 4;
    public static final int FRONT_CAMERA_FACE2 = 5;
    public static final int FRONT_CAMERA_FACE3 = 6;

    public static final String FACE_TYPE = "type";

    @Bind(R.id.register_img_avatar)
    CircleImageView ImgAvatar;
    @Bind(R.id.register_edt_account)
    TextInputLayout EdtAccount;
    @Bind(R.id.register_edt_pwd1)
    TextInputLayout EdtPwd1;
    @Bind(R.id.register_edt_pwd2)
    TextInputLayout EdtPwd2;
    @Bind(R.id.register_img_face1)
    ImageView ImgFace1;
    @Bind(R.id.register_img_face2)
    ImageView ImgFace2;
    @Bind(R.id.register_img_face3)
    ImageView ImgFace3;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private RxPermissions rxPermissions;
    private Uri outputFileUri;
    private String avatarPath;
    private RegisterPresenter presenter;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.register);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        presenter=new RegisterPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(ImgAvatar);
        rxPermissions = RxPermissions.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_register_commit:

                EditText accountEdit = EdtAccount.getEditText();
                EditText pwd1Edit = EdtPwd1.getEditText();
                EditText pwd2Edit = EdtPwd2.getEditText();

                account = accountEdit != null ? accountEdit.getText().toString() : null;
                String pwd1 = pwd1Edit != null ? pwd1Edit.getText().toString() : null;
                String pwd2 = pwd2Edit != null ? pwd2Edit.getText().toString() : null;

                if (!checkNotNull(account)) {
                    Snackbar.make(coordinatorLayout, R.string.login_not_empty_account,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                if (!checkNotNull(pwd1)){
                    Snackbar.make(coordinatorLayout, R.string.login_not_empty_password,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                if (!checkNotNull(pwd2)){
                    Snackbar.make(coordinatorLayout, R.string.login_not_empty_password,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                if (!checkEquals(pwd1,pwd2)){
                    Snackbar.make(coordinatorLayout, R.string.register_pwd_not_equals,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                setDialogMsg(R.string.register_start);
                dialog.show();

                String newPwd1 = CipherUtil.base64Encode(account,pwd1);
                String newPwd2 = CipherUtil.base64Encode(account,pwd2);

                Log.e(TAG,"avatarPath0"+avatarPath);

                if (TextUtils.isEmpty(avatarPath)){
                  //  avatarPath="file:///android_asset/ic_launcher.png";
                    Log.e(TAG,"avatarPath1"+avatarPath);
                }

                presenter.register(account,newPwd1,newPwd2,avatarPath);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkNotNull(String str) {
        return !TextUtils.isEmpty(str);
    }

    private boolean checkEquals(String str1, String str2) {
        return str1.equals(str2);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_register_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_register_context_camera:
                if (CameraUtil.checkCameraHardware(this)) {
                    fromCamera();
                } else {
                    ToastUtil.Short(R.string.register_camera_null);
                }
                return true;

            case R.id.menu_register_context_album:
                formAlbum();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void formAlbum() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                doAlbum();
                            } else {
                                ToastUtil.Short(R.string.register_toast_fail);
                            }
                        }
                    });
        } else {
            doAlbum();
        }
    }

    private void fromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rxPermissions.request(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                doCamera();
                            } else {
                                ToastUtil.Short(R.string.register_toast_fail);
                            }
                        }
                    });
        } else {
            doCamera();
        }
    }

    private void doAlbum() {
        Intent album = new Intent();
        album.setAction(Intent.ACTION_PICK);
        album.setType("image/*");
        if (album.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(album, TAKE_ALBUM);
        } else {
            ToastUtil.Short(R.string.register_open_fail);
        }
    }

    private void doCamera() {

        outputFileUri = Uri.fromFile(FileUtil.ImageUriFilePath());
        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        if (camera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera, TAKE_PHOTO);
        } else {
            ToastUtil.Short(R.string.register_open_fail);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (outputFileUri != null) {
                        //旋转处理
                        int degree = PictureUtil.readPictureDegree(outputFileUri.getPath());
                        Bitmap oldBitmap = BitmapFactory.decodeFile(outputFileUri.getPath());
                        Bitmap newBitmap = PictureUtil.rotaingImageView(degree, oldBitmap);
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                newBitmap, null, null));
                        setPhotoZoom(uri, PHOTO_REQUEST_CUT);
                    }
                }
                break;

            case TAKE_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        setPhotoZoom(data.getData(), ALBUM_REQUEST_CUT);
                    } else {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                bitmap, null, null));
                        setPhotoZoom(uri, ALBUM_REQUEST_CUT);
                    }
                }
                break;

            case PHOTO_REQUEST_CUT:
                if (resultCode == RESULT_OK && data.getExtras() != null) {
                    Bitmap c = data.getParcelableExtra("data");
                    Uri uri=PictureUtil.saveImg2SDCard(c,FileUtil.ImageUriFilePath());
                    avatarPath=uri.getPath();
                    ImgAvatar.setImageBitmap(c);
                }
                break;

            case ALBUM_REQUEST_CUT:
                if (resultCode == RESULT_OK && data.getExtras() != null) {
                    Bitmap a = data.getParcelableExtra("data");
                    Uri uri = PictureUtil.saveImg2SDCard(a,FileUtil.ImageUriFilePath());
                    avatarPath=uri.getPath();
                    ImgAvatar.setImageBitmap(a);
                }
                break;
            case FRONT_CAMERA_FACE1:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "FACE1 :" + data.getStringExtra(FrontCameraActivity.IMG_URI));
                }
                break;
            case FRONT_CAMERA_FACE2:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "FACE2 :" + data.getStringExtra(FrontCameraActivity.IMG_URI));
                }
                break;
            case FRONT_CAMERA_FACE3:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "FACE3 :" + data.getStringExtra(FrontCameraActivity.IMG_URI));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPhotoZoom(Uri uri, int type) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是输出图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);

        switch (type) {
            case PHOTO_REQUEST_CUT:
                startActivityForResult(intent, PHOTO_REQUEST_CUT);
                break;
            case ALBUM_REQUEST_CUT:
                startActivityForResult(intent, ALBUM_REQUEST_CUT);
                break;
        }

    }

    @OnClick(R.id.register_img_avatar)
    public void onAvatarClick(View view) {
        ImgAvatar.showContextMenu();
    }

    @OnClick(R.id.register_img_face1)
    public void onFace1Click(View view) {
        fromFrontCamera(FRONT_CAMERA_FACE1);
    }

    @OnClick(R.id.register_img_face2)
    public void onFace2Click(View view) {
        fromFrontCamera(FRONT_CAMERA_FACE2);
    }

    @OnClick(R.id.register_img_face3)
    public void onFace3Click(View view) {
        fromFrontCamera(FRONT_CAMERA_FACE3);
    }

    public void fromFrontCamera(final int face) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            rxPermissions.request(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                doFrontCamera(face);
                            } else {
                                ToastUtil.Short(R.string.register_toast_fail);
                            }
                        }
                    });
        } else {
            doFrontCamera(face);
        }
    }

    private void doFrontCamera(int face) {

        Intent intent = new Intent(this, FrontCameraActivity.class);

        switch (face) {
            case FRONT_CAMERA_FACE1:
                intent.putExtra(FACE_TYPE, FRONT_CAMERA_FACE1);
                startActivityForResult(intent, FRONT_CAMERA_FACE1);
                break;
            case FRONT_CAMERA_FACE2:
                intent.putExtra(FACE_TYPE, FRONT_CAMERA_FACE2);
                startActivityForResult(intent, FRONT_CAMERA_FACE2);
                break;
            case FRONT_CAMERA_FACE3:
                intent.putExtra(FACE_TYPE, FRONT_CAMERA_FACE3);
                startActivityForResult(intent, FRONT_CAMERA_FACE3);
                break;
        }

    }

    @Override
    public void onSucceed(User values) {
        Log.e(TAG,values.getElse_info().toString());

        SharedPreferences spf= MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=spf.edit();

        editor.putString(Config.USER_NAME,account);
        editor.putString(Config.USER_ID,values.getElse_info().getUser_id());
        editor.putString(Config.USER_SESSION,values.getElse_info().getSession_id());
        editor.putBoolean(Config.USER_REFRESH,true);

        editor.apply();

        Intent intent=new Intent();
        intent.putExtra(LoginActivity.REGISTER_RESULT,LoginActivity.REGISTER_RESULT_CODE);
        setResult(RESULT_OK,intent);

        ToastUtil.Short(R.string.register_succeed);

        finish();

    }

    @Override
    public void onFailure(String err) {
        Log.d(TAG, err);
        dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        dialog.dismiss();
    }
}
