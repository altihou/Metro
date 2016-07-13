package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.UserInfo;
import com.accelerator.metro.bean.UserLogin;

import rx.Observable;

/**
 * Created by Nicholas on 2016/5/17.
 *
 * MVP Contract
 */
public interface LoginContract {

    interface Model extends BaseModel {
        Observable<UserInfo> login(UserLogin userLogin);
    }

    interface View extends BaseView<UserInfo> {}

    interface Presenter extends BasePresenter {
        void login(UserLogin userLogin);
    }

}
