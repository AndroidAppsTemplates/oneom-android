package com.iam.oneom.pages.main.search.online.vodlocker;

import android.databinding.ObservableField;
import android.widget.Toast;

import com.iam.oneom.R;
import com.iam.oneom.core.CustomRequest;
import com.iam.oneom.core.util.Intents;
import com.iam.oneom.pages.OneOm;
import com.iam.oneom.pages.main.search.online.OnlineSearchResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by iam on 13.05.17.
 */

public class VodlockerSearchResult extends OnlineSearchResult {

    private static final String TAG = VodlockerSearchResult.class.getSimpleName();

    @Override
    public void parse(String html) {
        Document document = Jsoup.parse(html);
        setName(document.getElementsByClass("link").text());
        setUrl(document.getElementsByClass("link").attr("href"));
        setPosterUrl(document.getElementsByTag("img").attr("src"));
    }

    @Override
    public void loadVideo(ObservableField<Boolean> loading) {
        if (loading != null) loading.set(true);
        CustomRequest.instance.request(getDownloadUrl() + "?video")
                .flatMap(s -> {

                    Document document = Jsoup.parse(s);
                    Elements elements = document.getElementsByTag("IFRAME");

                    if (elements.size() == 0) {
                        if (loading != null) loading.set(false);
                        return rx.Observable.error(new Exception("Error parsing \n\n" + s + "\n\n there are no IFRAME TAG"));
                    }

                    return CustomRequest.instance.request(elements.get(0).attr("src"));
                })
                .onErrorReturn(throwable -> "")
                .subscribe(s -> {
                    Document document = Jsoup.parse(s);

                    if (checkForDisappearance(document)) {
                        getToast(R.string.video_was_deleted);
                        loading.set(false);
                        return;
                    }

                    Elements src = document.getElementsByAttributeValueMatching("src", ".+\\.mp4");

                    if (src.size() == 0) {
                        getToast(R.string.parsing_error);
                        if (loading != null) loading.set(false);
                        return;
                    }


                    if (loading != null) loading.set(false);
                    Intents.runOnline(OneOm.getContext(), src.get(0).attr("src"));
                }, throwable -> getToast(R.string.parsing_error));
    }

    private boolean checkForDisappearance(Document document) {
        Elements elements = document.getElementsByTag("title");
        return elements.size() > 0 && elements.get(0).text().contains("deleted");
    }

    private void getToast(int text) {
        Toast.makeText(OneOm.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
