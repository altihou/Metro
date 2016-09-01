package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.ResultCode;

import rx.Observable;

/**
 * Created by Nicholas on 2016/8/7.
 */
public interface SignOutContract {

    interface Model extends BaseModel {
        Observable<ResultCode> signOut();
    }

    interface View extends BaseView<ResultCode> {
    }

    interface Presenter extends BasePresenter {
        void signOut();
    }
}
