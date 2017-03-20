package com.example.usamir.restaurantfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by usamir on 10.7.2016.
 */
public class Places {
    final String TAG = "Places";
    public List<HashMap<String, String>> parse(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d(TAG, "Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    /**
     * Get places from json Array
     * @param jsonArray
     * @return
     */
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> placeMap = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "Error: " + e.getLocalizedMessage());
            }
        }
        return placesList;
    }

    /**
     * Get place information out of json objects
     * @param googlePlaceJson
     * @return
     */
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> map = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String openNow = "N\\A";
        String rating = "N\\A";

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }

            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            if (!googlePlaceJson.isNull("opening_hours")) {
                openNow = googlePlaceJson.getJSONObject("opening_hours").getString("open_now");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            if (!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getString("rating");
            }

            map.put("place_name", placeName);
            map.put("vicinity", vicinity);
            map.put("lat", latitude);
            map.put("lng", longitude);
            map.put("reference", reference);
            map.put("open_now", openNow);
            map.put("rating", rating);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Error: " + e.getLocalizedMessage());
        }
        return map;
    }
}
