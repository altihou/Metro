package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.OrderContract;
import com.accelerator.metro.model.OrderModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/23.
 */
public class OrderPresenter extends RxManager implements OrderContract.Presenter {

    private static final String TAG=OrderPresenter.class.getName();

    private OrderContract.View view;
    private OrderContract.Model model;

    public OrderPresenter(OrderContract.View view) {
        this.view = view;
        model=new OrderModel();
    }

    @Override
    public void getOrder(String p, String type) {

        Subscription s = model.getOrder(p, type)
                .subscribe(new Observer<Order>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(Order order) {
                        int code=order.getIs_ok();
                        switch (code){
                            case 1:
                                view.onSucceed(order);
                                break;
                            case 0:
                                view.noOrder();
                                break;
                            default:
                                Log.e(TAG,"获取订单错误，错误码："+code);
                                break;
                        }
                    }
                });
        addSub(s);
    }

    @Override
    public void cancelOrder(String orderNum) {

        Subscription s = model.cancelOrder(orderNum)
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                        view.cancelCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.cancelFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {

                        int code=resultCode.getIs_ok();
                        switch (code){
                            case 1:
                            view.cancelSucceed(resultCode);
                                break;
                            case -1:
                                view.cancelError();
                                break;
                            default:
                                Log.e(TAG,"取消订单失败，错误码："+code);
                                break;
                        }
                    }
                });
        addSub(s);
    }

    @Override
    public void unSubscription() {
        unSub();
        view=null;
    }
}
