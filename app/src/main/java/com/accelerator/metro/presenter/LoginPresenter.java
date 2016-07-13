package com.accelerator.metro.presenter;

import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.bean.UserLogin;
import com.accelerator.metro.contract.LoginContract;
import com.accelerator.metro.model.LoginModel;
import com.accelerator.metro.utils.RxManager;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/5/17.
 *
 * Presenter实现类
 */
public class LoginPresenter extends RxManager implements LoginContract.Presenter {

    private LoginContract.Model model;
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        model=new LoginModel();
    }

    /**
     * 从服务器请求登录，返回用户信息并实现view的接口
     * @param userLogin UserLogin
     */
    @Override
    public void login(UserLogin userLogin) {

        Subscription s = model.login(userLogin)
                .subscribe(new Subscriber<UserInfo>() {

                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        view.onSucceed(userInfo);
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
