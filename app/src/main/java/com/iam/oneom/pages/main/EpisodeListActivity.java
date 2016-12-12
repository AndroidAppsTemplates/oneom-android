package com.iam.oneom.pages.main;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.iam.oneom.R;
import com.iam.oneom.core.entities.Util;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.entities.model.Torrent;
import com.iam.oneom.core.network.request.SerialSearchResult;
import com.iam.oneom.core.network.request.SerialsSearchRequest;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Editor;
import com.iam.oneom.env.handling.recycler.BindableViewHolder;
import com.iam.oneom.env.handling.recycler.itemdecorations.EqualSpaceItemDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.GridLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.TagBar;
import com.iam.oneom.env.widget.svg;
import com.iam.oneom.env.widget.text.Text;
import com.iam.oneom.env.widget.text.font;
import com.iam.oneom.pages.main.EpisodePage.EpisodePageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeListActivity extends AppCompatActivity {

    private static final String TAG = EpisodeListActivity.class.getSimpleName();

    ListPopupWindow popupWindow;

    @BindView(R.id.progress)
    CircleProgressBar progressBar;
    @BindView(R.id.search_et)
    EditText searchET;
    @BindView(R.id.search_icon)
    ImageView searchIcon;
    @BindView(R.id.searchHeader)
    RelativeLayout searchView;
    @BindView(R.id.mainrecycler)
    RecyclerView episodesGrid;

    List<Episode> episodes = new ArrayList<>();

    GridLayoutManager recyclerLayoutManager;
    EpisodesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_list_activity);
        ButterKnife.bind(this);

        episodes = Realm.getDefaultInstance().where(Episode.class).findAll();

        searchIcon.setImageDrawable(svg.search.drawable());
        searchET.addTextChangedListener(new TextWatcher() {

            AsyncTask<String, Void, String> task;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (task != null) {
                    hideProgressBar();
                    task.cancel(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s != null && s.length() > 0) {
                    adapter.filterOnSearch(Editor.splitTOWords(s.toString()));
                    com.iam.oneom.core.network.Web.instance.searchSerials(s.toString()).enqueue(new Callback<SerialsSearchRequest>() {
                        @Override
                        public void onResponse(Call<SerialsSearchRequest> call, Response<SerialsSearchRequest> response) {
                            showPopup(response.body().getResults());
                        }

                        @Override
                        public void onFailure(Call<SerialsSearchRequest> call, Throwable t) {

                        }
                    });
                } else {
                    adapter.filterOnSearch(new ArrayList<>());
                }
            }
        });

        episodesGrid.addItemDecoration(new EqualSpaceItemDecoration((int) Decorator.dipToPixels(this, 5)));

        invalidateRecycler();
    }

    class EpisodesAdapter extends RecyclerView.Adapter<BindableViewHolder> {

        LayoutInflater inflater;
        List<Episode> episodes = new ArrayList<>();

        public EpisodesAdapter(List<Episode> episodes, AppCompatActivity context) {
            if (context != null) inflater = context.getLayoutInflater();
            this.episodes = episodes;
        }

        @Override
        public BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(BindableViewHolder holder, int position) {
            holder.onBind(position);
        }

        public void filterOnSearch(ArrayList<String> matchStrings) {

            HashMap<Episode, Integer> epsWithMatchCount = new HashMap<>();
            ArrayList<Episode> resultEpisodesList = new ArrayList<>();

            for (Episode e : EpisodeListActivity.this.episodes) {
                int containsCount = 0;
                for (String s : matchStrings) {
                    if (e.getSerial().getTitle().toLowerCase().contains(s.toLowerCase())) {
                        containsCount++;
                    }
                }

                epsWithMatchCount.put(e, containsCount);
            }

            for (int i = matchStrings.size(); i > 0; i--) {
                for (Map.Entry<Episode, Integer> entry : epsWithMatchCount.entrySet()) {
                    if (entry.getValue() == i) {
                        resultEpisodesList.add(entry.getKey());
                    }
                }
            }

            if (resultEpisodesList.size() == 0) {
                EpisodesAdapter.this.episodes = EpisodeListActivity.this.episodes;
            } else {
                EpisodesAdapter.this.episodes = resultEpisodesList;
            }

            notifyDataSetChanged();

            requestEditTextFocus();
        }

        @Override
        public int getItemCount() {
            return episodes.size();// + 1;
        }

        class EpisodeVH extends BindableViewHolder {

            View view;
            ImageView image;
            Text title;
            TagBar tagbar;
            RelativeLayout frame;
            FrameLayout frameLayout;

            public EpisodeVH(View itemView) {
                super(itemView);
                view = itemView;
                view.setBackgroundResource(R.drawable.table_item_border);
                setFrame(itemView);
                setImage(itemView);
                setTitle(itemView);
                setTagBar(itemView);
            }

            private void setFrame(View itemView) {
                frame = (RelativeLayout) itemView.findViewById(R.id.frame);
                int a = (int) Decorator.dipToPixels(EpisodeListActivity.this, 8);
                frame.setPadding(a, a, a, a);
            }

            private void setTagBar(View itemView) {
                tagbar = (TagBar) itemView.findViewById(R.id.tagBar);
            }

            private void setTitle(View itemView) {
                title = (Text) itemView.findViewById(R.id.title);
                title.setTypeface(font.font133sb.typeface(EpisodeListActivity.this));
                title.setTextColor(Decorator.TXTBLUE);
            }

            private void setImage(View itemView) {
                image = (ImageView) itemView.findViewById(R.id.image);
                frameLayout = (FrameLayout) itemView.findViewById(R.id.imageframe);
                Decorator.setSquareSize(frameLayout, Decorator.getSizeForTable(3) - (int) Decorator.dipToPixels(EpisodeListActivity.this, 8) * 2);
                image.setBackgroundResource(R.drawable.episode_item_image_cropper);
            }

            @Override
            public void onBind(final int position) {
                final Episode ep = episodes.get(position);
                String titleText = ep.getSerial().getTitle() + " " + Util.episodeInSeasonString(ep);
                title.setText(titleText);
                ArrayList<String> tags = new ArrayList<>();
                for (Torrent torrent : ep.getTorrent()) {
                    tags.add(Util.qualityTag(torrent));
                }
                tagbar.addTags(tags);
                Glide
                        .with(view.getContext())
                        .load(ep.getSerial() == null ? "null" : ep.getSerial().getPoster().getOriginal())
                        .into(image);
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EpisodePageActivity.class);
                    intent.putExtra(getString(R.string.media_page_episode_intent), episodes.get(position).getId());
                    startActivity(intent);
                });
            }
        }
    }

    class PopupAdapter extends ArrayAdapter<String> {

        List<String> names;
        List<Long> ids;
        LayoutInflater inflater;
        int resource;

        public PopupAdapter(Context context, int resource, List<String> objects, List<Long> ids) {
            super(context, resource, objects);
            inflater = ((AppCompatActivity) context).getLayoutInflater();
            this.ids = ids;
            this.names = objects;
            this.resource = resource;
        }


        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            SearchResultViewHolder holder;

            if (view == null) {
                view = inflater.inflate(resource, parent, false);
                holder = new SearchResultViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (SearchResultViewHolder) view.getTag();
            }

            holder.onBind(position);

            return view;
        }


        class SearchResultViewHolder {

            View view;
            Text text;

            SearchResultViewHolder(View view) {
                this.view = view;
                text = (Text) view.findViewById(R.id.serialname);
            }

            public void onBind(final int position) {
                text.setText(names.get(position));
                text.setOnClickListener(v -> {
                    Intent intent = new Intent(EpisodeListActivity.this, SerialPageActivity.class);
                    intent.putExtra(getString(R.string.media_page_serial_intent), ids.get(position));
                    startActivity(intent);
                });
            }

        }
    }

    public void showPopup(final List<SerialSearchResult> serials) {
        if (serials != null && serials.size() > 0) {

            popupWindow = new ListPopupWindow(EpisodeListActivity.this);

            final ArrayList<String> names = new ArrayList<>(serials.size());
            ArrayList<Long> ids = new ArrayList<>(serials.size());
            for (SerialSearchResult serial : serials) {
                names.add(serial.getTitle());
                ids.add(serial.getId());
            }

            PopupAdapter adapter = new PopupAdapter(EpisodeListActivity.this, R.layout.popup_item, names, ids);

            popupWindow.setAdapter(adapter);
            popupWindow.setModal(false);
            popupWindow.setWidth((int) (Decorator.getScreenWidth() * 0.9));
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnchorView(searchET);
            popupWindow.show();
        }

        requestEditTextFocus();
    }

    public void requestEditTextFocus() {
        searchET.post(new Runnable() {
            public void run() {
                searchET.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) EpisodeListActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(searchET, 0);
            }
        });
    }

    private void invalidateRecycler() {
        recyclerLayoutManager = new GridLayoutManager(this, 3);
        episodesGrid.setLayoutManager(recyclerLayoutManager);
        adapter = new EpisodesAdapter(episodes, this);
        episodesGrid.addOnScrollListener(episodesRecyclerViewOnScrollListener());
        episodesGrid.setAdapter(adapter);
    }

    private RecyclerView.OnScrollListener episodesRecyclerViewOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    searchView.animate()
                            .setDuration(500)
                            .setStartDelay(0)
                            .translationY(-searchView.getHeight())
                            .setListener(searchAnimatorListener(recyclerView));
                } else if (dy < 0) {
                    searchView.animate()
                            .setDuration(500)
                            .setStartDelay(0)
                            .translationY(0)
                            .setListener(searchAnimatorListener(recyclerView));
                }
            }
        };
    }

    private Animator.AnimatorListener searchAnimatorListener(final RecyclerView recyclerView) {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                recyclerView.clearOnScrollListeners();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.addOnScrollListener(episodesRecyclerViewOnScrollListener());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
