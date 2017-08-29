package com.rafal.pracamagisterska.objects;

import android.os.SystemClock;

import com.rafal.pracamagisterska.algorithm.GraphState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal on 2017-06-05.
 */

public class Path {

    List<GraphState> path;
    Double cost = 0.0;
    Double algTime;
    int openSetSize;
    int closeSetSize;
    String startRoadName, endRoadName;

    public Path() {
        path = new ArrayList<>();
    }

    public void add(GraphState graphState) {
        path.add(graphState);
    }

    public ArrayList<String> getPathAsList(){
        ArrayList<String> list = new ArrayList<>();
        for (GraphState gs: path ) {
            list.add(gs.getNodeId());
        }
        return list;
    }

    public int getCloseSetSize() {
        return closeSetSize;
    }

    public void setCloseSetSize(int closeSetSize) {
        this.closeSetSize = closeSetSize;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getOpenSetSize() {
        return openSetSize;
    }

    public void setOpenSetSize(int openSetSize) {
        this.openSetSize = openSetSize;
    }

    public Double getAlgTime() {
        return algTime;
    }

    public void setAlgTime(Double algTime) {
        this.algTime = algTime;
    }

    public String toString(){
        return "Found path in " + algTime + "  \tTotal cost " + cost + " \tEdges " + + path.size() + "  \tOpenSet size " + openSetSize + "  \tClosedSet size " + closeSetSize;
    }

    public String getEndRoadName() {
        return endRoadName;
    }

    public void setEndRoadName(String endRoadName) {
        this.endRoadName = endRoadName;
    }

    public String getStartRoadName() {
        return startRoadName;
    }

    public void setStartRoadName(String startRoadName) {
        this.startRoadName = startRoadName;
    }
}
