package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.nfc.AccountStorage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NFCActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nfc_tv_succeed)
    TextView nfcTvSucceed;
    @Bind(R.id.nfc_btn_close)
    Button btnClose;

    private String startId;
    private String endId;
    private String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

//        Intent intent = getIntent();
//
//        if (intent != null) {
//            Bundle bundle = intent.getExtras();
//            startId = bundle.getString(OrderFinishFragment.START_ID);
//            endId = bundle.getString(OrderFinishFragment.END_ID);
//            price = bundle.getString(OrderFinishFragment.ORDER_PRICE);
//            toNFC();
//        }

        toNFC();
    }

    private void toNFC() {
//        String info = startId + "|" + endId + "|" + price;
        SharedPreferences sp= MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        String info = sp.getString(Config.USER_ID,"error");
        AccountStorage.SetAccount(this, info);
    }

    @OnClick(R.id.nfc_btn_close)
    public void onCloseClick(View view) {
        finish();
    }

}
