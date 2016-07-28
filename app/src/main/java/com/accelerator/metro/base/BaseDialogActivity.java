package com.accelerator.metro.base;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.accelerator.metro.R;

/**
 * Created by Nicholas on 2016/7/18.
 */
public class BaseDialogActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    protected AlertDialog dialog;
    private TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_base, null);
        tvMsg = (TextView) view.findViewById(R.id.dialog_msg);
        dialog.setView(view);
    }

    protected void setDialogCancelable(boolean flag) {
        dialog.setCancelable(flag);
    }

    protected void setDialogMsg(String msg) {
        tvMsg.setText(msg);
    }

    protected void setDialogMsg(int msgId) {
        tvMsg.setText(msgId);
    }

    protected void setDialogShow() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    protected void setDialogDismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setDialogDismiss();
        builder = null;
        dialog = null;
    }
}
