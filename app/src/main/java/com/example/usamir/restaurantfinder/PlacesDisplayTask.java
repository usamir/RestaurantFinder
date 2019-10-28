package com.example.usamir.restaurantfinder;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by usamir on 10.7.2016.
 */
public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
    private GoogleMap mMap;
    private final String TAG = "PlacesDisplayTask";

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
            Log.d(TAG, "Error: " + e.toString());
        }
        return googlePlacesList;
    }

    /**
     * Place markers of nearby objects to map
     * @param list list where to place markers
     */
    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        mMap.clear();
        for (int i = 0; i < list.size(); i++) {

            // Get google place, and get lat / lang of the object founded
            HashMap<String, String> place = list.get(i);
            LatLng objLatLng = new LatLng(Double.parseDouble(place.get("lat")),
                    Double.parseDouble(place.get("lng")));

            String openNow = place.get("open_now");
            String working = openNow;
            if (openNow.contains("true")) {
                working = " open";
            } else if (openNow.contains("false")) {
                working = " closed";
            }

            // Create marker for find nearby objects
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(objLatLng)
                    .title(place.get("place_name") + " | Working: " + working + " | Rating: " + place.get("rating"))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restaurant_building));
            mMap.addMarker(markerOptions);

        }
    }
}
