package com.iam.oneom.util;

import android.content.Context;

import com.iam.oneom.R;
import com.iam.oneom.core.SecureStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class Time {

    public static final long SECOND = 1000L;
    public static final long MINUTE = SECOND * 60L;
    public static final long HOUR = MINUTE * 60L;
    public static final long DAY = HOUR * 24L;
    public static final long MONTH = DAY * 30L;
    public static final long YEAR = MONTH * 12L;

    public static Date parse(String date, TimeFormat timeFormat) {
        if (date == null) {
            return new Date(0);
        }

        try {
            return timeFormat.dateFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return parse(null, timeFormat);
        }
    }

    public static String episodesLastUpdated(Context context) {
        return SecureStore.getEpisodesLastUpdated() == 0 ?
                context.getString(R.string.never) : Time.format(new Date(SecureStore.getEpisodesLastUpdated()), "HH:mm, dd MMM yyyy");
    }

    public static Date getYear(Date date) throws ParseException {
        return TimeFormat.YEAR.dateFormat().parse(TimeFormat.YEAR.dateFormat().format(date));
    }

    public enum TimeFormat {
        IDN(new SimpleDateFormat("yyyy-MM-dd")),
        TEXT(new SimpleDateFormat("dd MMMM, yyyy")),
        YEAR(new SimpleDateFormat("yyyy")),
        ITDN(new SimpleDateFormat("kk:mm:ss")),
        OutputDTwTZ(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")),
        OutputDT(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
        IDTwTZ(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        TimeFormat(SimpleDateFormat sdf) {
            this.sdf = sdf;
        }

        public SimpleDateFormat dateFormat() {
            return sdf;
        }

        SimpleDateFormat sdf;
    }

    public static String format(Date date, TimeFormat out) {

        if (date == null) {
            return "";
        }

        return out.dateFormat().format(date);
    }

    public static String format(Date date, String out) {

        if (date == null) {
            return "";
        }

        return new SimpleDateFormat(out).format(date);
    }


    public static String format(long date, String out) {
        return new SimpleDateFormat(out).format(new Date(date));
    }

    public static String format(long date, TimeFormat out) {
        return out.dateFormat().format(new Date(date));
    }

    public static String format(Long date, String out) {
        return format(date == null ? 0 : date, out);
    }

    public static String format(Long date, TimeFormat out) {
        return format(date == null ? 0 : date, out);
    }

    public static String format(String date, TimeFormat in, TimeFormat out) throws ParseException {
        Date d = in.dateFormat().parse(date);
        return out.dateFormat().format(d);
    }

    public static String toString(long millis) {
        return format(new Date(millis), TimeFormat.IDN);
    }

    public static String getMonthName(int num) {
        num++;
        switch (num) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "undefined";
        }
    }

    public static String goodLookingEnglishDate(String date, TimeFormat tf) {
        StringBuilder res = new StringBuilder();

        Date today = null;
        try {
            today = tf.dateFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        res.append(calendar.get(Calendar.DAY_OF_MONTH));
        res.append(" ");
        res.append(getMonthName(calendar.get(Calendar.MONTH)));
        res.append(" ");
        res.append(calendar.get(Calendar.YEAR) % 100);

        return res.toString();
    }

}

