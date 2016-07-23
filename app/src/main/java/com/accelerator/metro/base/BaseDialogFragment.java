package com.accelerator.metro.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.accelerator.metro.R;

/**
 * Created by zoom on 2016/5/28.
 */
public class BaseDialogFragment extends Fragment {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView tvMsg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        builder=new AlertDialog.Builder(getActivity());
        dialog=builder.create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_base, null);
        tvMsg= (TextView) view.findViewById(R.id.dialog_msg);
        dialog.setView(view);
    }

    protected void setDialogCanaelable(boolean flag){
        dialog.setCancelable(flag);
    }

    protected void setDialogMsg(String msg){
        tvMsg.setText(msg);
    }

    protected void setDialogMsg(int msgId){
        tvMsg.setText(msgId);
    }

    protected void showDialog(){
        if (!dialog.isShowing()){
            dialog.show();
        }
    }

    protected void setDialogDismiss(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        dialog.dismiss();
        builder=null;
        dialog=null;
        super.onDestroy();
    }
}
