package com.rafal.pracamagisterska.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal on 2017-06-09.
 */

public class Route implements Serializable {

    private int id;
    private String name;
    private int userId;
    private Double cost;
    private int period;
    private String dateFrom;
    private String dateTo;
    private int seatNumber;
    private String carInfo;
    private int status;
    private int pn;
    private int wt;
    private int sr;
    private int czw;
    private int pt;
    private int sob;
    private int nd;
    private ArrayList<String> nodesList;
    private String userName="";

    public Route() {
    }

    public Route(int id,
                 String name,
                 int userId,
                 Double cost,
                 int period,
                 String dateFrom, String dateTo,
                 int seatNumber,
                 String carInfo,
                 int status,
                 int pn, int wt, int sr,int czw,int pt,int sob,int nd) {
        this.carInfo = carInfo;
        this.cost = cost;
        this.czw = czw;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.id = id;
        this.name = name;
        this.nd = nd;
        this.period = period;
        this.pn = pn;
        this.pt = pt;
        this.seatNumber = seatNumber;
        this.sob = sob;
        this.sr = sr;
        this.status = status;
        this.userId = userId;
        this.wt = wt;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public Double getCost() {
        return cost;
    }

    public int getCzw() {
        return czw;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNd() {
        return nd;
    }

    public int getPeriod() {
        return period;
    }

    public int getPn() {
        return pn;
    }

    public int getPt() {
        return pt;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public int getSob() {
        return sob;
    }

    public int getSr() {
        return sr;
    }

    public int getUserId() {
        return userId;
    }

    public int getWt() {
        return wt;
    }

    @Override
    public String toString() {
        return "Route{" +
                "carInfo='" + carInfo + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", cost=" + cost +
                ", period='" + period + '\'' +
                ", dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", seatNumber=" + seatNumber +
                ", status=" + status +
                ", UserName=" + userName +
                ", pn=" + pn +
                ", wt=" + wt +
                ", sr=" + sr +
                ", czw=" + czw +
                ", pt=" + pt +
                ", sob=" + sob +
                ", nd=" + nd +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getNodesList() {
        return nodesList;
    }

    public void setNodesList(ArrayList<String> nodesList) {
        this.nodesList = nodesList;
    }
}
