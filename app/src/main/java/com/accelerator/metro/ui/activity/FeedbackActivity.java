package com.accelerator.metro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.accelerator.metro.R;
import com.accelerator.metro.base.BaseDialogActivity;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.FeedbackContract;
import com.accelerator.metro.presenter.FeedbackPresenter;
import com.accelerator.metro.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends BaseDialogActivity implements FeedbackContract.View {

    private static final String TAG=FeedbackActivity.class.getName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.feedback_edit_content)
    EditText editContent;
    @Bind(R.id.feedback_btn_ok)
    Button btnOk;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private FeedbackPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presenter=new FeedbackPresenter(this);

    }

    @OnClick(R.id.feedback_btn_ok)
    public void onOkClick(View view){

        String content=editContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)){
            Snackbar.make(coordinatorLayout, R.string.feedback_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }

        setDialogMsg(R.string.WAIT);
        setDialogCancelable(false);
        setDialogShow();

        presenter.feedback(content);
    }

    @Override
    public void reLogin() {
        ToastUtil.Short(R.string.login_relogin);
        startActivity(new Intent(this, LoginActivity.class));
        setDialogDismiss();
    }

    @Override
    public void onSucceed(ResultCode values) {
        ToastUtil.Short(R.string.feedback_succeed);
        finish();
    }

    @Override
    public void onFailure(String err) {
        Log.e(TAG,err);
        setDialogDismiss();
        Snackbar.make(coordinatorLayout, R.string.feedback_failure, Snackbar.LENGTH_SHORT).show();
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
