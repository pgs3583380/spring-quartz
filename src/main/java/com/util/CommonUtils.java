package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author genshan pan
 * @version 1.0
 * @Date Jul 3, 2017
 * @Time 10:48:39 AM
 * @since JDK 1.7
 */
public class CommonUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }
        return null == obj;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    /**
     * 判断是否为指定长度
     */
    public static boolean checkAppointLenth(String str, int len) {
        return !isEmpty(str) && str.length() == len;
    }

    /**
     * 获取obj的值
     */
    @Deprecated
    public static String getObj(Object[] obj, int i) {
        return obj[i] == null ? "" : obj[i].toString();
    }

    public static String subStr(String str, int len) {
        if (isEmpty(str)) {
            return null;
        }
        if (str.length() >= len) {
            return str.substring(0, len);
        }
        return str;
    }

    /**
     * 更新版获取参数
     */
    public static String[] getObjs(Object[] obj) {
        int len = obj.length;
        String[] str = new String[len];
        for (int i = 0; i < len; i++) {
            str[i] = (obj[i] == null ? "" : obj[i].toString());
        }
        return str;
    }

    /**
     * 分钟加减
     *
     * @param oldTime 原始时间
     * @param amount  分钟
     */
    public static String modifyMinute(String oldTime, int amount, String... format) {
        return modifyTime(oldTime, amount, "min", format);
    }

    /**
     * @param oldTime 原始时间
     * @param amount  天数
     */
    public static String modifyDay(String oldTime, int amount, String... format) {
        return modifyTime(oldTime, amount, "day", format);
    }

    private static String modifyTime(String oldTime, int amount, String status, String[] format) {
        String ft = "";
        if ("day".equals(status)) {
            ft = "yyyy-MM-dd";
        }
        if ("min".equals(status)) {
            ft = "yyyy-MM-dd HH:mm";
        }
        if (format != null && format.length > 0) {
            ft = format[0];
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ft);
        Date newDate = null;
        try {
            Date date = sdf.parse(oldTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if ("day".equals(status)) {
                cal.add(Calendar.DATE, amount);
            }
            if ("min".equals(status)) {
                cal.add(Calendar.MINUTE, amount);
            }
            newDate = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(newDate);
    }

    /**
     * 时间格式化
     *
     * @param date   时间
     * @param format 格式
     */
    public static String timeFormat(Date date, String format) {
        if (isNotEmpty(format) && isNotEmpty(date)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        }
        return null;
    }

    /**
     * string 转化为date类型
     */
    public static Date StringToTime(String str, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (isNotEmpty(str)) {
            try {
                return simpleDateFormat.parse(str);
            } catch (ParseException ignored) {
            }
        }
        return null;
    }

    /**
     * 获取今天的日期     年-月-日
     */
    public static String getToday() {
        return new SimpleDateFormat(Constants.dateFormatPattern).format(new Date());
    }

    /**
     * 获取现在的时间  年-月-日  时分秒
     */
    public static String getNowTime() {
        return new SimpleDateFormat(Constants.timestampPattern).format(new Date());
    }

    /**
     * 获取现在的时间 精确到毫秒，一般用来对文件重命名
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 补0
     *
     * @param num    原始数字
     * @param numLen 补0后数字长度
     */
    public static String supplementZero(int num, int numLen) {
        String str = String.valueOf(num);
        int strLen = str.length();
        StringBuilder newNum = new StringBuilder();
        if (numLen <= strLen) {
            return str;
        }
        for (int j = 0, len = numLen - strLen; j < len; j++) {
            newNum.append("0");
        }
        return newNum.toString() + str;
    }

    /**
     * 修改时间格式  2017-06-07  2017年06月07日
     *
     * @return
     */
    public static String convertTime(String time, String... formats) {
        String format1 = "yyyy-MM-dd";
        String format2 = "yyyy年MM月dd日";
        if (formats != null) {
            int len = formats.length;
            for (int i = 0; i < len; i++) {
                if (i == 0) {
                    format1 = formats[i];
                }
                if (i == 1) {
                    format2 = formats[i];
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format1);
        try {
            Date date = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format2);
            return sdf2.format(date);
        } catch (ParseException e) {
            return null;
        }
    }
}