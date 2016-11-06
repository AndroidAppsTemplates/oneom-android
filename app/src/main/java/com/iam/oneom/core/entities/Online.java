package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.oneom.core.util.Editor;
import com.iam.oneom.pages.OneOm;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Online extends Entity implements Parcelable {

    private Source source;
    private Lang lang;
    private Quality quality;
    private String title;
    private String embedCode;
    private String url;
    private String videoUrl;
    private String posterUrl;

    public Online(Element html, Source.Origin origin) {
        super(html.toString());
        source = Source.getByOrigin(origin);
        lang = Lang.getByOrigin(origin);
        quality = Quality.getByHtml(html.toString());
        switch (origin) {

            case vodlocker:

                Elements cols = html.select("td");
                Element[] columns = new Element[2];
                for (int j = 0; j < cols.size(); j++) {
                    columns[j] = cols.get(j);
                }

                String post = columns[0].select("a[href]").attr("style");
                this.posterUrl = Editor.posterLink(post);
                this.url = columns[1].select("div.link").select("a[href]").attr("href");
                this.title = columns[1].select("div.link").select("a[href]").text();

                break;
            default:
                break;
        }
    }

    public Online (JSONObject json, String posterUrl) {
        super(json);
        this.posterUrl = posterUrl;
        try {
            source = Source.getByID(json.getJSONObject("source").getString("id"));
            lang = Lang.getByID(json.getJSONObject("lang").getString("id"));
            quality = Quality.getByID(json.getJSONObject("quality").getString("id"));
            title = json.getString("title");
            embedCode = json.getString("embed_code");
            url = json.getString("url");
            videoUrl = json.getString("video_url");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Online main constructor");
            e.printStackTrace();
        }
    }

    protected Online(Parcel in) {
        super(in);
        source = in.readParcelable(Source.class.getClassLoader());
        lang = in.readParcelable(Lang.class.getClassLoader());
        quality = in.readParcelable(Quality.class.getClassLoader());
        title = in.readString();
        embedCode = in.readString();
        url = in.readString();
        videoUrl = in.readString();
        posterUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(source, flags);
        dest.writeParcelable(lang, flags);
        dest.writeParcelable(quality, flags);
        dest.writeString(title);
        dest.writeString(embedCode);
        dest.writeString(url);
        dest.writeString(videoUrl);
        dest.writeString(posterUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Online> CREATOR = new Creator<Online>() {
        @Override
        public Online createFromParcel(Parcel in) {
            return new Online(in);
        }

        @Override
        public Online[] newArray(int size) {
            return new Online[size];
        }
    };

    public Source source() { return source; }

    public Lang lang() { return lang; }

    public String title() { return title; }

    public String posterURL() { return this.posterUrl; }

    public static String extractVideoLink(String html, Source.Origin origin) {
        switch (origin) {
            case vodlocker:
                Document doc = Jsoup.parse(html);
                Elements link = doc.select("div.mobilink").select("a[href]");
                return link.attr("href");
            default:
                return "";
        }
    }

    public String url() {
        return url;
    }
    public String videoUrl() {
        return videoUrl;
    }
    public Quality quality() { return quality; }


}
