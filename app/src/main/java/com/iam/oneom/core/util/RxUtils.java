package com.iam.oneom.core.util;

import rx.Subscription;

/**
 * Created by iam on 19.05.17.
 */

public final class RxUtils {

    private RxUtils() {}

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
