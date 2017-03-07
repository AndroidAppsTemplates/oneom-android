package com.iam.oneom.pages.main;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iam.oneom.R;
import com.iam.oneom.core.DbHelper;
import com.iam.oneom.core.entities.model.Episode;
import com.iam.oneom.core.network.Web;
import com.iam.oneom.core.network.request.SerialSearchResult;
import com.iam.oneom.core.network.request.SerialsSearchRequest;
import com.iam.oneom.core.network.response.EpResponse;
import com.iam.oneom.core.util.Decorator;
import com.iam.oneom.core.util.Time;
import com.iam.oneom.env.handling.recycler.itemdecorations.EqualSpaceItemDecoration;
import com.iam.oneom.env.handling.recycler.layoutmanagers.LinearLayoutManager;
import com.iam.oneom.env.widget.CircleProgressBar;
import com.iam.oneom.env.widget.svg;
import com.iam.oneom.env.widget.text.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EpisodeListActivity extends AppCompatActivity implements Callback<SerialsSearchRequest>, TextWatcher {

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
    Map<Date, List<Episode>> groups = new LinkedHashMap<>();

    RecyclerView.LayoutManager recyclerLayoutManager;
    EpisodeGroupAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_list_activity);
        ButterKnife.bind(this);

        episodes = DbHelper.where(Episode.class).equalTo("isSheldule", true).findAllSorted("airdate", Sort.DESCENDING);

        for (Episode e : episodes) {
            if (groups.get(e.getAirdate()) == null) {
                groups.put(e.getAirdate(), new ArrayList<>());
                groups.get(e.getAirdate()).add(e);
            } else {
                groups.get(e.getAirdate()).add(e);
            }
        }

        searchIcon.setImageDrawable(svg.search.drawable());
        searchET.addTextChangedListener(this);

        episodesGrid.addItemDecoration(new EqualSpaceItemDecoration((int) Decorator.dipToPixels(this, 5)));

        invalidateRecycler();
    }

    class EpisodeGroupAdapter extends RecyclerView.Adapter<EpisodeGroupAdapter.GroupVH> {

        Map<Date, List<Episode>> groups;
        List<Date> keys;


        EpisodeGroupAdapter(Map<Date, List<Episode>> groups) {
            this.groups = groups;
            this.keys = new ArrayList(groups.keySet());
        }

        @Override
        public EpisodeGroupAdapter.GroupVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new GroupVH(
                    LayoutInflater.from(EpisodeListActivity.this).inflate(R.layout.episodes_group_item, parent, false));
        }

        @Override
        public void onBindViewHolder(EpisodeGroupAdapter.GroupVH holder, int position) {
            Date date = keys.get(position);
            holder.onBind(date, groups.get(date));
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        class GroupVH extends RecyclerView.ViewHolder {

            @BindView(R.id.recycler)
            RecyclerView recyclerView;
            @BindView(R.id.date)
            TextView date;

            public GroupVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            void onBind(Date date, List<Episode> episodes) {
                this.date.setText(Time.format(date, Time.TimeFormat.TEXT));
                recyclerView.setAdapter(new EpisodesAdapter(episodes, recyclerView.getContext()));
                recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 3));
            }
        }
    }

    class EpisodesAdapter extends RecyclerView.Adapter<EpisodeVH> {

        LayoutInflater inflater;
        List<Episode> episodes = new ArrayList<>();

        public EpisodesAdapter(List<Episode> episodes, Context context) {
            if (context != null) inflater = LayoutInflater.from(context);
            this.episodes = episodes;
        }

        @Override
        public EpisodeVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EpisodeVH(inflater.inflate(R.layout.episodes_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(EpisodeVH holder, int position) {
            holder.onBind(episodes.get(position));
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
                    SerialPageActivity.start(v.getContext(), ids.get(position));
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
        recyclerLayoutManager = new LinearLayoutManager(this);
        episodesGrid.setLayoutManager(recyclerLayoutManager);
        adapter = new EpisodeGroupAdapter(groups);
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

    @Override
    public void onResponse(Call<SerialsSearchRequest> call, Response<SerialsSearchRequest> response) {
        if (response.body() != null) {
            showPopup(response.body().getResults());
        }
    }

    @Override
    public void onFailure(Call<SerialsSearchRequest> call, Throwable t) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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
//            adapter.filterOnSearch(Editor.splitTOWords(s.toString()));
            com.iam.oneom.core.network.Web.instance.searchSerials(s.toString()).enqueue(this);
        } else {
//            adapter.filterOnSearch(new ArrayList<>());
        }
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

/*

03-05 17:25:48.279 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1466883 Santa Clarita Diet
03-05 17:25:51.729 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1466882 Santa Clarita Diet
03-05 17:29:48.699 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1466547 The Big Bang Theory
03-05 17:30:53.999 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1466461 Quantico
03-05 18:22:07.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462797 Hawaii Five-0
03-05 18:22:19.639 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462779 Scandal
03-05 18:22:46.289 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462742 Jane the Virgin
03-05 18:23:23.869 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462688 Ransom
03-05 18:23:45.549 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462656 Last Man Standing
03-05 18:25:15.799 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462525 Imposters
03-05 18:25:20.789 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462518 Blue Bloods
03-05 18:25:50.259 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462490 Supergirl
03-05 18:27:06.359 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462377 Modern Family
03-05 18:27:10.789 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462371 New Girl
03-05 18:28:20.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1462266 Lethal Weapon
03-05 18:31:58.639 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461990 2 Broke Girls
03-05 18:31:59.459 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461989 Kevin Can Wait
03-05 18:33:43.929 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461843 Chicago Med
03-05 18:34:00.649 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461820 How to Get Away with Murder
03-05 18:34:07.199 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461814 Grey's Anatomy
03-05 18:34:10.439 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461810 Suits
03-05 18:34:17.299 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461801 Bones
03-05 18:34:19.719 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461800 The Mick
03-05 18:34:22.929 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1461796 Quantico
03-05 18:53:25.879 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460237 Jane the Virgin
03-05 18:54:15.909 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460169 Blindspot
03-05 18:54:24.009 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460157 The Expanse
03-05 18:54:54.829 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460120 Colony
03-05 18:54:56.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460118 Colony
03-05 18:55:00.729 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460113 Cardinal
03-05 18:55:51.699 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460045 Kevin Can Wait
03-05 18:55:53.179 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460043 Supergirl
03-05 18:56:17.489 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1460008 Apple Tree Yard
03-05 18:58:01.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459852 Family Guy
03-05 18:58:03.899 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459849 Training Day
03-05 18:58:32.599 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459807 NCIS: New Orleans
03-05 18:58:37.279 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459802 Man with a Plan
03-05 18:59:01.159 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459767 Ransom
03-05 18:59:09.459 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459754 Blue Bloods
03-05 18:59:10.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459752 Hawaii Five-0
03-05 18:59:46.559 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459708 Mom
03-05 18:59:47.839 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459706 Mom
03-05 19:01:13.509 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459672 Chicago P.D.
03-05 19:01:18.179 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459669 Chicago Fire
03-05 19:07:13.319 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1459259 Criminal Minds
03-05 19:49:16.669 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1456385 Suits
03-05 19:51:24.309 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1456204 Voice
03-05 19:52:01.189 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1456148 The Flash
03-05 19:52:01.919 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1456147 Jane the Virgin
03-05 19:52:02.789 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1456146 Supergirl
03-05 19:52:53.989 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1456071 The Mick
03-05 19:54:18.559 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455949 NCIS: New Orleans
03-05 19:54:20.689 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455946 Quantico
03-05 19:54:23.659 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455942 Apple Tree Yard
03-05 19:55:10.489 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455876 Man with a Plan
03-05 19:55:25.079 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455855 Blue Bloods
03-05 19:55:26.079 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455854 Hawaii Five-0
03-05 19:56:32.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455758 Bull
03-05 19:56:44.969 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455738 Apple Tree Yard
03-05 19:56:50.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455730 The Big Bang Theory
03-05 20:07:48.509 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455258 X Company
03-05 20:08:10.749 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1455225 Pure
03-05 20:25:41.059 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1454058 Invisible
03-05 21:06:36.479 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450840 Star Wars Rebels
03-05 21:07:12.549 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450805 Twin Peaks
03-05 21:07:13.649 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450804 Twin Peaks
03-05 21:07:14.629 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450803 Twin Peaks
03-05 21:07:15.749 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450802 Twin Peaks
03-05 21:07:19.419 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450799 Twin Peaks
03-05 21:07:20.259 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450798 Twin Peaks
03-05 21:07:20.949 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450797 Twin Peaks
03-05 21:07:22.029 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450796 Twin Peaks
03-05 21:08:05.409 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450752 Kevin Can Wait
03-05 21:12:24.899 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450445 Teen Wolf
03-05 21:12:25.709 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450444 Teen Wolf
03-05 21:12:26.479 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450443 Teen Wolf
03-05 21:12:27.249 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450442 The Mick
03-05 21:13:35.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450339 NCIS: New Orleans
03-05 21:13:37.999 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450336 2 Broke Girls
03-05 21:13:38.759 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450335 Man with a Plan
03-05 21:13:49.009 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450319 NCIS: Los Angeles
03-05 21:14:21.789 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450271 Bones
03-05 21:14:30.379 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450259 Schitt's Creek
03-05 21:14:54.879 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450223 Blue Bloods
03-05 21:15:00.359 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450215 Hawaii Five-0
03-05 21:15:01.249 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450214 MacGyver
03-05 21:15:02.039 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450213 MacGyver
03-05 21:15:30.629 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450188 Mom
03-05 21:16:31.529 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450135 The Path
03-05 21:16:33.429 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450134 The Path
03-05 21:16:38.249 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450130 Outsiders
03-05 21:16:40.969 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450128 Outsiders
03-05 21:17:29.459 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1450090 The Halcyon
03-05 21:21:28.449 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449898 Lethal Weapon
03-05 21:21:59.049 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449850 Cardinal
03-05 21:21:59.739 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449849 Cardinal
03-05 21:22:01.059 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449847 2 Broke Girls
03-05 21:22:35.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449795 The Simpsons
03-05 21:22:36.709 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449794 The Simpsons
03-05 21:23:15.289 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449734 Reign
03-05 21:24:37.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1449610 NCIS
03-05 21:34:13.049 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1448766 Valkyrien
03-05 21:49:57.729 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1447376 The Grand Tour
03-05 21:52:16.689 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1447167 Riverdale
03-05 21:52:18.379 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1447165 Riverdale
03-05 21:54:39.499 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446958 American Housewife
03-05 21:55:10.879 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446910 Tina and Bobby
03-05 21:55:26.609 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446886 Revolting
03-05 21:57:14.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446723 Son of Zorn
03-05 21:57:29.079 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446701 Lethal Weapon
03-05 21:58:44.109 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446590 Blindspot
03-05 22:00:00.019 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446478 Bones
03-05 22:00:00.759 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1446477 New Girl
03-05 22:33:21.589 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443849 How to Get Away with Murder
03-05 22:33:28.409 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443839 Star Wars Rebels
03-05 22:33:38.239 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443824 Modern Family
03-05 22:33:44.189 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443816 Conviction
03-05 22:33:44.919 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443815 Conviction
03-05 22:35:18.809 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443671 Scandal
03-05 22:35:19.509 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443670 Grey's Anatomy
03-05 22:35:23.729 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443664 NCIS: New Orleans
03-05 22:35:25.689 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443661 NCIS
03-05 22:36:19.939 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443585 SIX
03-05 22:36:22.069 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443582 Colony
03-05 22:36:28.759 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443573 Hawaii Five-0
03-05 22:37:11.709 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443509 Trollhunters
03-05 22:37:12.369 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443508 Trollhunters
03-05 22:37:12.939 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443507 Trollhunters
03-05 22:37:13.579 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443506 Trollhunters
03-05 22:37:14.199 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443505 Trollhunters
03-05 22:37:14.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443504 Trollhunters
03-05 22:37:15.489 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443503 Trollhunters
03-05 22:38:20.039 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443405 Family Guy
03-05 22:38:45.659 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443368 Trollhunters
03-05 22:39:21.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443317 Mary Kills People
03-05 22:39:22.629 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443315 Mary Kills People
03-05 22:39:23.399 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443314 Mary Kills People
03-05 22:39:40.299 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443290 Son of Zorn
03-05 22:41:02.179 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443167 Fortitude
03-05 22:41:02.919 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443166 Fortitude
03-05 22:41:04.429 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443164 Blue Bloods
03-05 22:41:06.679 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443161 MacGyver
03-05 22:41:08.159 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1443159 Blindspot
03-05 22:54:19.429 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1441946 The Devil You Know
03-05 22:54:23.939 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1441939 The Devil You Know
03-05 23:11:11.889 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440895 Man Seeking Woman
03-05 23:11:26.329 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440875 The Royals
03-05 23:11:27.109 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440874 The Royals
03-05 23:11:27.809 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440873 The Royals
03-05 23:11:28.519 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440872 The Royals
03-05 23:11:29.249 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440871 The Royals
03-05 23:11:46.639 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440847 It's Always Sunny in Philadelphia
03-05 23:11:48.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440845 It's Always Sunny in Philadelphia
03-05 23:12:04.709 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440819 APB
03-05 23:12:57.669 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440739 The Royals
03-05 23:13:15.129 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440713 Tina and Bobby
03-05 23:13:19.729 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440707 Sneaky Pete
03-05 23:13:20.519 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440706 Sneaky Pete
03-05 23:13:21.219 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440705 Sneaky Pete
03-05 23:13:21.919 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440704 Sneaky Pete
03-05 23:14:49.889 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440574 MacGyver
03-05 23:15:32.749 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440530 Superior Donuts
03-05 23:15:41.549 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440522 Son of Zorn
03-05 23:17:22.769 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440440 Mom
03-05 23:19:39.059 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440323 Endeavour
03-05 23:22:20.749 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1440127 The Big Bang Theory
03-05 23:23:54.989 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439983 2 Broke Girls
03-05 23:23:55.719 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439982 The Halcyon
03-05 23:24:40.059 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439912 Salem
03-05 23:24:40.859 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439911 Salem
03-05 23:25:13.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439861 New Girl
03-05 23:25:14.639 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439860 New Girl
03-05 23:25:52.539 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439805 Father Brown
03-05 23:26:05.509 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439785 The OA
03-05 23:26:06.829 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439783 The OA
03-05 23:26:08.129 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439781 The OA
03-05 23:26:08.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1439780 The OA
03-06 00:02:13.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1436612 Celeste
03-06 00:19:38.969 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1435233 The Miracle
03-06 00:22:00.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1435053 The Walking Dead
03-06 00:22:17.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1435028 Bull
03-06 00:22:58.639 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434966 Star
03-06 00:23:32.549 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434915 Detroiters
03-06 00:23:54.389 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434886 This Is Us
03-06 00:23:55.849 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434884 Hawaii Five-0
03-06 00:24:03.689 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434873 NCIS: New Orleans
03-06 00:24:04.599 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434872 NCIS
03-06 00:24:10.219 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434863 Ransom
03-06 00:24:11.839 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434861 Ransom
03-06 00:24:13.369 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434859 The Mick
03-06 00:24:14.239 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434858 The Mick
03-06 00:24:15.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434857 The Mick
03-06 00:24:24.439 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434843 Pure Genius
03-06 00:24:29.049 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434836 Modern Family
03-06 00:24:43.439 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434818 Timeless
03-06 00:24:44.249 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434817 Timeless
03-06 00:24:45.159 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434816 Timeless
03-06 00:24:46.159 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434815 Timeless
03-06 00:24:46.989 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434814 2 Broke Girls
03-06 00:24:57.749 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434798 Madam Secretary
03-06 00:24:58.579 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434797 Madam Secretary
03-06 00:24:59.379 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434796 Madam Secretary
03-06 00:25:01.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434794 Elementary
03-06 00:25:02.149 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434793 Elementary
03-06 00:25:03.169 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434792 Elementary
03-06 00:25:07.589 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434787 Blue Bloods
03-06 00:25:44.479 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434739 Powerless
03-06 00:25:50.039 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434732 Colony
03-06 00:25:51.149 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434731 The Simpsons
03-06 00:26:00.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434719 The Blacklist
03-06 00:26:02.579 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434717 The Blacklist
03-06 00:26:03.479 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434716 The Blacklist
03-06 00:26:05.049 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434714 Death in Paradise
03-06 00:26:11.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434705 Bones
03-06 00:26:12.809 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434704 The Halcyon
03-06 00:26:22.569 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434691 Incorporated
03-06 00:26:28.309 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434683 Family Guy
03-06 00:27:31.379 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434595 Z: The Beginning of Everything
03-06 00:27:32.099 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434594 Z: The Beginning of Everything
03-06 00:27:33.439 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434592 Z: The Beginning of Everything
03-06 00:27:34.049 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434591 Z: The Beginning of Everything
03-06 00:27:34.739 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434590 Z: The Beginning of Everything
03-06 00:27:41.339 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434581 Workaholics
03-06 00:27:44.449 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434577 It's Always Sunny in Philadelphia
03-06 00:27:45.199 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434576 It's Always Sunny in Philadelphia
03-06 00:27:46.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434575 It's Always Sunny in Philadelphia
03-06 00:27:48.649 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434572 Bones
03-06 00:28:06.579 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434548 Scorpion
03-06 00:28:07.399 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1434547 Scorpion
03-06 00:39:01.639 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433592 A Series of Unfortunate Events
03-06 00:39:02.509 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433591 A Series of Unfortunate Events
03-06 00:39:03.169 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433590 A Series of Unfortunate Events
03-06 00:39:03.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433589 A Series of Unfortunate Events
03-06 00:39:04.799 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433588 A Series of Unfortunate Events
03-06 00:39:05.619 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433587 A Series of Unfortunate Events
03-06 00:39:06.439 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433586 A Series of Unfortunate Events
03-06 00:39:07.279 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433585 Chicago P.D.
03-06 00:39:08.079 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433584 Conviction
03-06 00:44:17.539 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433119 Madam Secretary
03-06 00:44:40.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433088 Shut Eye
03-06 00:44:41.479 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433086 Shut Eye
03-06 00:44:42.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433085 Shut Eye
03-06 00:44:42.919 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433084 Shut Eye
03-06 00:44:44.469 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1433082 Shut Eye
03-06 00:47:59.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432790 Pacific Heat
03-06 00:47:59.839 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432789 Pacific Heat
03-06 00:48:03.319 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432784 Pacific Heat
03-06 00:48:03.949 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432783 Pacific Heat
03-06 00:52:41.459 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432364 Rectify
03-06 00:53:50.929 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432263 Sherlock
03-06 00:53:51.709 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432262 Delicious
03-06 00:54:09.939 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432235 Pure Genius
03-06 00:54:10.659 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432234 Pure Genius
03-06 00:54:11.409 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432233 Pure Genius
03-06 00:54:12.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432232 Pure Genius
03-06 00:54:12.839 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432231 Pure Genius
03-06 00:54:16.329 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432226 Man with a Plan
03-06 00:54:23.719 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432214 This Is Us
03-06 00:54:24.499 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432213 This Is Us
03-06 00:54:25.219 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432212 This Is Us
03-06 00:56:11.379 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432052 Criminal Minds
03-06 00:56:12.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432051 Criminal Minds
03-06 00:56:15.019 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432047 Lethal Weapon
03-06 00:56:15.769 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1432046 Lethal Weapon
03-06 00:56:54.769 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431988 Mom
03-06 00:56:56.879 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431985 The Big Bang Theory
03-06 00:57:59.519 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431896 Doubt
03-06 00:58:01.229 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431894 Training Day
03-06 00:58:02.799 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431892 Ransom
03-06 00:58:04.109 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431890 No Tomorrow
03-06 00:58:04.829 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431889 No Tomorrow
03-06 00:58:05.519 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431888 No Tomorrow
03-06 00:58:37.039 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431840 Crazy Ex-Girlfriend
03-06 00:58:39.969 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431836 Pacific Heat
03-06 01:00:02.279 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431715 Conviction
03-06 01:08:29.089 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431394 The Vampire Diaries
03-06 01:08:29.799 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431393 The Vampire Diaries
03-06 01:08:30.519 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431392 The Vampire Diaries
03-06 01:08:31.279 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431391 The Vampire Diaries
03-06 01:08:32.139 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431390 The Vampire Diaries
03-06 01:08:39.979 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431378 Arrow
03-06 01:11:00.609 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1431204 Santa Clarita Diet
03-06 01:21:48.229 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1430488 Quantico
03-06 01:22:10.559 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1430455 MacGyver
03-06 01:23:30.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1430336 Rectify
03-06 01:27:09.819 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1430011 Scorpion
03-06 01:27:14.769 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1430004 Hawaii Five-0
03-06 01:27:15.689 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1430003 MacGyver
03-06 01:27:20.379 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429996 2 Broke Girls
03-06 01:27:22.429 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429993 Man with a Plan
03-06 01:27:44.249 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429960 The Big Bang Theory
03-06 01:28:27.459 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429895 Elementary
03-06 01:30:15.599 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429738 Designated Survivor
03-06 01:30:17.739 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429735 Modern Family
03-06 01:30:20.509 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429731 Conviction
03-06 01:31:12.119 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429656 The Middle
03-06 01:32:35.579 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429530 Frequency
03-06 01:32:36.389 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429529 Frequency
03-06 01:32:37.149 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429528 Frequency
03-06 01:32:41.799 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429521 Timeless
03-06 01:33:58.009 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429404 Bull
03-06 01:33:58.719 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429403 Bull
03-06 01:34:03.769 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429395 2 Broke Girls
03-06 01:34:04.569 7570-7570/com.iam.oneohm D/EpisodeListActivity: onResponse: 1429394 Gotham
 */