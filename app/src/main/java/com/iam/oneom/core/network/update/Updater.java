package com.iam.oneom.core.network.update;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.iam.oneom.core.SecureStore;
import com.iam.oneom.core.rx.RxBus;
import com.iam.oneom.core.rx.event.UpdateFinishedEvent;

import java.util.Date;

/**
 * Created by iam on 07.04.17.
 */

public final class Updater extends IntentService{

    private static final String TAG = Updater.class.getSimpleName();
    private static final String PHASES_EXTRA = "PHASES_EXTRA";

    public static void update(Context context) {
        update(context, UpdatePhase.values());
    }

    public static void update(Context context, UpdatePhase... phases) {
        Intent intent = new Intent(context, Updater.class);
        intent.putExtra(PHASES_EXTRA, UpdatePhase.names(phases));
        context.startService(intent);
    }

    public Updater() {
        super("Update_data_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        UpdatePhase[] phases = UpdatePhase.phases(intent.getStringArrayExtra(PHASES_EXTRA));
        for (UpdatePhase phase : phases) {
            try {
                phase.body().run();
            } catch (UpdateException e) {
                RxBus.INSTANCE.post(new UpdateFinishedEvent(false, e));
            }
        }
        SecureStore.setEpisodesLastUpdated(new Date().getTime());
        RxBus.INSTANCE.post(new UpdateFinishedEvent(true));
    }
}
