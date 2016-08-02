package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.User;
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

    private static final String TAG=LoginPresenter.class.getName();

    private LoginContract.Model model;
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        model=new LoginModel();
    }

    @Override
    public void login(String phone,String pwd) {

        Subscription s = model.login(phone,pwd)
                .subscribe(new Subscriber<User>() {

                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        switch (user.getIs_ok()){
                            case 1:
                                view.onSucceed(user);
                                break;
                            case -2:
                                view.accountNotExist();
                                break;
                            case -1:
                            case -3:
                                view.pwdError();
                                break;
                            default:
                                Log.e(TAG,"登录错误，错误码："+user.getIs_ok());
                                break;
                        }
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
