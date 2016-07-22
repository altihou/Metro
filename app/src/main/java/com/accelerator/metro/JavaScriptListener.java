package com.accelerator.metro;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class JavaScriptListener {

    private Context context;
    private AlertDialog.Builder dialog;

    private onPointClickListener listener;

    public interface onPointClickListener {
        void onClick(String name, String id, int type);
    }

    public void setPointClickListener(onPointClickListener lintener) {
        this.listener = lintener;
    }

    public JavaScriptListener(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context);
    }

    @JavascriptInterface
    public void getPointData(final String content, final String id) {

        dialog.setTitle(content);
        dialog.setMessage("此站为起点站还是终点站?");
        dialog.setCancelable(true);

        dialog.setNeutralButton("起点", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.onClick(content, id, 1);
                }
            }
        });

        dialog.setNegativeButton("终点", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.onClick(content, id, 2);
                }
            }
        });

        dialog.show();

    }


}
