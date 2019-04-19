package com.example.findmefood.models;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {

    List<String> display_address;

    public List<String> showAddress() {
        return display_address;
    }
}
