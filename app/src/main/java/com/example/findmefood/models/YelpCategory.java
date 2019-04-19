package com.example.findmefood.models;

import java.io.Serializable;

public class YelpCategory implements Serializable {
    String alias;
    String title;

    public String getCategoryAlias() {
        return alias;
    }

    public String getCategoryTitle() {
        return title;
    }
}
