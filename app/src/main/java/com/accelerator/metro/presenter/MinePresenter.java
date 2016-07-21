package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.MineInfo;
import com.accelerator.metro.contract.MineContract;
import com.accelerator.metro.model.MineModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/20.
 */
public class MinePresenter extends RxManager implements MineContract.Presenter {

    private static final String TAG=MinePresenter.class.getName();

    private MineContract.Model model;
    private MineContract.View view;

    public MinePresenter(MineContract.View view) {
        this.view = view;
        model=new MineModel();
    }

    @Override
    public void getMine() {

        Subscription s = model.getMine().subscribe(new Observer<MineInfo>() {
            @Override
            public void onCompleted() {
                view.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                view.onFailure(e.getMessage());
            }

            @Override
            public void onNext(MineInfo mineInfo) {

                if (mineInfo.getIs_ok()!=1){
                    Log.e(TAG,"获取错误，错误码："+mineInfo.getIs_ok());
                    return;
                }

                view.onSucceed(mineInfo);
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
