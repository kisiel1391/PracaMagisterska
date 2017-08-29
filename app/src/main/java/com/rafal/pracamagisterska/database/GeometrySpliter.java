package com.rafal.pracamagisterska.database;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Pair;

import com.rafal.pracamagisterska.objects.Edge;
import com.rafal.pracamagisterska.objects.EdgesDataset;
import com.rafal.pracamagisterska.objects.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal on 2017-05-17.
 */

public class GeometrySpliter {

    public Context context;

    DatabaseHelper myDb;

    private class Triplet<T, U, V> {

        private final T first;
        private final U second;
        private final V third;

        private Triplet(T first, U second, V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public T getEdge() { return first; }
        public U getCrossingEdge() { return second; }
        public V getCrossingNode() { return third; }
    }

    public GeometrySpliter(Context context){
        this.context = context;
    }

    public void splitGeometry() {
        myDb = new DatabaseHelper(context);
        myDb.openConnection();

        Cursor cursor = myDb.getAllEdges();
        List<Triplet<String, String, String>> crossingTriplet = new ArrayList<>();

        while(cursor.moveToNext()){
            System.out.println(cursor.getString(1));
            //long start = SystemClock.elapsedRealtime();
            Cursor  crossingEdges = myDb.findCrossingEdge(cursor.getString(1));
            //System.out.println("Searching time: " + (SystemClock.elapsedRealtime() - start)/1000.0);
            while(crossingEdges.moveToNext()){
                crossingTriplet.add(new Triplet<>(cursor.getString(1),              //edge_id
                                                  crossingEdges.getString(1),       //node_id
                                                  crossingEdges.getString(2)));     //position
            }
        }

        cursor.close();
        System.out.println("Points to split: " + crossingTriplet.size());

        //myDb.openTransaction();

        long start;
        int iterator = 1;

        for(Triplet<String, String, String> t : crossingTriplet){
            //wyciagnij EDGE z bazy i usun
            Edge edgeToSplit = myDb.getEdgeAndDelete(t.getEdge());

            //rozetnij EDGE na E1 i E2
            Edge firstEdgeAfterSplit = new Edge(edgeToSplit);
            Edge secondEdgeAfterSplit = new Edge(edgeToSplit);

            //poprawa lastNode1 i firstNode2
            firstEdgeAfterSplit.setWayId(firstEdgeAfterSplit.getWayId() + "_" + t.getCrossingNode());
            firstEdgeAfterSplit.setLastNode(t.getCrossingNode());
            secondEdgeAfterSplit.setFirstNode(t.getCrossingNode());

            //pobranie listy wierzcholki w celu modyfikacji w EDGES-NODES
            List<String> edgeToSplitVertexes = myDb.getVertexes(t.getEdge());
            List<String>firstEdgeAfterSplitNodesList = new ArrayList<>();
            List<EdgesDataset.EdgesNodes> firstEdgeAfterSplitNodes = new ArrayList<>();
            List<EdgesDataset.EdgesNodes> secondEdgeAfterSplitNodes = new ArrayList<>();

            //System.out.println("\t\tEdge to split info " + edgeToSplit.getWayId() + " " + edgeToSplit.getFirstNode() + " "  + edgeToSplit.getLastNode());
            //System.out.println("\t\tSplitter info " + t.getEdge() + " " + t.getCrossingNode());

            //utworzenie nowych list wierzcholkow
            int idx = 1;
            for(int i = edgeToSplitVertexes.indexOf(firstEdgeAfterSplit.getFirstNode());
                    i <= edgeToSplitVertexes.indexOf(t.getCrossingNode());
                    i++){
                firstEdgeAfterSplitNodesList.add(edgeToSplitVertexes.get(i));
                firstEdgeAfterSplitNodes.add(new EdgesDataset().new EdgesNodes(firstEdgeAfterSplit.getWayId(),
                                                                                edgeToSplitVertexes.get(i),
                                                                                idx++));
            }

            idx = 1;

            for(int j = edgeToSplitVertexes.indexOf(secondEdgeAfterSplit.getFirstNode());
                    j <= edgeToSplitVertexes.lastIndexOf(secondEdgeAfterSplit.getLastNode());
                    j++){
                secondEdgeAfterSplitNodes.add(new EdgesDataset().new EdgesNodes(secondEdgeAfterSplit.getWayId(),
                                                                                edgeToSplitVertexes.get(j),
                                                                                idx++));
            }

            firstEdgeAfterSplit.setLength(calculateEdgeLength(firstEdgeAfterSplitNodesList));
            secondEdgeAfterSplit.setLength(edgeToSplit.getLength() - firstEdgeAfterSplit.getLength());

            List<Edge> eL = new ArrayList<>();
            eL.add(firstEdgeAfterSplit);
            eL.add(secondEdgeAfterSplit);

            //System.out.println("\t\tEdge1 to info " + firstEdgeAfterSplit.getWayId() + " " + firstEdgeAfterSplit.getFirstNode() + " "  + firstEdgeAfterSplit.getLastNode());
            //System.out.println("\t\tEdge2 to split info" + secondEdgeAfterSplit.getWayId() + " " + secondEdgeAfterSplit.getFirstNode() + " "  + secondEdgeAfterSplit.getLastNode());


            myDb.insertEdgesT(eL);
            myDb.deleteEdgesNodesForEdge(t.getEdge());
            myDb.insertEdgesNodesT(firstEdgeAfterSplitNodes);
            myDb.insertEdgesNodesT(secondEdgeAfterSplitNodes);



            System.out.println("Split node " + iterator++ + " / " + crossingTriplet.size() );
        }

        //myDb.commitTransaction();

        myDb.closeConnection();
    }

