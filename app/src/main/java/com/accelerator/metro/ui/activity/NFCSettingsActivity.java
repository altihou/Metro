package com.accelerator.metro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.R;
import com.accelerator.metro.ui.fragment.MineFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NFCSettingsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nfc_tv_state)
    TextView nfcTvState;
    @Bind(R.id.nfc_switch)
    Switch nfcSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_nfc);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        SharedPreferences sp= MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);

        boolean state=sp.getBoolean(Config.NFC_STATE,false);

        if (state){
            nfcSwitch.setChecked(true);
            nfcTvState.setText(MetroApp.getContext().getResources().getString(R.string.nfc_open));
        }else {
            nfcSwitch.setChecked(false);
            nfcTvState.setText(MetroApp.getContext().getResources().getString(R.string.nfc_close));
        }

        nfcSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sp= MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                if (b){
                    nfcTvState.setText(MetroApp.getContext().getResources().getString(R.string.nfc_open));
                    editor.putBoolean(Config.NFC_STATE,true);
                    editor.apply();
                }else {
                    nfcTvState.setText(MetroApp.getContext().getResources().getString(R.string.nfc_close));
                    editor.putBoolean(Config.NFC_STATE,false);
                    editor.apply();
                }
            }
        });

    }

    @Override
    public void finish() {

        SharedPreferences sp = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        boolean showNFC = sp.getBoolean(Config.NFC_STATE, false);

        if (showNFC){
            Intent intent=new Intent(MineFragment.ACTION_NAME_SHOW_NFC);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else {
            Intent intent=new Intent(MineFragment.ACTION_NAME_HIDE_NFC);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        super.finish();
    }
}
