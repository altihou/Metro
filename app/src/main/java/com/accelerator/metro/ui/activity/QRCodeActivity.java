package com.accelerator.metro.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.ui.fragment.OrderFinishFragment;
import com.accelerator.metro.utils.FileUtil;
import com.accelerator.metro.utils.PictureUtil;
import com.accelerator.metro.utils.QRCodeUtil;
import com.accelerator.metro.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class QRCodeActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.qrcode_img)
    ImageView qrcodeImg;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.qrcode_tips)
    TextView tvTips;
    @Bind(R.id.qrcode_btn_close)
    Button btnClose;

    private Bundle bundle;
    private String orderNum;

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            orderNum = bundle.getString(OrderFinishFragment.ORDER_NUM);
            toQRCode();
        }

        tvTips.setText(MetroApp.getContext().getResources().getString(R.string.qrcode_tips_no_nfc));

    }

    @Override
    protected void onResume() {
        super.onResume();
        rxPermissions = RxPermissions.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_qrcode_share) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    shareQRCode();
                                } else {
                                    ToastUtil.Short(R.string.register_toast_fail);
                                }
                            }
                        });
            } else {
                shareQRCode();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareQRCode() {
        Bitmap bitmap = PictureUtil.captureWithoutStatusBar(this);
        Uri uri = PictureUtil.saveImg2SDCard(bitmap, FileUtil.ImageUriFilePath());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, MetroApp.getContext().getResources().getString(R.string.qrcode_share)));
    }

    private void toQRCode() {
        qrcodeImg.setImageBitmap(QRCodeUtil.createQRCode(orderNum, 600));
    }


    @OnClick(R.id.qrcode_btn_close)
    public void onNFCClose(View view) {
        finish();
    }

}