    private Double calculateEdgeLength(List<String> firstEdgeAfterSplitNodesList) {
        Double len = 0.0;

        long start;
        for(int i = 0; i < firstEdgeAfterSplitNodesList.size() - 1; i++){
            //start = SystemClock.elapsedRealtime();

           /* Node n1 = myDb.getNodeT(firstEdgeAfterSplitNodesList.get(i));
            Node n2 = myDb.getNodeT(firstEdgeAfterSplitNodesList.get(i+1));*/
            Pair<Double, Double> n1 = myDb.getNodeT2(firstEdgeAfterSplitNodesList.get(i));
            Pair<Double, Double> n2 = myDb.getNodeT2(firstEdgeAfterSplitNodesList.get(i+1));
            //System.out.println("\tWyciagniecie NODOW in " + (SystemClock.elapsedRealtime() - start)/1000.0);
            //start = SystemClock.elapsedRealtime();
            len += calculateSegmentLength(n1, n2);
            //System.out.println("\tObliczenie segmentu in " + (SystemClock.elapsedRealtime() - start)/1000.0);

        }
        return len;
    }

    private Double calculateSegmentLength(Node node1, Node node2) {
        Double sLength;
        Double lat1, lat2, lon1, lon2, dLat, dLon;

        lat1 = node1.getLat();
        lon1 = node1.getLon();

        lat2 = node2.getLat();
        lon2 = node2.getLon();

        dLat = lat2 - lat1;
        dLon = lon2 - lon1;

        sLength = dLat*dLat + dLon*dLon * Math.cos(lat1*Math.PI/180) * Math.cos(lat1*Math.PI/180);
        sLength = Math.sqrt(sLength)*40075.704/360; //dlugosc w km

        return sLength;
    }

    private Double calculateSegmentLength(Pair<Double, Double> node1, Pair<Double, Double> node2) {
        Double sLength;
        Double lat1, lat2, lon1, lon2, dLat, dLon;

        lat1 = node1.first;
        lon1 = node1.second;

        lat2 = node2.first;
        lon2 = node2.second;

        dLat = lat2 - lat1;
        dLon = lon2 - lon1;

        sLength = dLat*dLat + dLon*dLon * Math.cos(lat1*Math.PI/180) * Math.cos(lat1*Math.PI/180);
        sLength = Math.sqrt(sLength)*40075.704/360; //dlugosc w km

        return sLength;
    }
}
