package com.accelerator.metro.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import com.soundcloud.android.crop.Crop;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nicholas on 2016/7/13.
 */
public class RegisterActivity extends BaseDialogActivity implements RegisterContract.View {

    private static final String TAG = RegisterActivity.class.getName();

    private static final int TAKE_PHOTO = 0;
    private static final int TAKE_ALBUM = 1;

    @Bind(R.id.register_img_avatar)
    CircleImageView ImgAvatar;
    @Bind(R.id.register_edt_account)
    TextInputLayout EdtAccount;
    @Bind(R.id.register_edt_pwd1)
    TextInputLayout EdtPwd1;
    @Bind(R.id.register_edt_pwd2)
    TextInputLayout EdtPwd2;
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
        toolbar.setNavigationOnClickListener(view -> finish());
        presenter = new RegisterPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(ImgAvatar);
        rxPermissions = RxPermissions.getInstance(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

//                Pattern p = Pattern.compile("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
//                Matcher m = p.matcher(account);
//
//                if (!m.find()) {
//                    ToastUtil.Short(R.string.login_not_phone);
//                    break;
//                }

                if (!checkNotNull(pwd1)) {
                    Snackbar.make(coordinatorLayout, R.string.login_not_empty_password,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                if (!checkNotNull(pwd2)) {
                    Snackbar.make(coordinatorLayout, R.string.login_not_empty_password,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                if (!checkEquals(pwd1, pwd2)) {
                    Snackbar.make(coordinatorLayout, R.string.register_pwd_not_equals,
                            Snackbar.LENGTH_SHORT)
                            .show();
                    break;
                }

                setDialogMsg(R.string.WAIT);
                setDialogCancelable(false);
                setDialogShow();

                String newPwd1 = CipherUtil.base64Encode(account, pwd1);
                String newPwd2 = CipherUtil.base64Encode(account, pwd2);

                if (TextUtils.isEmpty(avatarPath)) {
                    avatarPath = "";
                }

                presenter.register(account, newPwd1, newPwd2, avatarPath);

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            doAlbum();
                        } else {
                            ToastUtil.Short(R.string.register_toast_fail);
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
                    .subscribe(aBoolean -> {
                        if (aBoolean) {
                            doCamera();
                        } else {
                            ToastUtil.Short(R.string.register_toast_fail);
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

                        outputFileUri = Uri.fromFile(FileUtil.ImageUriFilePath());

                        Crop.of(uri, outputFileUri)
                                .withAspect(1,1)
                                .asSquare()
                                .start(this);
                    }
                }
                break;

            case TAKE_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        outputFileUri = Uri.fromFile(FileUtil.ImageUriFilePath());

                        Crop.of(data.getData(), outputFileUri)
                                .withAspect(1,1)
                                .asSquare()
                                .start(this);
                    } else {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                bitmap, null, null));

                        outputFileUri = Uri.fromFile(FileUtil.ImageUriFilePath());

                        Crop.of(uri, outputFileUri)
                                .withAspect(1,1)
                                .asSquare()
                                .start(this);
                    }
                }
                break;

            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = PictureUtil.getSmallBitmap(PictureUtil.getRealPathFromURI(outputFileUri), 300, 300);
                    ImgAvatar.setImageBitmap(bitmap);
                    avatarPath=outputFileUri.getPath();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.register_img_avatar)
    public void onAvatarClick(View view) {
        ImgAvatar.showContextMenu();
    }

    @Override
    public void onSucceed(User values) {

        Log.e(TAG, values.toString());

        SharedPreferences spf = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_PHONE, account);
        editor.putString(Config.USER_ID, values.getUser_id());
        editor.putString(Config.USER_SESSION, values.getSession_id());

        editor.apply();

        SharedPreferences sp= MetroApp.getContext().getSharedPreferences(Config.FIRST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorFirst=sp.edit();
        editorFirst.putBoolean(Config.FIRST_TIME,false);
        editorFirst.apply();

        setResult(RESULT_OK);

        ToastUtil.Short(R.string.register_succeed);

        finish();

    }

    @Override
    public void onFailure(String err) {
        Log.d(TAG, err);
        ToastUtil.Short(R.string.register_failure);
        setDialogDismiss();
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
    }

    @Override
    public void accountExist() {
        ToastUtil.Short(R.string.register_account_exist);
    }

    @Override
    public void pwdNotEquals() {
        ToastUtil.Short(R.string.register_pwd_not_equals);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }
}
