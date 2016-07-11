package com.example.usamir.restaurantfinder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by usamir on 10.7.2016.
 */
public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
    GoogleMap mMap;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {
        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();
        JSONObject googlePlacesJson;

        // Parse json
        try {
            mMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("PlacesDisplayTask", "Error: " + e.toString());
        }
        return googlePlacesList;
    }

    /**
     * Place markers of nearby objects to map
     * @param list
     */
    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        mMap.clear();
        for (int i = 0; i < list.size(); i++) {

            // Get google place, and get lat / lang of the object founded
            HashMap<String, String> googlePlace = list.get(i);
            LatLng objLatLng = new LatLng(Double.parseDouble(googlePlace.get("lat")),
                    Double.parseDouble(googlePlace.get("lng")));

            // Create marker for find nearby objects
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(objLatLng)
                    .title(googlePlace.get("place_name") + " : " + googlePlace.get("vicinity"));
            mMap.addMarker(markerOptions);
        }
    }
}
