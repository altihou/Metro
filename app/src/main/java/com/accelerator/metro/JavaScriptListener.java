package com.accelerator.metro;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class JavaScriptListener {

    public static final int TYPE_CODE_START = 0;
    public static final int TYPE_CODE_END = 1;

    private Context context;
    private AlertDialog.Builder dialog;

    private onPointClickListener listener;

    public interface onPointClickListener {
        void onClick(String name, String id, String priceId, int type);
    }

    public void setPointClickListener(onPointClickListener lintener) {
        this.listener = lintener;
    }

    public JavaScriptListener(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context);
    }

    @JavascriptInterface
    public void getPointData(final String content, final String id, final String priceId) {

        dialog.setTitle(content);
        dialog.setMessage(R.string.station_star_or_end);
        dialog.setCancelable(true);

        dialog.setNeutralButton(R.string.station_get_start, (dialogInterface, i) -> {
            if (listener != null) {
                listener.onClick(content, id, priceId, TYPE_CODE_START);
            }
        });

        dialog.setNegativeButton(R.string.station_get_end, (dialogInterface, i) -> {
            if (listener != null) {
                listener.onClick(content, id, priceId, TYPE_CODE_END);
            }
        });

        dialog.show();

    }


}
