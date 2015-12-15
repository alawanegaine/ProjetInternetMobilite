package com.example.johan.essaiscarte;

import android.os.AsyncTask;
import android.test.mock.MockApplication;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dimitri on 11/12/2015.
 */
public class WebServiceConnection extends AsyncTask<LatLng, Void, JSONObject> {

    private WeakReference<MapsActivity> mActivity = null;
    private LatLng pointlocale;

    public WebServiceConnection(MapsActivity mapsActivity, LatLng latlong)
    {
        link(mapsActivity);
        pointlocale = latlong;
    }




    @Override
    protected JSONObject doInBackground(LatLng... params) {
        try {
            URL url = new URL("http://192.168.43.154:8080/RememberPlaces/GetListPoints");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setRequestMethod("POST");
            JSONObject latlong = new JSONObject();
            latlong.put("latitude",pointlocale.latitude);
            latlong.put("longitude", pointlocale.longitude);


            OutputStream OS = conn.getOutputStream();
            OS.write(latlong.toString().getBytes());
            OS.flush();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(reader);
                String read = null;
                StringBuffer sb = new StringBuffer();

                while ((read = br.readLine()) != null) {
                    sb.append(read);
                }


                JSONObject response = new JSONObject(sb.toString());

                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WebService", "Erreur de connexion");
        }


        return null;
    }

    private void link(MapsActivity pActivity) {
        mActivity = new WeakReference<MapsActivity>(pActivity);
    }
}