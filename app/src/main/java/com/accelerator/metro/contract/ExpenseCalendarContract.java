package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.Order;

import rx.Observable;

/**
 * Created by Nicholas on 2016/8/4.
 */
public interface ExpenseCalendarContract {

    int REFRESH=0;
    int MORE=1;

    interface Model extends BaseModel {
        Observable<Order> getRechargeOrder(String p);
    }

    interface View extends BaseView<Order> {
        void reLogin();
        void empty();
        void loadMoreSucceed(Order order);
    }

    interface Presenter extends BasePresenter {
        void getRechargeOrder(String p,int load);
    }
}
