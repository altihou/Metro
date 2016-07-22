package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/21.
 */
public interface FeedbackContract {

    interface Model extends BaseModel {
        Observable<ResultCode> feedback(String content);
    }

    interface View extends BaseView<ResultCode> {
        void reLogin();
    }

    interface Presenter extends BasePresenter {
        void feedback(String content);
    }

}
