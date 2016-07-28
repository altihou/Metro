package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.CommitOrder;
import com.accelerator.metro.contract.CommitOrderContract;
import com.accelerator.metro.model.CommitOrderModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class CommitOrderPresenter extends RxManager implements CommitOrderContract.Presenter {

    private static final String TAG=CommitOrderPresenter.class.getName();

    private CommitOrderContract.Model model;
    private CommitOrderContract.View view;

    public CommitOrderPresenter(CommitOrderContract.View view) {
        this.view = view;
        model=new CommitOrderModel();
    }

    @Override
    public void commitOrder(String start, String end,String money) {

        Subscription s = model.commitOrder(start, end,money)
                .subscribe(new Observer<CommitOrder>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(CommitOrder commitOrder) {
                        int code = commitOrder.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(commitOrder);
                                break;
                            case -1:
                                view.existNotPayOrder();
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "提交订单错误，错误码：" + code);
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
