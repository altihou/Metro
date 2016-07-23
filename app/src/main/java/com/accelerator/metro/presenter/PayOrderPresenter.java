package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.PayOrderContract;
import com.accelerator.metro.model.PayOrderModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/23.
 */
public class PayOrderPresenter extends RxManager implements PayOrderContract.Presenter {

    private static final String TAG=PayOrderPresenter.class.getName();

    private PayOrderContract.View view;
    private PayOrderContract.Model model;

    public PayOrderPresenter(PayOrderContract.View view) {
        this.view = view;
        model=new PayOrderModel();
    }

    @Override
    public void payOrder(String orderNum, String userPayPwd) {

        Subscription s = model.payOrder(orderNum, userPayPwd)
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
                            case -6:
                                view.setPayPwd();
                                break;
                            case -1:
                                view.orderError();
                                break;
                            case -2:
                                view.payPwdError();
                                break;
                            case -3:
                                view.notSufficientFunds();
                                break;
                            default:
                                Log.e(TAG, "支付订单错误，错误码：" + code);
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
