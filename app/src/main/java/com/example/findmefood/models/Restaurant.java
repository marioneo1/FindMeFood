package com.example.findmefood.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial") //hide compiler warning for serial
public class Restaurant implements Serializable {
    private String name;
    private String alias;
    private String phone;
    private String display_phone;
    private String price;
    private String image_url;
    private String url;
    private Boolean is_closed;
    private double rating;
    private double distance;
    private Location location;
    private Coordinates coordinates;


    public List<YelpCategory> getCategories() {
        return categories;
    }

    private List<YelpCategory> categories;

    public String getImage_url() {
        return image_url;
    }

    public String getUrl() {return url;}

    public Location getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getPhone() {
        return phone;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public double getDistance() {
        return distance;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getName(){
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public Boolean getIs_closed() {
        return is_closed;
    }

    public double getRating() {
        return rating;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", is_closed=" + is_closed +
                ", rating=" + rating +
                '}';
    }

}
