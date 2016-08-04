package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;

import java.util.List;

import rx.Observable;

/**
 * Created by Nicholas on 2016/8/2.
 */
public interface FinishOrderContract {

    interface Model extends BaseModel {
        Observable<Order> getNoTripOrder();
        Observable<Order> getHistoryOrder();
        Observable<ResultCode> refundOrder(String orderSn);
        Observable<ResultCode> deletedOrder(String orderSn);
    }

    interface View extends BaseView<List<List<Order.ElseInfoBean>>> {
        void onGetNoTripFail(String err);
        void reLogin();

        void onRefundSucceed(ResultCode resultCode);
        void onRefundFailure(String err);
        void onRefundConplete();

        void onDeleteSucceed(ResultCode resultCode);
        void onDeleteFailure(String err);
        void onDeleteConplete();
    }

    interface Presenter extends BasePresenter {
        void getFinishOrder();
        void refundOrder(String orderSn);
        void deletedOrder(String orderSn);
    }
}
