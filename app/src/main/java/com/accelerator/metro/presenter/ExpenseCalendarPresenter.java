package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.Order;
import com.accelerator.metro.contract.ExpenseCalendarContract;
import com.accelerator.metro.model.ExpenseCalendarModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/8/4.
 */
public class ExpenseCalendarPresenter extends RxManager
        implements ExpenseCalendarContract.Presenter {

    private static final String TAG = ExpenseCalendarPresenter.class.getName();

    private ExpenseCalendarContract.Model model;
    private ExpenseCalendarContract.View view;

    public ExpenseCalendarPresenter(ExpenseCalendarContract.View view) {
        this.view = view;
        model = new ExpenseCalendarModel();
    }

    @Override
    public void getRechargeOrder(String p, final int load) {
        Subscription s = model.getRechargeOrder(p)
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

                        if (order.getUser_id().equals("-1")) {
                            view.reLogin();
                            return;
                        }

                        int code = order.getIs_ok();
                        switch (code) {
                            case 1:

                                if (load == ExpenseCalendarContract.MORE) {
                                    view.loadMoreSucceed(order);
                                } else if (load == ExpenseCalendarContract.REFRESH) {
                                    view.onSucceed(order);
                                }

                                break;
                            case 0:
                                view.empty();
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "获取充值订单错误，错误码：" + code);
                                break;
                        }

                    }
                });
        addSub(s);
    }

    @Override
    public void unSubscription() {
        unSub();
        view = null;
    }
}
