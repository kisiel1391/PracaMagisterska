package com.rafal.pracamagisterska.objects;

import java.util.List;

/**
 * Created by Rafal on 2017-04-18.
 */

public class EdgesDataset {

    private List<Edge> edgesList;
    private List<EdgesNodes> edgesNodesList;

    public class  EdgesNodes{
        private String edge_id;
        private String node_id;
        private int node_position;

        public EdgesNodes(String edge_id, String node_id, int node_position){
            this.edge_id = edge_id;
            this.node_id = node_id;
            this.node_position = node_position;
        }

        public String getEdge_id() {
            return edge_id;
        }

        public void setEdge_id(String edge_id) {
            this.edge_id = edge_id;
        }

        public String getNode_id() {
            return node_id;
        }

        public void setNode_id(String node_id) {
            this.node_id = node_id;
        }

        public int getNode_position() {
            return node_position;
        }

        public void setNode_position(int node_position) {
            this.node_position = node_position;
        }
    }

    public List<Edge> getEdgesList() {
        return edgesList;
    }

    public void setEdgesList(List<Edge> edgesList) {
        this.edgesList = edgesList;
    }

    public List<EdgesNodes> getEdgesNodesList() {
        return edgesNodesList;
    }

    public void setEdgesNodesList(List<EdgesNodes> edgesNodesList) {
        this.edgesNodesList = edgesNodesList;
    }
}
