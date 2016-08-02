package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.User;

import rx.Observable;

/**
 * Created by Nicholas on 2016/5/17.
 *
 * MVP Contract
 */
public interface LoginContract {

    interface Model extends BaseModel {
        Observable<User> login(String phone,String pwd);
    }

    interface View extends BaseView<User> {
        void accountNotExist();
        void pwdError();
    }

    interface Presenter extends BasePresenter {
        void login(String phone,String pwd);
    }

}
