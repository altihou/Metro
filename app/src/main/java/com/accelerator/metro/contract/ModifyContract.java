package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ModifyUser;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/21.
 */
public interface ModifyContract {

    interface Model extends BaseModel{
        Observable<ModifyUser> modify(String nickname,String sex,String avatarPath);
    }

    interface View extends BaseView<ModifyUser>{
        void reLogin();
    }

    interface Presenter extends BasePresenter{
        void modify(String nickname,String sex,String avatarPath);
    }
}
