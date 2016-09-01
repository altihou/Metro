package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/8/8.
 */
public interface PlanTicketContract {

    interface Model extends BaseModel{
        Observable<ResultCode> autoBuy(int type,String startPoint,String endPoint,String workTime,String days);
    }

    interface View extends BaseView<ResultCode>{
        void reLogin();
        void orderExist(ResultCode resultCode);
    }

    interface Presenter extends BasePresenter{
        void autoBuy(int type,String startPoint,String endPoint,String workTime,String days);
    }

}
