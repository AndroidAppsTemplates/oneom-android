package com.iam.oneom.pages.main.episodes;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.Toolbar;

import com.iam.oneom.BR;
import com.iam.oneom.R;
import com.iam.oneom.binding.episode.EpisodeDiffObservableDbCallback;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.rx.RxUtils;
import com.iam.oneom.util.OneOmUtil;
import com.iam.oneom.util.Time;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.Date;

import io.realm.Sort;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;
import rx.Subscription;

/**
 * Created by iam on 09.05.17.
 */

public class EpisodesViewModel {

    public final DiffObservableList<Episode> items;
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableField<String> lastUpdated;
    public final ItemBinding<Episode> itemBinding = ItemBinding.of(BR.ep, R.layout.episodes_list_item_bind);

    private Subscription subscription;
    private Subscription downloadSub;

    public EpisodesViewModel(Context context) {
        lastUpdated = new ObservableField<>(Time.episodesLastUpdated(context));
        items = new DiffObservableList<>(new EpisodeDiffObservableDbCallback());
    }

    public void onResume() {
        if (subscription != null && !subscription.isUnsubscribed()) return;

        subscription = DbHelper.where(Episode.class)
                .equalTo(Episode.IS_SCHEDULE, true)
                .lessThan(Episode.AIRDATE, new Date().getTime())
                .findAllSortedAsync(
                        new String[]{Episode.AIRDATE, Episode.UPDATED_AT},
                        new Sort[]{Sort.DESCENDING, Sort.DESCENDING}
                )
                .asObservable()
                .subscribe(episodes -> {
                    items.update(episodes);
                });
    }

    public void onDestroy() {
        RxUtils.unsubscribe(subscription);
        RxUtils.unsubscribe(downloadSub);
    }


    @BindingAdapter("episodesViewModel")
    public static void setSwipyRefreshListener(SwipyRefreshLayout view, EpisodesViewModel episodesViewModel) {
        view.setOnRefreshListener(direction -> {
            episodesViewModel.loading.set(true);
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                long latestDate = episodesViewModel.getLatestDate();
                episodesViewModel.downloadSub =
                        Web.instance.getLastEpisodesByDate(latestDate + 1, latestDate + Time.MONTH)
                                .onErrorReturn(throwable -> null)
                                .subscribe(epsDateResponse -> {
                                    DbHelper.insertAll(OneOmUtil.setSchedule(epsDateResponse.getEps()));
                                    episodesViewModel.loading.set(false);
                                });
            } else {
                long earliestDate = episodesViewModel.getEarliestDate();
                episodesViewModel.downloadSub =
                        Web.instance.getLastEpisodesByDate(earliestDate - Time.MONTH, earliestDate - 1)
                                .onErrorReturn(throwable -> null)
                                .subscribe(epsDateResponse -> {
                                    DbHelper.insertAll(OneOmUtil.setSchedule(epsDateResponse.getEps()));
                                    episodesViewModel.loading.set(false);
                                });
            }
        });
    }

    private long getEarliestDate() {
        return OneOmUtil.earliest(items) == null ? 0 : OneOmUtil.earliest(items).getAirdate();
    }


    private long getLatestDate() {
        return OneOmUtil.latest(items) == null ? Long.MAX_VALUE : OneOmUtil.latest(items).getAirdate();
    }

    @BindingAdapter("onMenuItemClick")
    public static void setOnMenuItemClickListener(Toolbar toolbar, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }
}