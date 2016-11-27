package com.iam.oneom.core.entities;

import android.os.Parcel;
import android.os.Parcelable;
import com.iam.oneom.core.util.Logger;
import com.iam.oneom.pages.OneOm;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Torrent extends Entity implements Parcelable {

    private Lang lang;
    private Quality quality;
    private Source source;
    private String url;
    private String title;
    private String vkPostId;
    private String value;

    public Torrent(JSONObject data){
        super(data);

        try {
            source = Source.getByID(data.getJSONObject("source").getString("id"));
            lang = Lang.getByID(data.getJSONObject("lang").getString("id"));
            quality = Quality.getByID(data.getJSONObject("quality").getString("id"));
            url = data.getString("url");
            title = data.getString("title");
            vkPostId = data.getString("vk_post_id");
            value = data.getString("value");
        } catch (JSONException e) {
//            OneOm.handleError(Thread.currentThread(), e, "Torrent main constructor");
            e.printStackTrace();
        }
    }

    public Torrent(Element html, Source.Origin origin) {
        super(html.toString());
        source = Source.getByOrigin(origin);
        lang = Lang.getByOrigin(origin);
        quality = Quality.getByHtml(html.toString());
        vkPostId = "0";


        switch (origin) {
            case piratebay:

                Elements pbCols = html.select("td");
                Element pbCol = pbCols.get(1);

                title = pbCol.select("a").get(0).html();
                value = pbCol.select("a").get(1).attr("href");
                url = "https://thepiratebay.cr" + pbCol.select("a").get(0).attr("href");

                break;
            case extratorrent:
                Elements etCols = html.select("td");
                value = "http://extratorrent.cc" + etCols.get(0).select("a").attr("href");
                url = "http://extratorrent.cc" + etCols.get(2).select("a").attr("href");
                title = etCols.get(2).select("a").attr("title").replace("view ", "").replace(" torrent", "");
                if (title.toLowerCase().equals("view comments")) {
                    title = etCols.get(2).select("a").get(1).attr("title").replace("view ", "").replace(" torrent", "");
                }
                break;
            case eztv:
                Elements eztvCols = html.select("td");
                url = eztvCols.get(0).select("a").get(1).attr("href");
                title = eztvCols.get(1).select("a[href]").html();
                value = eztvCols.get(2).select("a").attr("href");


                break;
            case kickasstorrents:

                Element katCol = html.select("td").get(0);
                url = "https://kat.cr" + katCol.select("a").get(1).attr("href");
                value = katCol.select("a").get(2).attr("href");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(katCol.select("div").get(1).attr("data-sc-params"));
                    title = URLDecoder.decode(jsonObject.getString("name"), "UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;

            case rarbg:


                Elements rarbgCol = html.select("td");

                title = rarbgCol.get(1).select("a").attr("title");
                url = "https://rarbg.to" + rarbgCol.get(1).select("a").attr("href");
                value = "https://rarbg.to" + rarbgCol.get(1).select("a").get(1).attr("href");

                break;
        }
    }

    protected Torrent(Parcel in) {
        super(in);
        lang = in.readParcelable(Lang.class.getClassLoader());
        quality = in.readParcelable(Quality.class.getClassLoader());
        source = in.readParcelable(Source.class.getClassLoader());
        url = in.readString();
        title = in.readString();
        vkPostId = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(lang, flags);
        dest.writeParcelable(quality, flags);
        dest.writeParcelable(source, flags);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(vkPostId);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Torrent> CREATOR = new Creator<Torrent>() {
        @Override
        public Torrent createFromParcel(Parcel in) {
            return new Torrent(in);
        }

        @Override
        public Torrent[] newArray(int size) {
            return new Torrent[size];
        }
    };

    public Lang lang() { return lang; }
    public Quality quality() { return quality; }
    public Source source() { return source; }
    public String url() { return url; }
    public String title() { return title; }
    public String vkPostId() { return vkPostId; }
    public String value() { return value; }

    public String tagInfo() {
        return quality.getName() + " " + lang.shortName();
    }
}
