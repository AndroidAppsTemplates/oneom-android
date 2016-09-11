package com.iam.oneom.core.util;

import com.iam.oneom.pages.OneOm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public final class Time {

    public final static int HOUR = 1;
    public final static int DAY = 2;

//    public static SimpleDateFormat[] dateFormat = new SimpleDateFormat[] {
//            new SimpleDateFormat("yyyy-MM-dd"),
//            new SimpleDateFormat("kk:mm:ss"),
//            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
//    };

//    public static s

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

    public static Date today() {
        Date date = new Date(System.currentTimeMillis());
        TimeFormat tf = TimeFormat.IDN;
        try {
            date = tf.dateFormat().parse(tf.dateFormat().format(date));
        } catch (ParseException e) {
            OneOm.handleError(Thread.currentThread(), e, "Time.today() ParseException");
            e.printStackTrace();
        }
        return date;
    }

    public static int weekday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        return weekday - 1;
    }

    public static int weekday(String date_in_string, TimeFormat format) {
        Date date = new Date();
        try {
            date = format.dateFormat().parse(date_in_string);
        } catch (ParseException e) {
            OneOm.handleError(Thread.currentThread(), e, "Time.weekday(String,TimeFormat) ParseException");
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        return weekday - 1;
    }

    public static int compareStringDates(String date, String earliestDate, DateFormat dateFormat) {
        Date dDate = null;
        Date dEarliestDate = null;
        try {
            dDate = dateFormat.parse(date);
            dEarliestDate = dateFormat.parse(earliestDate);
        } catch (ParseException e) {
            OneOm.handleError(Thread.currentThread(), e, "Time.compareStringDates(String,String,DateFormat) ParseException");
            e.printStackTrace();
        }
        return dDate.compareTo(dEarliestDate);
    }

    public static String getDayOfWeek (int num) {
        //num++;
        switch (num) {
            case 1: return "Понедельник";
            case 2: return "Вторник";
            case 3: return "Среда";
            case 4: return "Четверг";
            case 5: return "Пятница";
            case 6: return "Суббота";
            case 7: return "Воскресенье";
            default: return "undefined";
        }
    }

    public static String getDayOfEnglishWeek (int num) {
        //num++;
        switch (num) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";
            case 7: return "Sunday";
            default: return "undefined";
        }
    }

    public static String getMonthName (int num) {
        num++;
        switch (num) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "undefined";
        }
    }

    public static String newsDate(Date date) {
        StringBuilder sb = new StringBuilder();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        sb.append(c.get(Calendar.DAY_OF_MONTH))
                .append(" ")
                .append(getMonthName(c.get(Calendar.MONTH)))
                .append(" ")
                .append(c.get(Calendar.YEAR))
                .append(", ")
                .append(c.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + c.get(Calendar.HOUR_OF_DAY) : c.get(Calendar.HOUR_OF_DAY))
                .append(":")
                .append(c.get(Calendar.MINUTE) < 10 ? "0" + c.get(Calendar.MINUTE) : c.get(Calendar.MINUTE));
        return sb.toString();
    }

//    public static String goodShortDate(String date) {
//        StringBuilder res = new StringBuilder();
//
//        Date today = null;
//        try {
//            today = dateFormat[0].parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//
//        res.append(calendar.get(Calendar.DAY_OF_MONTH));
//        res.append(" ");
//        int num = calendar.get(Calendar.MONTH);
//
//        res.append(getMonthName(num));
//
//        return res.toString().toLowerCase();
//    }

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

//    public static String goodLookingDate(String date) {
//        StringBuilder res = new StringBuilder();
//
//        Date today = null;
//        try {
//            today = dateFormat[0].parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//
//        res.append(calendar.get(Calendar.DAY_OF_MONTH));
//        res.append(" ");
//        res.append(getMonthName(calendar.get(Calendar.MONTH)));
//        res.append(", ");
//        res.append(getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK) - 1));
//
//        return res.toString();
//    }
//
    public static Date substractSomeTime(Date date, int quantity, int what) {
        Calendar shiftDay = Calendar.getInstance();
        shiftDay.setTime(date);
        switch (what) {
            case HOUR:
                shiftDay.add(Calendar.HOUR, -quantity);
                break;
            case DAY:
                shiftDay.add(Calendar.DAY_OF_MONTH, -quantity);
                break;
        }
        return shiftDay.getTime();
    }

    public static Date addSomeTime(Date date, int quan, int what) {
        Calendar shiftDay = Calendar.getInstance();
        shiftDay.setTime(date);
        switch (what) {
            case HOUR:
                shiftDay.add(Calendar.HOUR, quan);
                break;
            case DAY:
                shiftDay.add(Calendar.DAY_OF_MONTH, quan);
                break;
        }
        return shiftDay.getTime();
    }

    public static String formatDate(String stringDate, DateFormat inputDateFormat, DateFormat outputDateFormat) {
        Date date = null;
        try {
            date = inputDateFormat.parse(stringDate);
        } catch (ParseException e) {
            OneOm.handleError(Thread.currentThread(), e, "Time.formatDate(String, DateFormat in, DateFormat out) ParseException");
            e.printStackTrace();
        }
        return outputDateFormat.format(date);
    }

    public static Date closestOrderDateAfterPdate(Date pdate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pdate);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.TUESDAY:
            case Calendar.THURSDAY:
                return addSomeTime(pdate, 1, DAY);
            case Calendar.SATURDAY:
                return addSomeTime(pdate, 2, DAY);
            default:
                return pdate;
        }
    }

    public enum compare {
        BEFORE(-1),
        EQUALS(0),
        AFTER(1);

        public int value() {
            return relative;
        }

        compare(int relative) {
            this.relative = relative;
        }

        int relative;
    }
}
