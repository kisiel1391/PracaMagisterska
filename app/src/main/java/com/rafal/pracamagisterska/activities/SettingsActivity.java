package com.rafal.pracamagisterska.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rafal.pracamagisterska.R;
import com.rafal.pracamagisterska.database.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    Button copy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        copy = (Button)findViewById(R.id.btnCopyDatabase);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDb = new DatabaseHelper(getApplicationContext());
                myDb.copyDatabase(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Pomyślnie skopoiowano bazę danych", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
