package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.CommitOrder;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/22.
 */
public interface CommitOrderContract {

    interface Model extends BaseModel {
        Observable<CommitOrder> commitOrder(String start,String end);
    }

    interface View extends BaseView<CommitOrder> {
        void reLogin();
        void existNotPayOrder();
    }

    interface Presenter extends BasePresenter {
        void commitOrder(String start,String end);
    }

}
