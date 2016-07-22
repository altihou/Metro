package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/21.
 */
public interface ModifyLoginPwdContract {

    interface Model extends BaseModel {
        Observable<ResultCode> modifyLoginPwd(String oldPwd,String newPwd1,String newPwd2);
    }

    interface View extends BaseView<ResultCode> {
        void reLogin();
    }

    interface Presenter extends BasePresenter {
        void modifyLoginPwd(String oldPwd,String newPwd1,String newPwd2);
    }
}
