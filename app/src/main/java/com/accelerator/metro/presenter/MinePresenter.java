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

    private static final String TAG = MinePresenter.class.getName();

    private MineContract.Model model;
    private MineContract.View view;

    public MinePresenter(MineContract.View view) {
        this.view = view;
        model = new MineModel();
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

                int code=mineInfo.getIs_ok();
                switch (code){
                    case 1:
                        view.onSucceed(mineInfo);
                        break;
                    case 411:
                        view.reLogin();
                        break;
                    default:
                        Log.e(TAG,"获取个人信息错误，错误码："+code);
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
