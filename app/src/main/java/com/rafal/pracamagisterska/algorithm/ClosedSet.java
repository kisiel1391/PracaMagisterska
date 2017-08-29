package com.rafal.pracamagisterska.algorithm;

import java.util.HashMap;

/**
 * Created by Rafal on 2017-06-05.
 */

public class ClosedSet {

    private HashMap<String, GraphState> closed;


    public ClosedSet() {
        closed = new HashMap<>();
    }

    public void add(GraphState actualnNode) {
        closed.put(actualnNode.getNodeId(), actualnNode);
    }

    public boolean contains(String nodeId) {
        return closed.containsKey(nodeId);
    }

    public GraphState get(String id){
        return closed.get(id);
    }

    public int size() {
        return closed.size();
    }
}
