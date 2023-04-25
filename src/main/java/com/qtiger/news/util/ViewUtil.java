package com.qtiger.news.util;

import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewUtil {

    public static String parseDate(Date date){
        if (ObjectUtils.isEmpty(date)) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        return dateFormat.format(date);
    }

}
