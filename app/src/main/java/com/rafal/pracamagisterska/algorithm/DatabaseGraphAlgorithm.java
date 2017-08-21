package com.rafal.pracamagisterska.algorithm;

import android.content.Context;
import android.os.SystemClock;
import android.util.Pair;

import com.rafal.pracamagisterska.database.DatabaseHelper;
import com.rafal.pracamagisterska.objects.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafal on 2017-06-05.
 */

public abstract class DatabaseGraphAlgorithm implements GraphAlgorithm {

    public Context context;
    int costAttr, weightAttr;
    OpenSet open;
    ClosedSet closed;
    GraphState startNode, actualnNode, endNode;
    DatabaseHelper myDb;
    String startNodeID, endNodeId;
    List<GraphState> children;
    long timer;


    public DatabaseGraphAlgorithm(Context context) {
        this.context = context;
    }

    @Override
    public Path findPath(String startNodeId, String endNodeId, int costAttr, int weightAttr){
        this.costAttr = costAttr;
        this.weightAttr = weightAttr;
        this.startNodeID = startNodeId;
        this.endNodeId = endNodeId;

        initializeAlgorithm();
        timer = SystemClock.elapsedRealtime();

        while(!open.isEmpty()){

            actualnNode = open.poll();

            children = new ArrayList<>();
            children = myDb.getChildrenAsGraphState(actualnNode.getNodeId());

            if(children.isEmpty()){
                closed.add(actualnNode);
                continue;
            }

            for(GraphState child : children){
                if(closed.contains(child.getNodeId())) continue;

                child.setG(actualnNode.getG(costAttr, weightAttr));
                child.setH(calculateH(child.getCoordinates()));

                if(!open.contains(child)) open.add(child);
                else{
                    if(child.getF() < open.get(child.getNodeId()).getF()){
                        open.update(child);
                    }
                }
            }

            closed.add(actualnNode);
            if(actualnNode.getNodeId().equals(endNodeId)) {return getPath();}
        }

        return null;
    }

    private Path getPath() {
        String parent = endNodeId;

        Path path = new Path();
        path.setOpenSetSize(open.size());
        path.setCloseSetSize(closed.size());
        path.setCost(closed.get(parent).getG(costAttr, weightAttr));
        path.setAlgTime((SystemClock.elapsedRealtime() - timer) / 1000.0);

        while(!parent.equals(startNodeID)){
            GraphState graphState = closed.get(parent);
            path.add(graphState);
            path.setCost(path.getCost() + graphState.getDistance(costAttr));
            parent = graphState.getParentId();

        }


        return path;
    }

    abstract void initializeAlgorithm();

    abstract Double calculateH(Pair<Double, Double> coordinates);
}
