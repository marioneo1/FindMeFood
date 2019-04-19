package com.example.findmefood.models;

import java.io.Serializable;
import java.util.List;

public class SearchRestaurantsResults implements Serializable {

    List<Restaurant> businesses;

    public List<Restaurant> getBusinesses() {
        return businesses;
    }

    @Override
    public String toString() {
        return "Restaurants found{" +
                "businesses=" + businesses +
                '}';
    }
}
