package com.accelerator.metro.presenter;

import android.util.Log;

import com.accelerator.metro.bean.ModifyUser;
import com.accelerator.metro.contract.ModifyContract;
import com.accelerator.metro.model.ModifyModel;
import com.accelerator.metro.utils.RxManager;

import rx.Observer;
import rx.Subscription;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ModifyPresenter extends RxManager implements ModifyContract.Presenter {

    private static final String TAG=MinePresenter.class.getName();

    private ModifyContract.Model model;
    private ModifyContract.View view;

    public ModifyPresenter(ModifyContract.View view) {
        this.view = view;
        model=new ModifyModel();
    }

    @Override
    public void modify(String nickname, String sex, String avatarPath) {

        Subscription s = model.modify(nickname, sex, avatarPath)
                .subscribe(new Observer<ModifyUser>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(ModifyUser modifyUser) {

                        if (modifyUser.getUser_id().equals("-1")) {
                            view.reLogin();
                            return;
                        }

                        int code = modifyUser.getIs_ok();
                        switch (code) {
                            case 1:
                                view.onSucceed(modifyUser);
                                break;
                            case 411:
                                view.reLogin();
                                break;
                            default:
                                Log.e(TAG, "修改错误，错误码：" + code);
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
