package com.rafal.pracamagisterska.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.rafal.pracamagisterska.R;
import com.rafal.pracamagisterska.database.DatabaseHelper;
import com.rafal.pracamagisterska.objects.Route;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FindRoutesActivity extends AppCompatActivity {

    private AutoCompleteTextView actvStartNodeId, actvEndNodeId;
    private Button search;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_routes);
        myDb = new DatabaseHelper(this);

        actvStartNodeId = (AutoCompleteTextView)findViewById(R.id.actvStartNodeIdToSearch);
        actvEndNodeId = (AutoCompleteTextView)findViewById(R.id.actvEndNodeIdToSearch);

        ArrayList<String> nodeList = myDb.getNodesForAutoCompleteTextView();
        System.out.println("nodes list size: " + nodeList.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, nodeList);

        actvStartNodeId.setAdapter(adapter);
        actvStartNodeId.setThreshold(1);

        actvEndNodeId.setAdapter(adapter);
        actvEndNodeId.setThreshold(1);

        search = (Button)findViewById(R.id.btnSearch);
        searchRoutes();

    }

    private void searchRoutes() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp, startNodeId, endNodeId;

                temp = actvStartNodeId.getText().toString();
                startNodeId = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

                temp = actvEndNodeId.getText().toString();
                endNodeId = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

                if(startNodeId.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nie wskazano punktu początkowego", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(endNodeId.equals("")) {
                    Toast.makeText(getApplicationContext(), "Nie wskazano punktu końcowego", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Route> routes = myDb.searchRoutesForPassenger(startNodeId, endNodeId);


                Intent intent = new Intent(getApplicationContext(), ShowRoutesActivity.class);
                intent.putExtra("Routes", (Serializable) routes);
                intent.putExtra("startNodeId", startNodeId);
                intent.putExtra("endNodeId", endNodeId);
                startActivity(intent);
                finish();

            }
        });
    }
}
