package com.iam.oneom.core.util;

import android.util.Log;

import com.iam.oneom.pages.OneOm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

public class Logger {

    public static void logHTMLElements(Elements elements) {
        Iterator<Element> iterator = elements.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            System.out.println("--------------------   " + i++ + "   ------------------");
            System.out.println(iterator.next());
        }
    }

    public static void logJSONArray(String tag, JSONArray array) {
        if (array != null) {
            if (array.length() > 0) {
                int l = array.length();
                for (int i = 0; i < l; i++) {
                    try {
                        Log.i(tag, array.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.i(tag, "array equals []");
            }
        } else {
            Log.i(tag, "array equals null");
        }
    }

    public static void logJSONObject(String tag, JSONObject object) {
        if (object != null) {
            if (object.length() > 0) {
                Iterator<String> iterator = object.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    try {
                        Log.i(tag, key + " " + object.getString(key));
                    } catch (JSONException e) {
//                        OneOm.handleError(Thread.currentThread(), e, "LogJSONObject");
                        e.printStackTrace();
                    }
                }
            } else {
                Log.i(tag, "object equals {}");
            }
        } else {
            Log.i(tag, "object equals null");
        }
    }

}
