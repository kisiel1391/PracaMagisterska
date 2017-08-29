package com.rafal.pracamagisterska.algorithm;

import android.content.Context;
import android.util.Pair;

import com.rafal.pracamagisterska.database.DatabaseHelper;
import com.rafal.pracamagisterska.objects.Node;
import com.rafal.pracamagisterska.objects.Path;

/**
 * Created by Rafal on 2017-06-05.
 */

public class AStar extends DatabaseGraphAlgorithm {

    private int heuristicAttr;

    public AStar(Context context) {
        super(context);
    }

    public Path findPath(String startNodeId, String endNodeId, int costAttr, int weightAttr, int heuristicAttr) {
        this.heuristicAttr = heuristicAttr;
        return findPath(startNodeId, endNodeId, costAttr, weightAttr);
    }

    @Override
    void initializeAlgorithm() {
        open = new OpenSet(costAttr, weightAttr);
        closed = new ClosedSet();
        myDb = new DatabaseHelper(context);

        Node nd  = myDb.getNode(endNodeId);

        endNode = new GraphState(endNodeId, 0.0, 0.0);
        endNode.setLat(nd.getLat());
        endNode.setLon(nd.getLon());


        nd  = myDb.getNode(startNodeID);

        startNode = new GraphState(startNodeID, 0.0, 0.0);
        startNode.setLat(nd.getLat());
        startNode.setLon(nd.getLon());
        startNode.setH(calculateH(startNode.getCoordinates()));

        open.add(startNode);
    }

    @Override
    Double calculateH(Pair<Double, Double> coordinates) {
        if(heuristicAttr == EUCLIDEAN_HEURISTIC) return calculateEuclideanHeuristic(coordinates);
        else if (heuristicAttr == MANHATTAN_HEURISTIC) return calculateManhattanHeuristic(coordinates);
        else return 0.0;
    }

    private Double calculateManhattanHeuristic(Pair<Double, Double> coordinates) {
        Double dLat, dLon,
                lat1 = coordinates.first,
                lon1 = coordinates.second,
                lat2 = endNode.getCoordinates().first,
                lon2 = endNode.getCoordinates().second;

        dLat = lat2 - lat1;
        dLon = lon2 - lon1;

        //return (Math.abs(dLat) + Math.abs(dLon)) * 40075.704 / 360;
        return Math.abs(dLat)/ 0.008983 +  Math.abs(dLon) / 0.015073;
    }

    private Double calculateEuclideanHeuristic(Pair<Double, Double> coordinates) {
        Double distance, dLat, dLon,
                lat1 = coordinates.first,
                lon1 = coordinates.second,
                lat2 = endNode.getCoordinates().first,
                lon2 = endNode.getCoordinates().second;

        dLat = lat2 - lat1;
        dLon = lon2 - lon1;

        distance = dLat * dLat + dLon * dLon * Math.cos(lat1 * Math.PI / 180) * Math.cos(lat1 * Math.PI / 180);
        distance = Math.sqrt(distance) * 40075.704 / 360; //dlugosc w km

        return distance;

    }
}
