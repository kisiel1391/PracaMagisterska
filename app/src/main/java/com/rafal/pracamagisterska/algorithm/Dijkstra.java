package com.rafal.pracamagisterska.algorithm;

import android.content.Context;
import android.util.Pair;

import com.rafal.pracamagisterska.database.DatabaseHelper;

/**
 * Created by Rafal on 2017-06-05.
 */

public class Dijkstra extends DatabaseGraphAlgorithm {

    public Dijkstra(Context context) {
        super(context);
    }

    @Override
    void initializeAlgorithm() {
        open = new OpenSet(costAttr, weightAttr);
        closed = new ClosedSet();
        myDb = new DatabaseHelper(context);

        startNode = new GraphState(startNodeID, 0.0, 0.0);

        open.add(startNode);
    }

    @Override
    Double calculateH(Pair<Double, Double> coordinates) {
        return 0.0;
    }


}
