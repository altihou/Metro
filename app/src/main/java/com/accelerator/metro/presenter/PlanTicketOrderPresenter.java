package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.PlanTicket;
import com.accelerator.metro.contract.PlanTicketOrderContract;
import com.accelerator.metro.model.PlanTicketOrderModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class PlanTicketOrderPresenter extends RxManager implements PlanTicketOrderContract.Presenter {

    private static final String TAG = PlanTicketOrderPresenter.class.getName();

    private PlanTicketOrderContract.View view;
    private PlanTicketOrderContract.Model model;

    public PlanTicketOrderPresenter(PlanTicketOrderContract.View view) {
        this.view = view;
        model = new PlanTicketOrderModel();
    }

    @Override
    public void getPlanTicket() {

        Subscription s = model.getPlanTicket()
                .subscribe(new Observer<PlanTicket>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(PlanTicket planTicket) {
                        if (planTicket.getUser_id().equals("-1")) {
                            view.reLogin();
                            return;
                        }

                        switch (planTicket.getIs_ok()) {
                            case 1:
                                view.onSucceed(planTicket);
                                break;
                            case 0:
                                view.orderEmpty(planTicket);
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "获取自动购票订单错误，错误码：" + planTicket.getIs_ok());
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
