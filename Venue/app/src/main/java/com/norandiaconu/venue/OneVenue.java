package com.norandiaconu.venue;

/**
 * Created by diaconuna on 11/19/2015.
 */
public class OneVenue {
    int id;
    String messages;
    String name;
    double longitude;
    double latitude;
    double radius;
    String created;

    public OneVenue(int id, String messages, String name, double longitude,
                     double latitude, double radius, String created) {
        this.id = id;
        this.messages = messages;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.created = created;
    }

}