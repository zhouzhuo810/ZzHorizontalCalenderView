package me.zhouzhuo.zzhorizontalcalenderview.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zz on 2016/1/13.
 */
public class DateUtil {

    public static String getYearMonthDay() {
        return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    }

    public static String getYearMonthDayHourMinuteSecond() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static String getHourMinuteSecond() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH)+1;
    }

    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentWeek() {
        return calWeek(getCurrentYear(), getCurrentMonth(), getCurrentDay());
    }

    public static int calWeek(int y, int m, int d) {
        int day = calPassDay(y, m, d);
        Log.i("TAG", y+"-"+m+"-"+d+",passDay="+day);
        int s = (y-1)+(y-1)/4-(y-1)/100+(y-1)/400+day;
        return s%7;
    }

    public static int calPassDay(int y, int m, int d) {
        int passDay = 0;
        for (int i=0; i<=m-1; i++) {
            passDay += calDayOfMonth(y, i);
        }
        return passDay+d;
    }

    public static int calDayOfMonth(int y, int m) {
        int day = 0;
        switch (m) {
            case 12:
                day = 31;
                break;
            case 11:
                day = 30;
                break;
            case 10:
                day = 31;
                break;
            case 9:
                day = 30;
                break;
            case 8:
                day = 31;
                break;
            case 7:
                day = 31;
                break;
            case 6:
                day = 30;
                break;
            case 5:
                day = 31;
                break;
            case 4:
                day = 30;
                break;
            case 3:
                day = 31;
                break;
            case 2:
                day = isLeapYear(y)?29:28;
                break;
            case 1:
                day = 31;
                break;
        }
        return day;
    }

    public static boolean isLeapYear(int y) {
        return y % 4 == 0 && y % 100 != 0 || y % 400 == 0;
    }


}
