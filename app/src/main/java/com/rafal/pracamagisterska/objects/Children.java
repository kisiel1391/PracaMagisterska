package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-05-27.
 */

public class Children {
    private String ref_id;
    private Double length;
    private String maxspeed;
    private Double lat;
    private Double lon;

    public Children() {
    }

    public Children(String ref_id,Double length, String maxspeed, Double lat, Double lon ) {
        this.ref_id = ref_id;
        this.length = length;
        this.maxspeed = maxspeed;
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLength() {
        return length;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getMaxspeed() {
        return maxspeed;
    }

    public String getRef_id() {
        return ref_id;
    }
}
