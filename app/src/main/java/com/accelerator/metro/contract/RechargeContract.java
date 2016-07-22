package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.Recharge;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/21.
 */
public interface RechargeContract {

    interface Model extends BaseModel{
        Observable<Recharge> recharge(String money);
    }

    interface View extends BaseView<Recharge>{
        void reLogin();
    }

    interface Presenter extends BasePresenter{
        void recharge(String money);
    }
}
