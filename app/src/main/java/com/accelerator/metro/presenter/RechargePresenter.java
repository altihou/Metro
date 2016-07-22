package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.Recharge;
import com.accelerator.metro.contract.RechargeContract;
import com.accelerator.metro.model.RechargeModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class RechargePresenter extends RxManager implements RechargeContract.Presenter {

    private static final String TAG=RechargePresenter.class.getName();

    private RechargeContract.Model model;
    private RechargeContract.View view;

    public RechargePresenter(RechargeContract.View view) {
        this.view = view;
        model = new RechargeModel();
    }

    @Override
    public void recharge(String money) {

        Subscription s = model.recharge(money)
                .subscribe(new Observer<Recharge>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(Recharge recharge) {
                        int code = recharge.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(recharge);
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "充值错误，错误码：" + code);
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
