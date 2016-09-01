package com.accelerator.metro.presenter;

import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.SignOutContract;
import com.accelerator.metro.model.SignOutModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/8/7.
 */
public class SignOutPresenter extends RxManager implements SignOutContract.Presenter {


    private SignOutContract.View view;
    private SignOutContract.Model model;

    public SignOutPresenter(SignOutContract.View view) {
        this.view = view;
        model = new SignOutModel();
    }

    @Override
    public void signOut() {
        Subscription s = model.signOut()
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
                        view.onSucceed(resultCode);
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
