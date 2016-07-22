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
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.api.ApiStore;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ModifyUser;
import com.accelerator.metro.contract.ModifyContract;
import com.accelerator.metro.presenter.ModifyPresenter;
import com.accelerator.metro.utils.CameraUtil;
import com.accelerator.metro.utils.FileUtil;
import com.accelerator.metro.utils.PictureUtil;
import com.accelerator.metro.utils.RxBus;
import com.accelerator.metro.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ModifyUserActivity extends BaseDialogActivity implements ModifyContract.View {

    private static final String TAG = ModifyUserActivity.class.getName();

    public static final String REFRESH = "modify_refresh";

    private static final int TAKE_PHOTO = 0;
    private static final int TAKE_ALBUM = 1;
    private static final int PHOTO_REQUEST_CUT = 2;
    private static final int ALBUM_REQUEST_CUT = 3;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.modify_user_avatar)
    CircleImageView modifyUserAvatar;
    @Bind(R.id.modify_user_nickname)
    TextInputLayout modifyUserNickname;
    @Bind(R.id.modify_user_rg)
    RadioGroup modifyUserRg;
    @Bind(R.id.modify_user_save)
    Button modifyUserSave;
    @Bind(R.id.modify_user_rb_man)
    RadioButton rbMan;
    @Bind(R.id.modify_user_rb_women)
    RadioButton rbWomen;
    @Bind(R.id.modify_user_rb_secret)
    RadioButton rbSecret;

    private String sexValue;
    private RxPermissions rxPermissions;
    private Uri outputFileUri;
    private String avatarPath;
    private ModifyPresenter presenter;
    private EditText nickRdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.modify);
        setSupportActionBar(toolbar);
        initViews();
    }

    private void initViews() {

        registerForContextMenu(modifyUserAvatar);
        rxPermissions=RxPermissions.getInstance(this);
        presenter=new ModifyPresenter(this);

        modifyUserRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(checkId);
                sexValue = rb.getText().toString();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences spf = MetroApp.getContext()
                .getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        String avatarUrl = spf.getString(Config.USER_AVATAR, "");
        String nickname = spf.getString(Config.USER_NICKNAME, "");
        String sex = spf.getString(Config.USER_SEX, "");

        Glide.with(this)
                .load(ApiStore.BASE_URL_IMG + avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(modifyUserAvatar);

        nickRdit = modifyUserNickname.getEditText();

        if (nickRdit != null) {
            nickRdit.setText(nickname);
            nickRdit.setSelection(nickname.length());
        }

        if (!TextUtils.isEmpty(sex)) {
            if (sex.equals("男"))
                rbMan.setChecked(true);
            if (sex.equals("女"))
                rbWomen.setChecked(true);
            if (sex.equals("保密"))
                rbSecret.setChecked(true);
        }else {
            rbMan.setChecked(true);
            sexValue="男";
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_register_context,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
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

        switch (requestCode){
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
                    modifyUserAvatar.setImageBitmap(c);
                }
                break;
            case ALBUM_REQUEST_CUT:
                if (resultCode == RESULT_OK && data.getExtras() != null) {
                    Bitmap a = data.getParcelableExtra("data");
                    Uri uri = PictureUtil.saveImg2SDCard(a,FileUtil.ImageUriFilePath());
                    avatarPath=uri.getPath();
                    modifyUserAvatar.setImageBitmap(a);
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

    @OnClick(R.id.modify_user_avatar)
    public void onAvatar(View view) {
        modifyUserAvatar.showContextMenu();
    }

    @OnClick(R.id.modify_user_save)
    public void onSaveClick(View view) {

        setDialogMsg(R.string.WAIT);
        dialog.show();

        String nickName=nickRdit.getText().toString();
        presenter.modify(nickName,sexValue,avatarPath);

    }

    @Override
    public void reLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        dialog.dismiss();
    }

    @Override
    public void onSucceed(ModifyUser values) {

        SharedPreferences spf= MetroApp.getContext()
                .getSharedPreferences(Config.USER,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=spf.edit();

        editor.putString(Config.USER_NICKNAME,values.getElse_info().getNickname());
        editor.putString(Config.USER_SEX,values.getElse_info().getUser_sex());
        editor.putString(Config.USER_AVATAR,values.getElse_info().getUser_headpic());
        editor.putBoolean(Config.USER_REFRESH,true);

        editor.apply();

        RxBus.getDefault().post(REFRESH);

        finish();

    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG,err);
        dialog.dismiss();
    }

    @Override
    public void onCompleted() {
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }
}
