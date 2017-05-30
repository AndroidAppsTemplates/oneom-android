package com.iam.oneom.core.rx;

import rx.Subscription;

/**
 * Created by iam on 19.05.17.
 */

public final class RxUtils {

    private RxUtils() {}

    public static void unsubscribe(Subscription subscription) {
        if (isSubscribed(subscription)) {
            subscription.unsubscribe();
        }
    }

    public static void unsubscribe(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            unsubscribe(subscription);
        }
    }

    public static boolean isSubscribed(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }

}
