package com.iam.oneom.pages.main.search.online.naya;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.iam.oneom.core.util.Intents;
import com.iam.oneom.view.listdialog.ClickableListItem;

import org.jsoup.nodes.Element;

/**
 * Created by iam on 29.05.17.
 */

public class NayaResolutionLink implements Parcelable, ClickableListItem {

    private String name;
    private String link;

    public NayaResolutionLink(Element element) {
        Log.i("", "NayaResolutionLink: ");
        name = element.attr("label");
        link = element.attr("src");
    }

    public String getLink() {
        return link;
    }

    protected NayaResolutionLink(Parcel in) {
        name = in.readString();
        link = in.readString();
    }

    public static final Creator<NayaResolutionLink> CREATOR = new Creator<NayaResolutionLink>() {
        @Override
        public NayaResolutionLink createFromParcel(Parcel in) {
            return new NayaResolutionLink(in);
        }

        @Override
        public NayaResolutionLink[] newArray(int size) {
            return new NayaResolutionLink[size];
        }
    };

    @Override
    public View.OnClickListener getOnClick() {
        return v -> {
            Intents.runOnline(v.getContext(), link);
        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(link);
    }
}
