package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.UserRegister;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/9.
 */
public interface RegisterContract {

    interface Model extends BaseModel{
        Observable<UserRegister> register(String phone, String pwd,String path);
    }

    interface View extends BaseView<UserRegister>{}

    interface Presnter extends BasePresenter{
        void register(String phone, String pwd,String path);
    }

}
