package com.qtiger.news.util;

import com.qtiger.news.entity.Comment;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ViewUtil {

    public static String parseDate(Date date){
        if (ObjectUtils.isEmpty(date)) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return dateFormat.format(date);
    }

    public static String parseDateTime(Date date){
        if (ObjectUtils.isEmpty(date)) {
            return "";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm");
        return dateFormat.format(date);
    }

    public static long getChildCommentNumber(Long commentId, List<Comment> comments) {
        return comments.stream()
                .filter(c -> c.getParentComment() != null && Objects.equals(c.getParentComment().getId(), commentId))
                .count();
    }

}
