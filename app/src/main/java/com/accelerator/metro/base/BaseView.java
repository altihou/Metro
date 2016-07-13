package com.accelerator.metro.base;

/**
 * Created by zoom on 2016/5/17.
 */
public interface BaseView<T> {
    void onSucceed(T values);
    void onFailure(String err);
    void onCompleted();
}
