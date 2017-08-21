package com.rafal.pracamagisterska.downloader;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by Rafal on 2017-04-13.
 */

public class DownloadHelper {

    //public final String API_URL = "http://api.openstreetmap.fr/oapi/interpreter";
    public final String API_URL = "http://overpass-api.de/api/interpreter";

    //Dla MaszewoCity
    //public final String OVERPASS_QUERY = "?data=%5Bout%3Axml%5D%5Btimeout%3A100%5D%5Bmaxsize%3A2000000000%5D%3B%0Aarea%283602777174%29-%3E.searchArea%3B%0A%28%0A%20%20way%5B%22highway%22%3D%22motorway%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22motorway_link%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22trunk%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22trunk_link%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22primary%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22primary_link%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22secondary%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22tertiary%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22residential%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22road%22%5D%28area.searchArea%29%3B%0A%29%3B%0A%28._%3B%3E%3B%29%3B%0Aout%20body%3B%0A%3E%3B";

    //Dla Szczecina
    public final String OVERPASS_QUERY = "?data=%5Bout%3Axml%5D%5Btimeout%3A100%5D%5Bmaxsize%3A2000000000%5D%3B%0Aarea%283600342935%29-%3E.searchArea%3B%0A%28%0A%20%20way%5B%22highway%22%3D%22motorway%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22motorway_link%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22trunk%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22trunk_link%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22primary%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22primary_link%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22secondary%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22tertiary%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22residential%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22road%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22unclassified%22%5D%28area.searchArea%29%3B%0A%20%20way%5B%22highway%22%3D%22living_street%22%5D%28area.searchArea%29%3B%0A%29%3B%0A%28._%3B%3E%3B%29%3B%0Aout%20body%3B%0A%3E%3B";



    public void downloadData(){

        URL url;

        try {



            url = new URL(API_URL + OVERPASS_QUERY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d("OsmOverpassGetter", "Server response code: " + conn.getResponseCode());
            Log.d("OsmOverpassGetter", "Server response message: " + conn.getHeaderField("Error"));
            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            File myFile = new File(getExternalStorageDirectory() + "/road.xml");
            System.out.println("Downoload file to " + myFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(myFile);

            while((line = br.readLine())!= null){
                sb.append(line + "\n");
                fos.write(sb.toString().getBytes());
                fos.flush();
                sb = new StringBuilder();
            }

            br.close();
            fos.close();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
