package com.qtiger.news.constant;

import lombok.Getter;

@Getter
public enum NewsCategory {

    SPORT("Sport"),
    ENTERTAINMENT("Entertainment"),
    BUSINESS("Business"),
    TECHNOLOGY("Technology"),
    OTHER("Other");

    NewsCategory(String name) {
        this.name = name;
    }

    private final String name;
}
