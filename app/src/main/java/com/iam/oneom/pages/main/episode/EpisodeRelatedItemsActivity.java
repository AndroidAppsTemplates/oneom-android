package com.iam.oneom.pages.main.episode;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.databinding.EpisodeSearchSourcesActivityBinding;
import com.iam.oneom.pages.main.viewmodel.EpisodeViewModel;
import com.iam.oneom.view.adapter.ViewPagerAdapter;

public class EpisodeRelatedItemsActivity extends AppCompatActivity {

    private static final String EP_ID_EXTRA = "EP_ID_EXTRA";


    public static void start(Context context, long epId) {
        Intent intent = new Intent(context, EpisodeRelatedItemsActivity.class);
        intent.putExtra(EP_ID_EXTRA, epId);
        context.startActivity(intent);
    }

    public static void start(View view, long epId) {
        start(view.getContext(), epId);
    }

    private EpisodeViewModel episodeViewModel;
    private EpisodeSearchSourcesActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.episode_search_sources_activity);

        Episode episode = DbHelper
                .where(Episode.class)
                .equalTo("id", getIntent().getLongExtra(EP_ID_EXTRA, 0))
                .findFirst();
        episodeViewModel = new EpisodeViewModel(
                episode
        );

        binding.setVm(episodeViewModel);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(OnlinePageFragment.getFragment(episode.getId()), getString(R.string.online).toUpperCase());
        adapter.addFragment(TorrentPageFragment.getFragment(episode.getId()), getString(R.string.torrent).toUpperCase());
        adapter.addFragment(SubtitlesPageFragment.getFragment(episode.getId()), getString(R.string.subtitles).toUpperCase());
        binding.pager.setAdapter(adapter);
    }

//    protected class HeaderVH extends BindableViewHolder {
//
//        @BindView(R.id.recycler)
//        RecyclerView recyclerView;
//        @BindView(R.id.related)
//        TextView related;
//
//        @BindDimen(R.dimen.online_related_spacing)
//        int relatedSpacing;
//
//        BaseSearchListFragment.HeaderAdapter adapter;
//
//        public HeaderVH(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            adapter = new BaseSearchListFragment.HeaderAdapter(getRelatedItems());
//            recyclerView.addItemDecoration(new SpacesBetweenItemsDecoration(relatedSpacing));
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.HORIZONTAL, false));
//            recyclerView.setAdapter(adapter);
//        }
//
//        @Override
//        public void onBind(int position) {
//
//        }
//    }

//    protected class HeaderAdapter<T extends Tagged & HasUrl> extends RecyclerView.Adapter<HeaderAdapter.ItemVH> {
//
//        private List<T> tagged;
//
//        protected HeaderAdapter(List<T> tagged) {
//            this.tagged = tagged;
//        }
//
//        @Override
//        public HeaderAdapter.ItemVH onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ItemVH(LayoutInflater.from(getActivity()).inflate(R.layout.episode_page_online_item, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(HeaderAdapter.ItemVH holder, int position) {
//            Glide.with(getActivity())
//                    .load(DbUtil.posterUrl(episode, Decorator.W480))
//                    .asBitmap()
//                    .error(R.drawable.ic_movie_black_48dp)
//                    .centerCrop()
//                    .into(new BitmapImageViewTarget(holder.poster) {
//
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable =
//                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
//                            circularBitmapDrawable.setCornerRadius(cornerRadius);
//
//                            holder.poster.setImageDrawable(circularBitmapDrawable);
//                        }
//                    });
//
//            holder.quality.setText(getRelatedText(tagged.get(position)));
//        }
//
//        @Override
//        public int getItemCount() {
//            return tagged.size();
//        }
//
//        protected class ItemVH extends BindableViewHolder {
//
//            @BindView(R.id.poster)
//            protected ImageView poster;
//            @BindView(R.id.quality)
//            protected TextView quality;
//
//            public ItemVH(View itemView) {
//                super(itemView);
//                ButterKnife.bind(this, itemView);
//            }
//
//            @Override
//            public void onBind(int position) {
//
//            }
//        }
//    }
}
