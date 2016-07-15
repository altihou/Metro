package com.accelerator.metro.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.accelerator.metro.R;
import com.accelerator.metro.utils.FileUtil;
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
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getName();

    private static final int TAKE_PHOTO = 0;
    private static final int TAKE_ALBUM = 1;
    private static final int PHOTO_REQUEST_CUT = 2;
    private static final int ALBUM_REQUEST_CUT = 3;

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
    @Bind(R.id.register_btn_register)
    Button BtnRegister;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private RxPermissions rxPermissions;
    private Uri outputFileUri;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(ImgAvatar);
        rxPermissions = RxPermissions.getInstance(this);
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
                fromCamera();
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
                            if (aBoolean){
                                doCamera();
                            }else {
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
        outputFileUri=Uri.fromFile(FileUtil.ImageUriFilePath());
        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);

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
                    setPhotoZoom(outputFileUri, PHOTO_REQUEST_CUT);
                }
                break;

            case TAKE_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data.getData()!=null){
                        setPhotoZoom(data.getData(), ALBUM_REQUEST_CUT);
                    }else {
                        Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                        Uri uri=Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                bitmap,null,null));
                        setPhotoZoom(uri, ALBUM_REQUEST_CUT);
                    }
                }
                break;

            case PHOTO_REQUEST_CUT:
                if (data.getExtras() != null) {
                    Bitmap c = (Bitmap) data.getExtras().get("data");
                    ImgAvatar.setImageBitmap(c);
                }
                break;

            case ALBUM_REQUEST_CUT:
                if (data.getExtras() != null) {
                    Bitmap a = (Bitmap) data.getExtras().get("data");
                    ImgAvatar.setImageBitmap(a);
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

        // outputX,outputY 是剪裁图片的宽高
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

    }

    @OnClick(R.id.register_img_face2)
    public void onFace2Click(View view) {

    }

    @OnClick(R.id.register_img_face3)
    public void onFace3Click(View view) {

    }

    @OnClick(R.id.register_btn_register)
    public void onRegisterClick(View view) {
        Log.e(TAG, "btn_register");
    }

}
