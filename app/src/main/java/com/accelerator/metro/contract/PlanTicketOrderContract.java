package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.PlanTicket;

import rx.Observable;

/**
 * Created by Nicholas on 2016/8/8.
 */
public interface PlanTicketOrderContract {

    interface Model extends BaseModel {
        Observable<PlanTicket> getPlanTicket();
    }

    interface View extends BaseView<PlanTicket> {
        void reLogin();
        void orderEmpty(PlanTicket result);
    }

    interface Presenter extends BasePresenter {
        void getPlanTicket();
    }
}
