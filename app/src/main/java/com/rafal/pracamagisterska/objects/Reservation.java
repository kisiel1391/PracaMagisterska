package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-06-09.
 */

public class Reservation {

    private int routeId;
    private String date;
    private int userId;
    private String confirmed;

    public Reservation() {
    }

    public Reservation(int routeId, String date,  int userId, String confirmed) {
        this.confirmed = confirmed;
        this.date = date;
        this.routeId = routeId;
        this.userId = userId;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "confirmed='" + confirmed + '\'' +
                ", routeId=" + routeId +
                ", date='" + date + '\'' +
                ", userId=" + userId +
                '}';
    }
}
