package com.rafal.pracamagisterska.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;

import com.rafal.pracamagisterska.objects.Edge;
import com.rafal.pracamagisterska.objects.EdgesDataset;
import com.rafal.pracamagisterska.objects.Node;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Rafal on 2017-04-14.
 */

public class ParsingHelper {

    private static Context context;
    private List<Node> nodesList;
    private List<EdgesDataset.EdgesNodes> edgesNodesList;

    public ParsingHelper(Context ctx){
        this.context = ctx.getApplicationContext();
    }

    public static List parseNodes(InputStream in) {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readNodes(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List readNodes(XmlPullParser parser) {

        List nodeList = new ArrayList();
        String name;
        try {
            parser.require(XmlPullParser.START_TAG, "", "osm");

            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() != XmlPullParser.START_TAG) continue;
                name = parser.getName();
                if(name.equals("node")) nodeList.add(readNode(parser));
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nodeList;
    }

    private static Node readNode(XmlPullParser parser) {
        String ref_id;
        Double lat, lon;

        try {
            parser.require(XmlPullParser.START_TAG, "", "node");
            ref_id = parser.getAttributeValue(null, "id");
            lat = Double.parseDouble( parser.getAttributeValue(null, "lat"));
            lon = Double.parseDouble( parser.getAttributeValue(null, "lon"));
            return new Node(0,ref_id, lat, lon );
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public EdgesDataset parseEdges(InputStream in, List<Node> ndL) {

        nodesList = ndL;
        edgesNodesList = new ArrayList<>();

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readEdges(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private EdgesDataset readEdges(XmlPullParser parser) {

        EdgesDataset oEdgesDataset = new EdgesDataset();

        List edgesList = new ArrayList();
        Edge edge;
        String name;

        try {
            parser.require(XmlPullParser.START_TAG, "", "osm");
            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() != XmlPullParser.START_TAG) continue;
                name = parser.getName();
                if(name.equals("way")){
                    edge = readEdge(parser);
                    edgesList.add(edge);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //edgesList = reapirEdges(edgesList);

        oEdgesDataset.setEdgesList(edgesList);
        oEdgesDataset.setEdgesNodesList(edgesNodesList);

        return oEdgesDataset;
    }

    private Edge readEdge(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "", "way");

        String wayId, firstNode, lastNode, temp, nodesList = "";
        String highway = "";
        String name = "";
        String maxspeed = "";
        String oneway = "";
        Double length;

        wayId = parser.getAttributeValue(null, "id");

        List<String> wayNodes = new ArrayList<>();

        while (parser.nextTag() != XmlPullParser.END_DOCUMENT){
            String tagName = parser.getName();
            //Log.d("Szukanie nodów i tagów", "name: " + tagName);
            if (tagName.equals("way")) break;
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            if (tagName.equals("nd")){
                temp = parser.getAttributeValue(null, "ref");
                wayNodes.add(temp);
                nodesList = nodesList + temp + ";";
            }
            if (tagName.equals("tag")){
                if (parser.getAttributeValue(null, "k").equals("highway")) highway = parser.getAttributeValue(null, "v");
                if (parser.getAttributeValue(null, "k").equals("maxspeed")) maxspeed = parser.getAttributeValue(null, "v");
                if (parser.getAttributeValue(null, "k").equals("name")) name = parser.getAttributeValue(null, "v");
                if (parser.getAttributeValue(null, "k").equals("oneway")) oneway = parser.getAttributeValue(null, "v");
            }
        }

        firstNode = wayNodes.get(0);
        lastNode = wayNodes.get(wayNodes.size()-1);

        length = calculateWayLength(wayNodes);

        for(int i = 1; i <= wayNodes.size() ; i++){
            edgesNodesList.add(new EdgesDataset().new EdgesNodes(wayId, wayNodes.get(i-1), i));
        }

        return new Edge(-1, wayId, firstNode, lastNode, highway, name, maxspeed, oneway, length);
    }

    private Double calculateWayLength(List<String> wayNodes) {
        Double length = 0.0;
        Node nd1 = null, nd2 = null;

        for(int i = 0; i < wayNodes.size()-1; i++){


            for(Node nd :nodesList){
                if (nd.getNodeId().equals(wayNodes.get(i))) nd1 = new Node(nd);
                if (nd.getNodeId().equals(wayNodes.get(i+1))) nd2 = new Node(nd);
                if (nd1 != null & nd2 != null) break;
            }
            //Log.d("calculateWayLength", "len seg is : " + calculateWaySegmentLength(nd1, nd2));

            length += calculateWaySegmentLength(nd1, nd2);
            nd1 = null;
            nd2 = null;
        }
        //Log.d("calculateWayLength", "\tlen is : " + length);
        return length;
    }

    private static Double calculateWaySegmentLength(Node node1, Node node2) {
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

}
