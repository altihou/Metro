package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.ModifyLoginPwdContract;
import com.accelerator.metro.model.ModifyLoginPwdModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ModifyLoginPwdPresenter extends RxManager implements ModifyLoginPwdContract.Presenter {

    private static final String TAG=ModifyLoginPwdPresenter.class.getName();

    private ModifyLoginPwdContract.View view;
    private ModifyLoginPwdContract.Model model;

    public ModifyLoginPwdPresenter(ModifyLoginPwdContract.View view) {
        this.view = view;
        model=new ModifyLoginPwdModel();
    }

    @Override
    public void modifyLoginPwd(String oldPwd, String newPwd1, String newPwd2) {

        Subscription s = model.modifyLoginPwd(oldPwd, newPwd1, newPwd2)
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {

                        int code = resultCode.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(resultCode);
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "修改登录密码错误，错误码：" + code);
                                break;
                        }
                    }
                });

        addSub(s);
    }

    @Override
    public void unSubscription() {
        unSub();
        view=null;
    }
}
