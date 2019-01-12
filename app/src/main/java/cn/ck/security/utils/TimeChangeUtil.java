package cn.ck.security.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeChangeUtil {
    /**
     * 将日期和时间合成一个字符串
     * */
    public static String joint(String date,String time){
        return date+" "+time;
    }

    /**
     * 将时间yyyy.MM.dd HH:mm:ss转换为时间戳
     * @param s
     * @return
     */
    public static String dateToStamp(String s){
        String res;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        ts /= 1000;
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转化时间 yyyy.MM.dd HH:mm:ss
     * @param s
     * @return
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        long lt = new Long(s);
        lt *= 1000;
        Date date = new Date(lt);
        res = sdf.format(date);
        return res;
    }

    /**
     * 将日期和时间分离，存放在数组中
     * @param s
     * @return
     */
    public static String[] split(String s){
        String[] res = s.split(" ");
        return res;
    }

    /**
     * 将时间段转化为 时：分：秒
     * @param s
     * @return
     */
    public static String caculatespaceTime(String s){
        String resHour = "",resMinute = "",resSecond = "";
        int hour = 0,minute = 0,second = 0;
        int beg = Integer.parseInt(s);
        if(beg < 60) {
            second = beg;
        }else {
            second = beg%60;
            int tempminute = (beg - second)/60;
            if(tempminute < 60) {
                minute = tempminute;
            }else {
                minute = tempminute%60;
                hour = (tempminute - minute)/60;
            }
        }
        if(hour == 0 ) resHour = "00";
        else if(hour < 10) resHour = "0" + hour;
        else resHour = "" + hour;

        if(minute == 0 ) resMinute = "00";
        else if(minute < 10) resMinute = "0" + minute;
        else resMinute = "" + minute;

        if(second == 0 ) resSecond = "00";
        else if(second < 10) resSecond = "0" + second;
        else resSecond = "" + second;

        return resHour + ":" + resMinute +  ":" + resSecond;
    }
}
