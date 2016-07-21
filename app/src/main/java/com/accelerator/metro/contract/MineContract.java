package com.accelerator.metro.contract;

import com.accelerator.metro.base.BaseModel;
import com.accelerator.metro.base.BasePresenter;
import com.accelerator.metro.base.BaseView;
import com.accelerator.metro.bean.MineInfo;

import rx.Observable;

/**
 * Created by Nicholas on 2016/7/10.
 */
public interface MineContract {

    interface Model extends BaseModel{
        Observable<MineInfo> getMine();
    }

    interface View extends BaseView<MineInfo>{
        void reLogin();
    }

    interface Presenter extends BasePresenter{
        void getMine();
    }
}
