package com.rafal.pracamagisterska.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rafal.pracamagisterska.R;
import com.rafal.pracamagisterska.algorithm.AStar;
import com.rafal.pracamagisterska.algorithm.GraphAlgorithm;
import com.rafal.pracamagisterska.database.DatabaseHelper;
import com.rafal.pracamagisterska.objects.Path;
import com.rafal.pracamagisterska.objects.Route;

import java.util.ArrayList;

public class AddRouteActivity extends AppCompatActivity {

    private AutoCompleteTextView actvStartNodeId, actvEndNodeId;
    private Button registerRoute;
    private EditText txtRouteName, txtCarDescription, txtPrice, txtSeatsNumber;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        myDb = new DatabaseHelper(this);

        actvStartNodeId = (AutoCompleteTextView)findViewById(R.id.actvStratNodeId);
        actvEndNodeId = (AutoCompleteTextView)findViewById(R.id.actvEndNodeIdToSearch);

        ArrayList<String> nodeList = myDb.getNodesForAutoCompleteTextView();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, nodeList);

        actvStartNodeId.setAdapter(adapter);
        actvStartNodeId.setThreshold(1);

        actvEndNodeId.setAdapter(adapter);
        actvEndNodeId.setThreshold(1);

        registerRoute = (Button)findViewById(R.id.btnAddPath);
        addPath();

        txtRouteName = (EditText) findViewById(R.id.txtRouteName);
        txtCarDescription = (EditText)findViewById(R.id.txtCarDescription);
        txtPrice = (EditText)findViewById(R.id.item_price);
        txtSeatsNumber = (EditText)findViewById(R.id.item_seats_number);


    }

    private void addPath() {

        registerRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp, startNodeId, endNodeId, routeName, carDescription;
                int seatsNumber;
                Double price;

                temp = actvStartNodeId.getText().toString();
                startNodeId = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

                temp = actvEndNodeId.getText().toString();
                endNodeId = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

                routeName = txtRouteName.getText().toString();
                carDescription = txtCarDescription.getText().toString();
                if(!txtPrice.getText().toString().equals("")) price = Double.parseDouble(txtPrice.getText().toString());
                if(!txtSeatsNumber.getText().toString().equals("")) seatsNumber = Integer.parseInt(txtSeatsNumber.getText().toString());

                if(startNodeId.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nie wskazano punktu początkowego", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(endNodeId.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nie wskazano punktu końcowego", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(routeName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nie podano nazwy trasy", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(carDescription.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nie podano opisu samochodu", Toast.LENGTH_SHORT).show();
                    return;
                }


                AStar astar = new AStar(getApplicationContext());
                Path path = astar.findPath(startNodeId,endNodeId,
                        GraphAlgorithm.DISTANCE,
                        GraphAlgorithm.NONE,
                        GraphAlgorithm.EUCLIDEAN_HEURISTIC);

                Route route = new Route(0, //id
                        txtRouteName.getText().toString(), //name
                        1,//userId
                        Double.parseDouble(txtPrice.getText().toString()),//cost,
                        1,//period,
                        "","",//String dateFrom, String dateTo,
                        Integer.parseInt(txtSeatsNumber.getText().toString()), //int seatNumber,
                        txtCarDescription.getText().toString(), //String carInfo,
                        0,//int status,
                        1,1,1,1,1,0,0//int pn, int wt, int sr,int czw,int pt,int sob,int nd)
                        );

                route.setNodesList(path.getPathAsList());

                myDb.addRoute(route);

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putStringArrayListExtra("Path", path.getPathAsList());
                intent.putExtra("startNodeId", startNodeId);
                intent.putExtra("endNodeId", endNodeId);
                startActivity(intent);

                finish();

            }
        });
    }
}
