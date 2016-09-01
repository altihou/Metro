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
import com.accelerator.metro.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.soundcloud.android.crop.Crop;
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
    private Uri imageUri;
    private String avatarPath;
    private ModifyPresenter presenter;
    private EditText nickEdit;

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
        rxPermissions = RxPermissions.getInstance(this);
        presenter = new ModifyPresenter(this);

        modifyUserRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkId = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(checkId);
                String sex = rb.getText().toString();
                switch (sex) {
                    case "男":
                        sexValue = "1";
                        break;
                    case "女":
                        sexValue = "2";
                        break;
                    case "保密":
                        sexValue = "0";
                        break;
                }
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

        nickEdit = modifyUserNickname.getEditText();

        if (nickEdit != null) {
            nickEdit.setText(nickname);
            nickEdit.setSelection(nickname.length());
        }

        if (!TextUtils.isEmpty(sex)) {
            if (sex.equals("1"))
                rbMan.setChecked(true);
            if (sex.equals("2"))
                rbWomen.setChecked(true);
            if (sex.equals("0"))
                rbSecret.setChecked(true);
        } else {
            rbSecret.setChecked(true);
            sexValue = "0";
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_register_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
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

        imageUri = Uri.fromFile(FileUtil.ImageUriFilePath());

        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        if (camera.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(camera, TAKE_PHOTO);
        } else {
            ToastUtil.Short(R.string.register_open_fail);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    int degree = PictureUtil.readPictureDegree(imageUri.getPath());
                    Bitmap oldBitmap = BitmapFactory.decodeFile(imageUri.getPath());
                    Bitmap newBitmap = PictureUtil.rotaingImageView(degree, oldBitmap);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                            newBitmap, null, null));

                    imageUri = Uri.fromFile(FileUtil.ImageUriFilePath());

                    Crop.of(uri, imageUri)
                            .withAspect(1,1)
                            .asSquare()
                            .start(this);

                }
                break;
            case TAKE_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {

                        imageUri = Uri.fromFile(FileUtil.ImageUriFilePath());

                        Crop.of(data.getData(), imageUri)
                                .asSquare()
                                .start(this);
                    } else {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                                bitmap, null, null));

                        imageUri = Uri.fromFile(FileUtil.ImageUriFilePath());

                        Crop.of(uri, imageUri)
                                .withAspect(1,1)
                                .asSquare()
                                .start(this);
                    }
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = PictureUtil.getSmallBitmap(PictureUtil.getRealPathFromURI(imageUri), 300, 300);
                    modifyUserAvatar.setImageBitmap(bitmap);
                    avatarPath=imageUri.getPath();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.modify_user_avatar)
    public void onAvatar(View view) {
        modifyUserAvatar.showContextMenu();
    }

    @OnClick(R.id.modify_user_save)
    public void onSaveClick(View view) {

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        String nickName = nickEdit.getText().toString();

        if (TextUtils.isEmpty(avatarPath)) {
            avatarPath = "";
        }

        presenter.modify(nickName, sexValue, avatarPath);

    }

    @Override
    public void reLogin() {
        setDialogDismiss();
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onSucceed(ModifyUser values) {

        ModifyUser.ElseInfoBean info = values.getElse_info();

        SharedPreferences spf = MetroApp.getContext()
                .getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString(Config.USER_ID, values.getUser_id());
        editor.putString(Config.USER_SESSION, values.getSession_id());
        editor.putString(Config.USER_NICKNAME, info.getNickname());
        editor.putString(Config.USER_SEX, info.getUser_sex());
        editor.putString(Config.USER_AVATAR, info.getUser_headpic());
        editor.putBoolean(Config.USER_REFRESH, true);

        editor.apply();

        setResult(RESULT_OK);

        finish();

    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG, err);
        setDialogDismiss();
    }

    @Override
    public void onCompleted() {
        setDialogDismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscription();
    }
}
