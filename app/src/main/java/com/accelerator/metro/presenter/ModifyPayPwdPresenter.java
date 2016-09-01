package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.ModifyPayPwdContract;
import com.accelerator.metro.model.ModifyPayPwdModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class ModifyPayPwdPresenter extends RxManager implements ModifyPayPwdContract.Presenter {

    private static final String TAG = ModifyPayPwdPresenter.class.getName();

    private ModifyPayPwdContract.Model model;
    private ModifyPayPwdContract.View view;

    public ModifyPayPwdPresenter(ModifyPayPwdContract.View view) {
        this.view = view;
        model = new ModifyPayPwdModel();
    }

    @Override
    public void modifyPayPwd(String oldPwd, String newPwd1, String newPwd2) {

        Subscription s = model.modifyPayPwd(oldPwd, newPwd1, newPwd2)
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

                        if (resultCode.getUser_id().equals("-1")) {
                            view.reLogin();
                            return;
                        }

                        int code = resultCode.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(resultCode);
                                break;
                            case -1:
                                view.oldPwdError();
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "修改支付密码错误，错误码：" + code);
                                break;
                        }
                    }
                });
        addSub(s);

    }

    @Override
    public void checkPayPwd() {

        Subscription s = model.checkPayPwd()
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                        view.checkCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.checkFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {

                        if (resultCode.getUser_id().equals("-1")) {
                            view.reLogin();
                            return;
                        }

                        int code = resultCode.getIs_ok();
                        switch (code) {
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                view.checkSucceed(resultCode);
                                break;
                        }
                    }
                });
        addSub(s);

    }

    @Override
    public void unSubscription() {
        unSub();
        view = null;
    }
}
