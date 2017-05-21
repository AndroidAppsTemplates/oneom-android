package com.iam.oneom.core.util;

import com.iam.oneom.core.entities.interfaces.Named;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {


    static class pattern {
        public static String notALetter = "\\W+";
        public static String squareBraces = "[\\[|\\]]";
        public static String year = "(19|20)\\d{2}";
        public static String imageLink = "[http].+[jpg|png]";
        public static String seasonEpisode = "[S| ]([0-9]{2}|[0-9])[ |.|x]?|([E| |]?([0-9]{3}|[0-9]{2}|[0-9])|) ?|(-([0-9]{2}|[0-9])|)";
        public static String vodlockerTotalSearchResults = "\\d+";
    }

    public static String encodeToUTF8(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String arrayWithoutBraces(Object[] objects) {
        return Arrays.toString(objects).replaceAll(pattern.squareBraces, "");
    }

    public static String filenameFromURL(String fileURL) {
        String[] parts = fileURL.split("/");
        return parts[parts.length - 1];
    }

    public static String addPairs(String root, String key, String value) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        result.append(root);

        if (!root.equals("")) result.append("&");


        result.append(URLEncoder.encode(key, "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(value, "UTF-8"));

        return result.toString();
    }

    public static ArrayList<String> splitTOWords(String s) {
        ArrayList<String> result = new ArrayList<>();
        String[] words = s.split(pattern.notALetter);
        switch (words.length) {
            case 0:
                return result;
            case 1:
                result.add(words[0]);
                return result;
            default:
                for (String word : words) {
                    if (word.length() >= 3) {
                        result.add(word);
                    }
                }
                return result;
        }
    }

    public static String size(long size) {

//        size = size / 8;

        int ending = 0;
        int mains = 0;
        int level = 0;

        while (size / 1024 > 0) {
            mains = (int) (size / 1024);
            ending = (int) (size % 1024);
            size /= 1024;
            level++;
        }

        if (mains == 0) {
            return String.format("%d%s", size, getBytesMultiplier(1));
        }

        return String.format(String.valueOf(ending).length() <= 1 ?
                "%d.%s%s" : "%d.%2.2s%s", mains, String.valueOf(ending), getBytesMultiplier(level));
    }

    private static String getBytesMultiplier(int level) {
        switch (level) {
            case 0:
                return "B";
            case 1:
                return "KB";
            case 2:
                return "MB";
            case 3:
                return "GB";
            case 4:
                return "TB";
            case 5:
                return "PB";
            case 6:
                return "EB";
            case 7:
                return "ZB";
            case 8:
                return "YB";
        }

        return "TO MUCH";
    }

    public static String posterLink(String html) {
        Pattern p = Pattern.compile(pattern.imageLink);
        Matcher m = p.matcher(html);
        if (m.find()) {
            return m.group();
        } else {
            return  "null";
        }
    }

    public static int vodlockerSearchResultsCount(String s) {
        Pattern p = Pattern.compile(pattern.vodlockerTotalSearchResults);
        Matcher m = p.matcher(s);

        if (m.find()) {
            return Integer.parseInt(m.group());
        } else {
            return 0;
        }
    }

    public static int seasonIndex(String where) {
        System.out.println(where);
        where = where.replaceAll(pattern.year, "");
        System.out.println(where);


        Pattern p = Pattern.compile(pattern.seasonEpisode, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(where);
        ArrayList<String> groups = new ArrayList<>();
        while (m.find()) {
            groups.add(m.group());
        }

        removeElement(groups, "", " ");
        System.out.print("{");
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).replaceAll(" ", "");
            if (i == 0) {
                System.out.print(groups.get(i) + ",");
            }

            if (i == 1) {
                System.out.print(groups.get(i));
            }
        }
        System.out.print("}");
        return 0;//Integer.parseInt(groups.get(17));
    }

    public static int episodeIndex(String where) {
        where = where.replaceAll(pattern.year, "");
        Pattern p = Pattern.compile(pattern.seasonEpisode, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(where);
        ArrayList<String> groups = new ArrayList<>();
        while (m.find()) {
            groups.add(m.group());
        }

        removeElement(groups, "", " ", "  ");

        for (String s : groups) {
            System.out.print("{" + groups.indexOf(s) + " \"" + s + "\"}, ");
        }
        return 0;//Integer.parseInt(groups.get(19));
    }

    public static void removeElement(ArrayList<String> al, String... string) {
        for (String s : string) {
            while (al.contains(s)) al.remove(s);
        }
    }

    public static String namesByComma(List<? extends Named> namedEntities) {
        if (namedEntities == null) {
            return "";
        }

        String[] names = new String[namedEntities.size()];
        for (int i = 0, l = names.length; i < l; i++) {
            names[i] = namedEntities.get(i).getName();
        }

        return names.length == 0 ? "" : Editor.arrayWithoutBraces(names);
    }

    public static String httpDataPairsToString(Map<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
//                OneOm.handleError(Thread.currentThread(), e, "Creating pairs array from Map for POST Editor.httpDataPairsToString(Map<String, String> params)");
                e.printStackTrace();
            }
        }

        return result.toString();
    }

    public static String sexyDouble(double d) {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else {
            if ((int)(d * 100 % 10) == 0)
                return String.format("%.1f", d);
            else
                return String.format("%.2f", d);
        }
    }
}
