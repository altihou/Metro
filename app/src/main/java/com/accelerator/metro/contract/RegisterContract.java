package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.User;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/9.
 */
public interface RegisterContract {

    interface Model extends BaseModel{
        Observable<User> register(String phone, String pwd1,String pwd2, String path);
    }

    interface View extends BaseView<User>{}

    interface Presnter extends BasePresenter{
        void register(String phone, String pwd1,String pwd2, String path);
    }

}
