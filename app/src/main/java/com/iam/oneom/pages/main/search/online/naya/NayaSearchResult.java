package com.iam.oneom.pages.main.search.online.naya;

import android.databinding.ObservableField;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.CustomRequest;
import com.iam.oneom.core.util.Intents;
import com.iam.oneom.core.util.RxUtils;
import com.iam.oneom.pages.OneOm;
import com.iam.oneom.pages.main.search.online.OnlineSearchResult;
import com.iam.oneom.view.listdialog.ListDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by iam on 13.05.17.
 */

public class NayaSearchResult extends OnlineSearchResult {

    private static final String TAG = NayaSearchResult.class.getSimpleName();
    private List<NayaResolutionLink> sources;
    private Subscription subscription;

    @Override
    public void parse(String html) {
        Document document = Jsoup.parse(html);
        setName(document.select("a.video-link").attr("title"));
        setUrl(document.select("a.video-link").attr("href"));
        setPosterUrl(document.getElementsByTag("img").attr("src"));
    }

    @Override
    public void loadVideo(ObservableField<Boolean> loading) {
        RxUtils.unsubscribe(subscription);
        subscription = CustomRequest.instance.request(getDownloadUrl())
                .subscribe(
                        html -> {
                            parseLinks(Jsoup.parse(html));

                            if (sources.size() == 0) {
                                Toast.makeText(OneOm.getContext(), R.string.no_links, Toast.LENGTH_SHORT).show();
                            }else if (sources.size() == 1) {
                                Intents.runOnline(OneOm.getContext(), sources.get(0).getLink());
                            } else {
                                ListDialog.show(OneOm.getContext().getCurrentActivity().getFragmentManager(), sources);
                            }
                        }
                );
    }

    private void parseLinks(Document document) {
        sources = new ArrayList<>();
        for (Element element : document.select("source")) {
            sources.add(new NayaResolutionLink(element));
        }
    }
}
