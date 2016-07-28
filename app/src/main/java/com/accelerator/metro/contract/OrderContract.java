package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.Order;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/23.
 */
public interface OrderContract {

    interface Model extends BaseModel {
        Observable<Order> getOrder(String p, String type);
    }

    interface View extends BaseView<Order> {
        void reLogin();
        void noOrder();
    }

    interface Presenter extends BasePresenter {
        void getOrder(String p, String type);
    }
}
