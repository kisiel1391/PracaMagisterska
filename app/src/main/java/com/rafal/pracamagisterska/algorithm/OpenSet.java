package com.rafal.pracamagisterska.algorithm;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Rafal on 2017-06-05.
 */

public class OpenSet {

    private int costAttr;
    private PriorityQueue<GraphState> open;
    protected Comparator<GraphState> comparator;

    public OpenSet(final int costAttr, final int weightAttr) {
        this.costAttr = costAttr;
        comparator = new Comparator<GraphState>() {
            @Override
            public int compare(GraphState gs1, GraphState gs2) {
                if(gs1.getG(costAttr, weightAttr) < gs2.getG(costAttr, weightAttr)) return -1;
                else return 1;
            }
        };
        open = new PriorityQueue<>(1,comparator);
    }

    public void add(GraphState graphState) {
        open.add(graphState);
    }

    public boolean isEmpty() {
        return open.isEmpty();
    }

    public GraphState poll() {
        
        return open.poll();
    }

    public boolean contains(GraphState graphState) {
        Iterator<GraphState> iterator = open.iterator();
        while (iterator.hasNext()){
            GraphState gs = iterator.next();
            if(gs.getNodeId().equals(graphState.getNodeId())) return true;
        }
        return false;
    }

    public GraphState get(String childId) {
        Iterator<GraphState> iterator = open.iterator();
        while (iterator.hasNext()){
            GraphState gs = iterator.next();
            if(gs.getNodeId().equals(childId)) return gs;
        }
        return null;
    }

    public void update(GraphState child) {
        Iterator<GraphState> iterator = open.iterator();
        while (iterator.hasNext()){
            GraphState gs = iterator.next();
            if(gs.getNodeId().equals(child.getNodeId())) {
                open.remove(gs);
                open.add(child);
                return;
            }
        }
    }

    public int size(){
        return open.size();
    }
}
