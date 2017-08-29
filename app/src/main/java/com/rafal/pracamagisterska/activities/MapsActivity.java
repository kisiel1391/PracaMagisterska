package com.rafal.pracamagisterska.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rafal.pracamagisterska.R;
import com.rafal.pracamagisterska.database.DatabaseHelper;
import com.rafal.pracamagisterska.objects.Node;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLngBounds bounds;
    private Button zoom;
    ArrayList<String> path;
    String startNodeId, endNodeId;
    private static DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        path = getIntent().getStringArrayListExtra("Path");
        startNodeId = getIntent().getStringExtra("startNodeId");
        endNodeId = getIntent().getStringExtra("endNodeId");


        zoom = (Button)findViewById(R.id.btnZoom);
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myDb =  new DatabaseHelper(this);

        System.out.println(("Lista nodów length from maps: " + path.size()));
        System.out.println(startNodeId);
        System.out.println(endNodeId);

        PolylineOptions myLine = new PolylineOptions();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(String nodeId: path){
            Node myNode = myDb.getNode(nodeId);
            myLine.add( new LatLng(myNode.getLat(),
                    myNode.getLon()));

            builder.include(new LatLng(myNode.getLat(),
                    myNode.getLon()));
        }

        Node startNode = myDb.getNode(startNodeId);
        Node endNode = myDb.getNode(endNodeId);

        builder.include(new LatLng(startNode.getLat(), startNode.getLon()));
        builder.include(new LatLng(endNode.getLat(), endNode.getLon()));

        bounds = builder.build();

        mMap.addMarker(new MarkerOptions().position(new LatLng(startNode.getLat(), startNode.getLon())).title("Start"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(endNode.getLat(), endNode.getLon())).title("Cel"));
        mMap.addPolyline(myLine);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

    }

    @Override
    public void onBackPressed() {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        this.finish();
    }
}
