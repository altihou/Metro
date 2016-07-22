package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.ResultCode;
import com.accelerator.metro.contract.FeedbackContract;
import com.accelerator.metro.model.FeedbackModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class FeedbackPresenter extends RxManager implements FeedbackContract.Presenter {

    private static final String TAG=FeedbackPresenter.class.getName();

    private FeedbackContract.Model model;
    private FeedbackContract.View view;

    public FeedbackPresenter(FeedbackContract.View view) {
        this.view = view;
        model=new FeedbackModel();
    }

    @Override
    public void feedback(String content) {

        Subscription s = model.feedback(content)
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
                        int code = resultCode.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(resultCode);
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "反馈错误，错误码：" + code);
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
