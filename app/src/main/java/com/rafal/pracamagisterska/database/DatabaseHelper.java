package com.rafal.pracamagisterska.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Pair;

import com.rafal.pracamagisterska.algorithm.GraphState;
import com.rafal.pracamagisterska.objects.Children;
import com.rafal.pracamagisterska.objects.Edge;
import com.rafal.pracamagisterska.objects.EdgesDataset;
import com.rafal.pracamagisterska.objects.Node;
import com.rafal.pracamagisterska.objects.Rate;
import com.rafal.pracamagisterska.objects.Route;
import com.rafal.pracamagisterska.objects.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rafal on 2017-04-13.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    SQLiteDatabase sqlDB;

    //////////////////////////////////////////////////////
    public static final String DATABASE_NAME = "roads.db";
    public static final String DATABASE_PATH = "/data/data/com.rafal.pracamagisterska/databases/";

    ////////////////////////NODES/////////////////////////
    public static final String NODES_TABLE_NAME =  "NODES";

    public static final String COLUMN_ID =  "ID";
    public static final String COLUMN_REF_ID =  "REF_ID";
    public static final String COLUMN_LAT =  "LAT";
    public static final String COLUMN_LON =  "LON";

    ////////////////////////EDGES/////////////////////////
    public static final String EDGES_TABLE_NAME =  "EDGES";

    public static final String COLUMN_FIRST_NODE =  "FIRST_NODE";
    public static final String COLUMN_LAST_NODE =  "LAST_NODE";
    public static final String COLUMN_HIGHWAY =  "HIGHWAY";
    public static final String COLUMN_NAME =  "NAME";
    public static final String COLUMN_MAXSPEED =  "MAXSPEED";
    public static final String COLUMN_ONEWAY =  "ONEWAY";
    public static final String COLUMN_LENGTH =  "LENGTH";

    //////////////////EDGES_NODES/////////////////////////
    public static final String EDGES_NODES_TABLE_NAME =  "EDGES_NODES";

    public static final String COLUMN_EDGE_ID = "EDGE_ID";
    public static final String COLUMN_NODE_ID = "NODE_ID";
    public static final String COLUMN_NODE_POSITION = "NODE_POSITION";

    //////////////////////////////////////////////////////

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlNodesTable = "CREATE TABLE IF NOT EXISTS " +
                NODES_TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REF_ID + " TEXT, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LON + " REAL)";

        String sqlEdgesTable = "CREATE TABLE IF NOT EXISTS " +
                EDGES_TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REF_ID + " TEXT, " +
                COLUMN_FIRST_NODE + " TEXT, " +
                COLUMN_LAST_NODE + " TEXT, " +
                COLUMN_HIGHWAY + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_MAXSPEED + " TEXT, " +
                COLUMN_ONEWAY + " TEXT, " +
                COLUMN_LENGTH + " REAL)";

        String sqlEdgesNodesTable = "CREATE TABLE IF NOT EXISTS " +
                EDGES_NODES_TABLE_NAME + " ( " +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EDGE_ID + " TEXT, " +
                COLUMN_NODE_ID + " TEXT, " +
                COLUMN_NODE_POSITION + " INTEGER )";

        String sqlCreateFirstNodeIndex = "CREATE INDEX FIRST_NODE_IDX ON " +
                EDGES_TABLE_NAME + " ( " + COLUMN_FIRST_NODE + " )";

        String sqlCreateLastNodeIndex = "CREATE INDEX LAST_NODE_IDX ON " +
                EDGES_TABLE_NAME + " ( " + COLUMN_LAST_NODE + " )";

        String sqlCreateEdgeIdIndex = "CREATE INDEX EDGE_ID_IDX ON " +
                EDGES_NODES_TABLE_NAME + " ( " + COLUMN_EDGE_ID + " )";

        String sqlCreateNodeIdIndex = "CREATE INDEX NODE_ID_IDX ON " +
                EDGES_NODES_TABLE_NAME + " ( " + COLUMN_NODE_ID + " )";

        String sqlCreateNodePositionIndex = "CREATE INDEX EDGE_POS_IDX ON " +
                EDGES_NODES_TABLE_NAME + " ( " + COLUMN_NODE_POSITION + " )";

        String sqlCreateNodeIdInNodesIndex = "CREATE INDEX NODE_ID2_IDX ON " +
                NODES_TABLE_NAME + " ( " + COLUMN_REF_ID + " )";

        db.execSQL(sqlNodesTable);
        db.execSQL(sqlEdgesTable);
        db.execSQL(sqlEdgesNodesTable);
        db.execSQL(sqlCreateFirstNodeIndex);
        db.execSQL(sqlCreateLastNodeIndex);
        db.execSQL(sqlCreateEdgeIdIndex);
        db.execSQL(sqlCreateNodeIdIndex);
        db.execSQL(sqlCreateNodePositionIndex);
        db.execSQL(sqlCreateNodeIdInNodesIndex);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlNodesTable = "DROP TABLE IF EXISTS " + NODES_TABLE_NAME;
        String sqlEdgesTable = "DROP TABLE IF EXISTS " + EDGES_TABLE_NAME;
        String sqlEdgesNodesTable = "DROP TABLE IF EXISTS " + EDGES_NODES_TABLE_NAME;

        db.execSQL(sqlNodesTable);
        db.execSQL(sqlEdgesTable);
        db.execSQL(sqlEdgesNodesTable);
        onCreate(db);
    }

    public void copyDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);

        this.getReadableDatabase();

        try {
            copyDatabaseFromAssets(context);
            System.out.println("Skopiowano bazę prawidłowo HEJ!");
        } catch (IOException e) {
            System.out.println("To gówno nie działa - błąd przy kopiowaniu");
            e.printStackTrace();
        }


    }


    public void deleteEdgesNodesForEdge(String edgeId){

        sqlDB.delete(EDGES_NODES_TABLE_NAME, COLUMN_EDGE_ID + " = " + edgeId,  null);

    }


    public List<String> getVertexes(String edgeId) {
        String sql = "SELECT " + COLUMN_NODE_ID + " FROM " + EDGES_NODES_TABLE_NAME +
                     " WHERE " + COLUMN_EDGE_ID + " = " + edgeId + " ORDER BY " + COLUMN_NODE_POSITION;

        Cursor cursor = sqlDB.rawQuery(sql, null);
        List<String> myVertexes = new ArrayList<>();

        while(cursor.moveToNext()){
            myVertexes.add(cursor.getString(0));
        }

        cursor.close();
        return myVertexes;
    }


    public Edge getEdgeAndDelete(String edgeId) {
        //long start = SystemClock.elapsedRealtime();
        String sql = "SELECT * FROM " + EDGES_TABLE_NAME + " WHERE " + COLUMN_REF_ID + " = " + edgeId;

        Cursor cursor = sqlDB.rawQuery(sql, null);

        if(cursor.moveToNext()){

            Edge edge = new Edge(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getDouble(8)
            );
//System.out.println("Select time: " + ((SystemClock.elapsedRealtime() - start)/1000.0));
            cursor.close();
//start = SystemClock.elapsedRealtime();
            int result = sqlDB.delete(EDGES_TABLE_NAME, COLUMN_REF_ID + " = " + edgeId, null);
  //          System.out.println("Delete time: " + ((SystemClock.elapsedRealtime() - start)/1000.0));
            return edge;
        }

        return null;
    }


    public Node getNode(String nodeID){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + NODES_TABLE_NAME + " WHERE " + COLUMN_REF_ID + " = " + nodeID +  " limit 1"; // add limit

        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();
        Node node = new Node(c.getInt(0),
                             c.getString(1),
                             c.getDouble(2),
                             c.getDouble(3));
        c.close();
        db.close();
        return node;
    }

    public Cursor getAllEdges(){
        String sql = "SELECT * FROM " + EDGES_TABLE_NAME;

        Cursor cursor = sqlDB.rawQuery(sql, null);
        //db.close();
        return cursor;
    }

    public void openConnection() {
        sqlDB  = this.getReadableDatabase();
    }

    public void closeConnection(){
        sqlDB.close();
    }

    public Cursor findCrossingEdge(String edgeID) {

        String sql = "SELECT * FROM " + EDGES_NODES_TABLE_NAME +
                     " WHERE " +  COLUMN_EDGE_ID  + " = '" + edgeID + "'"+
                     " AND " + COLUMN_NODE_ID + " in (SELECT DISTINCT " + COLUMN_NODE_ID  + " FROM " + EDGES_NODES_TABLE_NAME +
                     " WHERE " + COLUMN_EDGE_ID + " <> '" +  edgeID  + "' AND " +
                       COLUMN_NODE_ID + " IN ( SELECT " + COLUMN_NODE_ID + " FROM " + EDGES_NODES_TABLE_NAME +
                     " WHERE " + COLUMN_EDGE_ID + " = '" + edgeID + "' AND " +
                       COLUMN_NODE_POSITION + " <> 1 AND " +
                       COLUMN_NODE_POSITION + " <> ( SELECT MAX( " + COLUMN_NODE_POSITION + " ) FROM " + EDGES_NODES_TABLE_NAME +
                     " WHERE " + COLUMN_EDGE_ID + " = '" + edgeID + "')))" +
                     " ORDER BY " +  COLUMN_NODE_POSITION;


        Cursor cursor = sqlDB.rawQuery(sql, null);

        return cursor;
    }


    public Pair<Double, Double> getNodeT2(String nodeId) {
        //System.out.println("-----------------------------");
        //long start = SystemClock.elapsedRealtime();
        String sql = "SELECT * FROM " + NODES_TABLE_NAME + " WHERE " + COLUMN_REF_ID + " = " + nodeId + " LIMIT 1";
        Cursor c = sqlDB.rawQuery(sql, null);
        //System.out.println("\tRaw query " + (SystemClock.elapsedRealtime() - start)/1000.0);
        //start = SystemClock.elapsedRealtime();
        c.moveToNext();
        //System.out.println("\tMove cursor " + (SystemClock.elapsedRealtime() - start)/1000.0);
        //start = SystemClock.elapsedRealtime();
        Pair <Double, Double> oPair = new Pair<>(c.getDouble(2), c.getDouble(3) );
        //System.out.println("\tCreaate Pair " + (SystemClock.elapsedRealtime() - start)/1000.0);
        c.close();
        return oPair ;
    }

    public void insertEdgesT(List<Edge> eL) {

        String sql = "INSERT INTO " + EDGES_TABLE_NAME + " (" +
                COLUMN_REF_ID + ", " +
                COLUMN_FIRST_NODE + " , " +
                COLUMN_LAST_NODE + " , " +
                COLUMN_HIGHWAY + " , " +
                COLUMN_NAME + " , " +
                COLUMN_MAXSPEED + " , " +
                COLUMN_ONEWAY + " , " +
                COLUMN_LENGTH + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement stmt = sqlDB.compileStatement(sql);

        for (Edge e : eL) {
            stmt.bindString(1, e.getWayId());
            stmt.bindString(2, e.getFirstNode());
            stmt.bindString(3, e.getLastNode());
            stmt.bindString(4, e.getHighway());
            stmt.bindString(5, e.getName());
            stmt.bindString(6, e.getMaxspeed());
            stmt.bindString(7, e.getOneway());
            stmt.bindDouble(8, e.getLength());

            stmt.execute();
            stmt.clearBindings();
        }
    }

    public void insertEdgesNodesT(List<EdgesDataset.EdgesNodes> enn) {
        String sql = "INSERT INTO " + EDGES_NODES_TABLE_NAME + " ( " +
                COLUMN_EDGE_ID + " , " +
                COLUMN_NODE_ID + " , " +
                COLUMN_NODE_POSITION + " ) VALUES (?, ?, ?)";

        SQLiteStatement stmt = sqlDB.compileStatement(sql);


        for(EdgesDataset.EdgesNodes en: enn){
            stmt.bindString(1, en.getEdge_id());
            stmt.bindString(2, en.getNode_id());
            stmt.bindLong(3, en.getNode_position());
            //Log.d("DatabaseHelper", "STMT: " + en.getEdge_id() + " " + en.getNode_id() +  " " + en.getNode_position());
            stmt.execute();
            stmt.clearBindings();
        }
    }

    private void copyDatabaseFromAssets(Context context) throws IOException{

        InputStream inputDatabaseStream = context.getAssets().open(DATABASE_NAME);

        String outFileDB = DATABASE_PATH + DATABASE_NAME;

        OutputStream outputDatabaseStream = new FileOutputStream(outFileDB);

        byte[] buffer = new byte[1024];
        int length;
        while((length = inputDatabaseStream.read(buffer)) > 0){
            outputDatabaseStream.write(buffer, 0, length);
        }

        outputDatabaseStream.flush();
        outputDatabaseStream.close();
        inputDatabaseStream.close();

    }

    public ArrayList<String> getNodesForAutoCompleteTextView() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + COLUMN_NAME + ", " + COLUMN_FIRST_NODE +
                    " FROM " + EDGES_TABLE_NAME +
                    " ORDER BY " + COLUMN_NAME;
        ArrayList<String> nodesList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            nodesList.add(cursor.getString(0) + " (" + cursor.getString(1) + ")");
        }

        return nodesList;
    }

    public List<GraphState> getChildrenAsGraphState(String nodeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT E." + COLUMN_FIRST_NODE + //0
                ", E." + COLUMN_LAST_NODE + //1
                ", E." + COLUMN_LENGTH + //2
                ", E." + COLUMN_ONEWAY + //3
                ", E." + COLUMN_MAXSPEED + //4
                ", E. " + COLUMN_HIGHWAY + //5
                ", N." + COLUMN_LAT + //6
                ", N." + COLUMN_LON + //7
                ", N2." + COLUMN_LAT + //8
                ", N2." + COLUMN_LON + //9
                " FROM " + EDGES_TABLE_NAME + " E " +
                " JOIN " + NODES_TABLE_NAME + " N ON N." + COLUMN_REF_ID + " = E." + COLUMN_FIRST_NODE +
                " JOIN " + NODES_TABLE_NAME + " N2 ON N2. " + COLUMN_REF_ID + " = E." + COLUMN_LAST_NODE +
                " WHERE " + COLUMN_FIRST_NODE + " = " + nodeId + " OR " +
                COLUMN_LAST_NODE + " = " + nodeId;

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() == 0) return null;

        List<GraphState> children = new ArrayList<>();

        while(cursor.moveToNext()){
            String  maxspeed = cursor.getString(4).isEmpty() ? "50" : cursor.getString(4);
            if(cursor.getString(0).equals(nodeId)) {
                children.add(new GraphState(cursor.getString(1),
                                            nodeId,
                                            cursor.getDouble(2),
                                            cursor.getDouble(8),
                                            cursor.getDouble(9),
                                            Integer.parseInt(maxspeed),
                                            cursor.getString(5)));
            }
            else if(!cursor.getString(3).equals("yes") && cursor.getString(1).equals(nodeId)){
                children.add(new GraphState(cursor.getString(0),
                                            nodeId,
                                            cursor.getDouble(2),
                                            cursor.getDouble(6),
                                            cursor.getDouble(7),
                                            Integer.parseInt(maxspeed),
                                            cursor.getString(5)));

            }
        }

        cursor.close();
        db.close();

        return children;
    }

    public List<User> getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM USERS";
        List<User> userList = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            userList.add(new User(cursor.getInt(0),
                                  cursor.getString(1),
                                  cursor.getString(2),
                                  cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)));
        }

        cursor.close();
        db.close();
        return userList;

    }

    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM USERS WHERE ID = " + id;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();

        User user = new User(cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));

        cursor.close();
        db.close();
        return user;
    }

    public List<Rate> getRating() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM RATINGS";
        List<Rate> rating = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        System.out.println("eeeee " + cursor.getCount());

        while (cursor.moveToNext()){
            rating.add(new Rate(cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getString(4)));
        }

        cursor.close();
        db.close();

        return rating;
    }

    public Double getRateForUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(VALUE) / COUNT(VALUE) AS AVG FROM RATINGS WHERE CONCERNS_USER_ID = " + id;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();

        Double d = cursor.getDouble(0);
        cursor.close();
        db.close();
        return d;
    }

    public void addRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "INSERT INTO ROUTES (NAME, USER_ID, COST, PERIOD, DATE_FROM, DATE_TO, SEATS_NUMBER, CAR_INFO, STATUS, PN, WT, SR, CZW, PT, SOB, ND)" +
                    " VALUES (?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement stmt = db.compileStatement(sql);

        stmt.bindString(1,route.getName());
        stmt.bindLong(2, route.getUserId());
        stmt.bindDouble(3, route.getCost());
        stmt.bindLong(4, route.getPeriod());
        stmt.bindString(5, route.getDateFrom());
        stmt.bindString(6, route.getDateTo());
        stmt.bindLong(7, route.getSeatNumber());
        stmt.bindString(8, route.getCarInfo());
        stmt.bindLong(9,0);
        stmt.bindLong(10, route.getPn());
        stmt.bindLong(11, route.getWt());
        stmt.bindLong(12, route.getSr());
        stmt.bindLong(13, route.getCzw());
        stmt.bindLong(14, route.getPt());
        stmt.bindLong(15, route.getSob());
        stmt.bindLong(16, route.getNd());

        long routeId = stmt.executeInsert();
        int counter = 1;

        List<String> nodesList = route.getNodesList();
        sql = "INSERT INTO ROUTES_NODES (ROUTE_ID, NODE_REF_ID, POSITION) VALUES (?, ?, ?)";
        SQLiteStatement nodeStmt = db.compileStatement(sql);

        for (String nodeId : nodesList){
            nodeStmt.bindLong(1, routeId);
            nodeStmt.bindString(2, nodeId);
            nodeStmt.bindLong(3, counter++);

            nodeStmt.execute();
            nodeStmt.clearBindings();

        }

    }

    public List<Route> searchRoutesForPassenger(String startNodeId, String endNodeId) {


        Node startNode = getNode(startNodeId);
        Node endNode = getNode(endNodeId);

        String sql = "SELECT  R.*, U.NAME, \n" +
                "(ABS(MIN(N.LAT - " + startNode.getLat() + ")) + ABS(MIN(N.LON - " + startNode.getLon() + "))) AS NearHome,\n" +
                "(ABS(MIN(N.LAT - " +  endNode.getLat() +")) + ABS(MIN(N.LON - " + endNode.getLon() + "))) AS NearDestination \n" +

                " FROM ROUTES R\n" +
                "JOIN ROUTES_NODES RN ON RN.ROUTE_ID = R.ID\n" +
                "JOIN NODES N ON RN.NODE_REF_ID = N.REF_ID\n" +
                "JOIN USERS U ON U.ID = R.USER_ID\n" +
                "GROUP BY R.NAME\n" +
                "ORDER BY NearDestination, (NearHome + NearDestination)\n";// +
                //"LIMIT 3";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        List<Route> routes = new ArrayList<>();

        while(cursor.moveToNext()){
            Route route = new Route(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getInt(9),
                    cursor.getInt(10),
                    cursor.getInt(11),
                    cursor.getInt(12),
                    cursor.getInt(13),
                    cursor.getInt(14),
                    cursor.getInt(15),
                    cursor.getInt(16));

            route.setUserName(cursor.getString(17));
            routes.add(route);
        }

        cursor.close();
        db.close();


        for (Route rt: routes){
            rt.setNodesList(getNodesListFromRoutesNodes(rt.getId()));
        }


        return routes;
    }

    private ArrayList<String> getNodesListFromRoutesNodes(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = " SELECT * FROM ROUTES_NODES WHERE ROUTE_ID = " + id + " ORDER BY POSITION";

        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<String> nodesList = new ArrayList<>();

        while(cursor.moveToNext()){
            nodesList.add(cursor.getString(1));
        }

        return nodesList;
    }
}
