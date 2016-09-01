package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/23.
 *
 */
public interface PayOrderContract {

    interface Model extends BaseModel {
        Observable<ResultCode> payOrder(String orderNum,String userPayPwd,String money);
    }

    interface View extends BaseView<ResultCode> {
        void reLogin();
        void setPayPwd();
        void orderError();
        void payPwdError();
        void notSufficientFunds();
    }

    interface Presenter extends BasePresenter {
        void payOrder(String orderNum,String userPayPwd,String money);
    }

}
