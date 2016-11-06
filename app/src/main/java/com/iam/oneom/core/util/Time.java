package com.iam.oneom.core.util;

import com.iam.oneom.pages.OneOm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public final class Time {

    public enum TimeFormat {
        IDN(new SimpleDateFormat("yyyy-MM-dd")),
        ITDN(new SimpleDateFormat("kk:mm:ss")),
        OutputDTwTZ(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")),
        OutputDT(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
        IDTwTZ(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));

        TimeFormat(SimpleDateFormat sdf) {
            this.sdf = sdf;
        }

        SimpleDateFormat dateFormat() {
            return sdf;
        }

        SimpleDateFormat sdf;
    }

    public static String format(String date, TimeFormat in, TimeFormat out) throws ParseException {
        Date d = in.dateFormat().parse(date);
        return out.dateFormat().format(d);
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

