package com.example.usamir.restaurantfinder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by usamir on 10.7.2016.
 */
public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... inputObj) {
        String data = "";

        // Create task
        try {
            mMap = (GoogleMap) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            Http http = new Http();
            data = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", "Error: " + e.toString());
        }
        return data;
    }

    /**
     * Run task to display objects
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = result;
        placesDisplayTask.execute(toPass);
    }
}
