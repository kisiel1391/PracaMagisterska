package com.rafal.pracamagisterska.algorithm;

import android.util.Pair;

/**
 * Created by Rafal on 2017-06-05.
 */
public class GraphState {

    private String id;
    private String parentId;
    private Double distance = 0.0; //distance from me to parent
    private Double gFunction = 0.0; //distance from parent to endNode
    private Double hFunction = 0.0; //heuristic value
    private int maxSpeed;
    private String roadType;
    private Double lat;
    private Double lon;

    public GraphState() {
    }

    public GraphState(String id, String parentId, Double distance, Double lat, Double lon, int maxSpeed, String roadType) {
        this.distance = distance;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.maxSpeed = maxSpeed;
        this.parentId = parentId;
        this.roadType = roadType;
    }

    public GraphState(String startNodeID, double distance, double gFunction) {
        this.id = startNodeID;
        this.distance = distance;
        this.gFunction = gFunction;
    }

    public String getNodeId() {
        return id;
    }

    public Double getG(int costAttr, int weightAttr) {
        if (weightAttr == GraphAlgorithm.NONE){
            if (costAttr == GraphAlgorithm.DISTANCE) return gFunction + distance ;
            else return gFunction + (distance / maxSpeed);
        } else if(weightAttr == GraphAlgorithm.WEIGTH) {
            if (costAttr == GraphAlgorithm.DISTANCE) return gFunction + distance * getWeigth();
            else return gFunction + (distance / maxSpeed) * getWeigth();
        }

        return 1.0;
    }

    private double getWeigth() {
        if(roadType == null)return 1.0;
        if (roadType.equals("motorway") || roadType.equals("motorway_link") ||
            roadType.equals("trunk")  ||  roadType.equals("trunk_link") ) return 0.5;
        else if(roadType.equals("primary") || roadType.equals("primary_link")) return 1.0;
        else if(roadType.equals("secondary") || roadType.equals("tertiary" )) return 1.3;
        else return 5.0;
    }

    public void setG(Double gFunction) {
        this.gFunction = gFunction;
    }

    public Double getF() {
        return distance + gFunction + hFunction;
    }

    public void setH(double hFunction) {
        this.gFunction = hFunction;
    }

    public Pair<Double, Double> getCoordinates(){
        return new Pair<>(lat, lon);
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getParentId() {
        return parentId;
    }

    public Double getDistance(int costAttr) {
        if (costAttr == GraphAlgorithm.DISTANCE) return distance;
        else return (distance / maxSpeed);
    }
}
