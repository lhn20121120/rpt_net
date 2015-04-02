/*
 * Created on 2005-12-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.cbrc.smis.common;



/**
 * @author cb
 * 
 * 
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FormatDate {

    /**
     * 该方法返回日期字符串的年份
     * 
     * @param date
     * @return
     */
    public String getYear(String date) {

        String ddd = "2005-3-9";

        return date.substring(0, 4);

    }

    /**
     * 该方法返回日期字符串的月份
     * 
     * @param date
     * @return
     */
    public String getMonth(String date) {

        String date1;

        try {

            date1 = date.substring(5, 7);

            int i = Integer.parseInt(date1);

        } catch (Exception e) {

            date1 = date.substring(5, 6);
        }
        return date1;
    }

    /**
     * 该方法返回日期字符串的天数
     * 
     * @param date
     * @return
     */
    public String getDay(String date) {

        int length = date.length();

        String s = date.substring(length - 2, length - 1);

        if (s.equals("-"))

            return date.substring(length - 1, length);

        return date.substring(length - 2, length);

    }

}