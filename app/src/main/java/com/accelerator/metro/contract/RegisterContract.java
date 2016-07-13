package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.Message;
import com.accelerator.metro.bean.UserInfo;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/9.
 */
public interface RegisterContract {

    interface Model extends BaseModel{
        Observable<Message> register(UserInfo userInfo,String path);
    }

    interface View extends BaseView<Message>{}

    interface Presnter extends BasePresenter{
        void register(UserInfo userInfo,String path);
    }

}
