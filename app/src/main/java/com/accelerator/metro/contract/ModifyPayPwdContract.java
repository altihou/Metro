package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/22.
 */
public interface ModifyPayPwdContract {

    interface Model extends BaseModel {
        Observable<ResultCode> modifyPayPwd(String oldPwd, String newPwd1, String newPwd2);
        Observable<ResultCode> checkPayPwd();
    }

    interface View extends BaseView<ResultCode> {
        void reLogin();
        void checkSucceed(ResultCode code);
        void checkFailure(String err);
        void checkCompleted();
    }


    interface Presenter extends BasePresenter {
        void modifyPayPwd(String oldPwd,String newPwd1,String newPwd2);
        void checkPayPwd();
    }
}
