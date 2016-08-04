package com.accelerator.metro.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.accelerator.metro.Config;
import com.accelerator.metro.MetroApp;
import com.accelerator.metro.bean.Order;
import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.FinishOrderContract;
import com.accelerator.metro.model.FinishOrderModel;
import com.accelerator.metro.utils.RxManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/8/2.
 */
public class FinishOrderPresenter extends RxManager implements FinishOrderContract.Presenter {

    private static final String TAG=FinishOrderPresenter.class.getName();

    private FinishOrderContract.Model model;
    private FinishOrderContract.View view;
    private List<List<Order.ElseInfoBean>> datas;
    private Subscription s2;

    public FinishOrderPresenter(FinishOrderContract.View view) {
        this.view = view;
        model = new FinishOrderModel();
    }

    @Override
    public void getFinishOrder() {

        datas = new ArrayList<>();

        Subscription s1 = model.getNoTripOrder()
                .subscribe(new Observer<Order>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onGetNoTripFail(e.getMessage());
                    }

                    @Override
                    public void onNext(Order order) {

                        int code = order.getIs_ok();
                        switch (code) {
                            case 0:
                                List<Order.ElseInfoBean> empty = new ArrayList<>();
                                datas.add(empty);
                                saveSessionId(order.getUser_id(), order.getSession_id());
                                break;
                            case 1:
                                datas.add(order.getElse_info());
                                saveSessionId(order.getUser_id(), order.getSession_id());
                                break;
                            case 411:
                                view.reLogin();
                                break;
                        }

                        s2 = model.getHistoryOrder()
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

                                        int code = order.getIs_ok();
                                        switch (code) {
                                            case 0:
                                                List<Order.ElseInfoBean> empty = new ArrayList<>();
                                                datas.add(empty);
                                                saveSessionId(order.getUser_id(), order.getSession_id());
                                                break;
                                            case 1:
                                                datas.add(order.getElse_info());
                                                saveSessionId(order.getUser_id(), order.getSession_id());
                                                break;
                                            case 411:
                                                view.reLogin();
                                                break;
                                        }

                                        view.onSucceed(datas);

                                    }
                                });

                        addSub(s2);
                    }
                });

        addSub(s1);
    }

    @Override
    public void refundOrder(String orderSn) {

        Subscription s = model.refundOrder(orderSn)
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                        view.onRefundConplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onRefundFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {
                        switch (resultCode.getIs_ok()){
                            case 1:
                                view.onRefundSucceed(resultCode);
                                break;
                            default:
                                Log.e(TAG,"退票错误，错误码："+resultCode.getIs_ok());
                                break;
                        }
                    }
                });

        addSub(s);

    }

    @Override
    public void deletedOrder(String orderSn) {

        Subscription s = model.deletedOrder(orderSn)
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                        view.onDeleteConplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onDeleteFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {

                        switch (resultCode.getIs_ok()){
                            case 1:
                                view.onDeleteSucceed(resultCode);
                                break;
                            default:
                                Log.e(TAG,"删除票错误，错误码："+resultCode.getIs_ok());
                                break;
                        }

                    }
                });

        addSub(s);

    }


    private void saveSessionId(String userId, String sessionId) {
        SharedPreferences sp = MetroApp.getContext().getSharedPreferences(Config.USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Config.USER_ID, userId);
        editor.putString(Config.USER_SESSION, sessionId);
        editor.apply();
    }

    @Override
    public void unSubscription() {
        unSub();
        view = null;
    }

}
