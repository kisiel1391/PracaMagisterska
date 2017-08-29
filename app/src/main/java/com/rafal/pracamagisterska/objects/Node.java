package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-04-13.
 */

public class Node {
    private int id;
    private String nodeId;
    private Double lat;
    private Double lon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Node(){}

    public Node(Node nd){
        this.id = nd.getId();
        this.nodeId = nd.getNodeId();
        this.lat = nd.getLat();
        this.lon = nd.getLon();
    }

    public Node(int oId, String oNodeId,Double oLat, Double oLon ){
        this.id = oId;
        this.nodeId = oNodeId;
        this.lat = oLat;
        this.lon = oLon;
    }

}
