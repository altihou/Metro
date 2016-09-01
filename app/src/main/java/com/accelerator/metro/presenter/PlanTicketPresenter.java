package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.PlanTicketContract;
import com.accelerator.metro.model.PlanTicketModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class PlanTicketPresenter extends RxManager implements PlanTicketContract.Presenter {

    private static final String TAG = PlanTicketPresenter.class.getName();

    private PlanTicketContract.Model model;
    private PlanTicketContract.View view;

    public PlanTicketPresenter(PlanTicketContract.View view) {
        this.view = view;
        model = new PlanTicketModel();
    }

    @Override
    public void autoBuy(int type, String startPoint, String endPoint, String workTime, String days) {

        Subscription s = model.autoBuy(type, startPoint, endPoint, workTime, days)
                .subscribe(new Observer<ResultCode>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultCode resultCode) {


                        if (resultCode.getUser_id().equals("-1")) {
                            view.reLogin();
                            return;
                        }

                        int code = resultCode.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(resultCode);
                                break;
                            case 0:
                                view.orderExist(resultCode);
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "自动买票错误，错误码:" + code);
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
