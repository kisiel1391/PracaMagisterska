package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-04-13.
 */

public class Edge {
    private int id;
    private String wayId;
    private String firstNode;
    private String lastNode;
    private String highway;
    private String name;
    private String maxspeed;
    private String oneway;
    private Double length;

    public String getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(String firstNode) {
        this.firstNode = firstNode;
    }

    public String getHighway() {
        return highway;
    }

    public void setHighway(String highway) {
        this.highway = highway;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastNode() {
        return lastNode;
    }

    public void setLastNode(String lastNode) {
        this.lastNode = lastNode;
    }

    public String getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(String maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOneway() {
        return oneway;
    }

    public void setOneway(String oneway) {
        this.oneway = oneway;
    }

    public String getWayId() {
        return wayId;
    }

    public void setWayId(String wayId) {
        this.wayId = wayId;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Edge(){}

    public Edge(Edge ed){
        this.id = ed.getId();
        this.wayId = ed.getWayId();
        this.firstNode = ed.getFirstNode();
        this.lastNode = ed.getLastNode();
        this.highway = ed.getHighway();
        this.name = ed.getName();
        this.maxspeed = ed.getMaxspeed();
        this.oneway = ed.getOneway();
        this.length = ed.getLength();
    }

    public Edge(int oId, String oWayId, String oFirstNode, String oLastNode, String oHighway,
                String oName, String oMaxspeed, String oOneway, Double oLength){
        this.id = oId;
        this.wayId = oWayId;
        this.firstNode = oFirstNode;
        this.lastNode = oLastNode;
        this.highway = oHighway;
        this.name = oName;
        this.maxspeed = oMaxspeed;
        this.oneway = oOneway;
        this.length = oLength;
    }


}
