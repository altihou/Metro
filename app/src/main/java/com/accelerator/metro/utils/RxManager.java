package com.accelerator.metro.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Nicholas on 2016/5/17.
 *
 * 管理订阅者（Subscription）的生命周期基类
 */
public abstract class RxManager {

    private CompositeSubscription cs;

    /**
     * 添加到队列
     * @param s Subscription
     */
    protected void addSub(Subscription s){
        if (cs==null){
            cs=new CompositeSubscription();
        }
        cs.add(s);
    }

    /**
     * 解除订阅（Subscription）,防止RxJava导致的内存泄漏
     */
    protected void unSub(){
        if (cs!=null){
            cs.unsubscribe();
        }
    }

    /**
     * 子类必须实现的方法
     */
    public abstract void unSubscription();
}
