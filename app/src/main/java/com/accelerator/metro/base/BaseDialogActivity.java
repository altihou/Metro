package com.accelerator.metro.base;

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
    private View view;

    private void createDialog(){
        builder = new AlertDialog.Builder(this);
        dialog = builder.create();
        view = LayoutInflater.from(this).inflate(R.layout.dialog_base, null);
        tvMsg = (TextView) view.findViewById(R.id.dialog_msg);
        dialog.setView(view);
    }

    protected void setDialogCancelable(boolean flag) {
        if (dialog == null) {
            createDialog();
        }
        dialog.setCancelable(flag);
    }

    protected void setDialogMsg(String msg) {
        if (dialog == null) {
            createDialog();
        }
        tvMsg.setText(msg);
    }

    protected void setDialogMsg(int msgId) {
        if (dialog == null) {
            createDialog();
        }
        tvMsg.setText(msgId);
    }

    protected void setDialogShow() {
        if (dialog == null) {
            createDialog();
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    protected void setDialogDismiss() {
        if (dialog == null) {
            createDialog();
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
            builder = null;
            dialog = null;
            view=null;
            tvMsg=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setDialogDismiss();
    }
}
