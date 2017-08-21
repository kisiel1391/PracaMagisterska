package com.rafal.pracamagisterska.objects;

import android.content.Context;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

import com.rafal.pracamagisterska.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rafal on 2017-05-13.
 */

public class Graph {

    private List<Edge> graph;
    private Context context;

    public Graph(Context context){
        this.context = context.getApplicationContext();
    }

    public Graph(List<Edge> oGraph){
        graph = oGraph;
    }

    public HashMap<String, Double> generateChildren(String parentID){

        HashMap<String, Double> childrens = new HashMap<>();

        for( Edge edge: graph){
            if (edge.getFirstNode().equals(parentID)) childrens.put(edge.getLastNode(), edge.getLength());
            if (edge.getLastNode().equals(parentID) && !edge.getOneway().equals("yes"))childrens.put(edge.getFirstNode(), edge.getLength());
        }

        return childrens;
    }

    public void getGraphFromDatabase() {

        DatabaseHelper myDB = new DatabaseHelper(context);
        myDB.openConnection();
        Cursor cursor = myDB.getAllEdges();
        graph = new ArrayList<>();


        long startTime = SystemClock.elapsedRealtime();
        while(cursor.moveToNext()){
            graph.add(new Edge(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getDouble(8)));
        }

        myDB.closeConnection();
        Log.d("GRAPH", "Created graph in: " + (SystemClock.elapsedRealtime() - startTime)/1000.0);
    }
}
