package com.yaya.myvr.app;

import java.util.List;

import rx.Subscription;

/**
 * Created by admin on 2017/5/3.
 */

public class RxManager {
    /**
     * 取消订阅
     * @param subscriptions
     */
    public static void unsubscribe(List<Subscription> subscriptions) {
        if (subscriptions == null) {
            return;
        }

        if (subscriptions.size() > 0) {
            return;
        }

        for (Subscription subscription : subscriptions) {
            if (subscription != null && subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

    }
}
