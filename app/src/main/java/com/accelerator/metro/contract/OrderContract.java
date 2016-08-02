package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/23.
 */
public interface OrderContract {

    interface Model extends BaseModel {
        Observable<Order> getOrder(String p, String type);
        Observable<ResultCode> cancelOrder(String orderNum);
    }

    interface View extends BaseView<Order> {
        void noOrder();
        void cancelCompleted();
        void cancelFailure(String err);
        void cancelError();
        void cancelSucceed(ResultCode resultCode);
    }

    interface Presenter extends BasePresenter {
        void getOrder(String p, String type);
        void cancelOrder(String orderNum);
    }
}
