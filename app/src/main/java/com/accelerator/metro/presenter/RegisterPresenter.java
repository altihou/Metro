package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.User;
import com.accelerator.metro.contract.RegisterContract;
import com.accelerator.metro.model.RegisterModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/9.
 */
public class RegisterPresenter extends RxManager implements RegisterContract.Presnter {

    private static final String TAG=RegisterPresenter.class.getName();

    private RegisterContract.Model model;
    private RegisterContract.View view;

    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        model=new RegisterModel();
    }

    @Override
    public void register(String phone,String pwd1,String pwd2, String path) {

        Subscription s = model.register(phone, pwd1,pwd2 ,path)
                .subscribe(new Observer<User>() {
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
                        int code=user.getIs_ok();
                        switch (code){
                            case 1:
                                view.onSucceed(user);
                                break;
                            case -2:
                                view.accountExist();
                                break;
                            case -5:
                                view.pwdNotEquals();
                                break;
                            default:
                                Log.e(TAG, "注册错误，错误码：" + user.getIs_ok());
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
