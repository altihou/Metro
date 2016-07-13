package com.accelerator.metro.presenter;

import com.accelerator.metro.bean.Message;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.contract.RegisterContract;
import com.accelerator.metro.model.RegisterModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/9.
 */
public class RegisterPresenter extends RxManager implements RegisterContract.Presnter {

    private RegisterContract.Model model;
    private RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        model=new RegisterModel();
    }

    @Override
    public void register(UserInfo userInfo,String path) {

        Subscription s = model.register(userInfo, path)
                .subscribe(new Observer<Message>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(Message message) {
                        view.onSucceed(message);
                    }
                });

        addSub(s);
    }

    @Override
    public void unSubscription() {
        view=null;
        unSub();
    }
}
