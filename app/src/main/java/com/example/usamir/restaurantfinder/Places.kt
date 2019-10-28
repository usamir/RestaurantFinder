package com.example.usamir.restaurantfinder

import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by usamir on 10.7.2016.
 */
class Places {
    internal val TAG = "Places"
    fun parse(jsonObject: JSONObject): List<HashMap<String, String>> {
        var jsonArray: JSONArray? = null
        try {
            jsonArray = jsonObject.getJSONArray("results")
        } catch (e: JSONException) {
            Log.d(TAG, "Error: " + e.localizedMessage!!)
            e.printStackTrace()
        }

        return getPlaces(jsonArray!!)
    }

    /**
     * Get places from json Array
     * @param jsonArray
     * @return
     */
    private fun getPlaces(jsonArray: JSONArray): List<HashMap<String, String>> {
        val placesCount = jsonArray.length()
        val placesList = ArrayList<HashMap<String, String>>()
        var placeMap: HashMap<String, String>?

        for (i in 0 until placesCount) {
            try {
                placeMap = getPlace(jsonArray.get(i) as JSONObject)
                placesList.add(placeMap)

            } catch (e: JSONException) {
                e.printStackTrace()
                Log.d(TAG, "Error: " + e.localizedMessage!!)
            }

        }
        return placesList
    }

    /**
     * Get place information out of json objects
     * @param googlePlaceJson
     * @return
     */
    private fun getPlace(googlePlaceJson: JSONObject): HashMap<String, String> {
        val map = HashMap<String, String>()
        var placeName = "-NA-"
        var vicinity = "-NA-"
        var openNow = "N\\A"
        var rating = "N\\A"

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name")
            }

            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity")
            }

            if (!googlePlaceJson.isNull("opening_hours")) {
                openNow = googlePlaceJson.getJSONObject("opening_hours").getString("open_now")
            }

            val latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat")
            val longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng")
            val reference = googlePlaceJson.getString("reference")
            if (!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getString("rating")
            }

            map["place_name"] = placeName
            map["vicinity"] = vicinity
            map["lat"] = latitude
            map["lng"] = longitude
            map["reference"] = reference
            map["open_now"] = openNow
            map["rating"] = rating

        } catch (e: JSONException) {
            e.printStackTrace()
            Log.d(TAG, "Error: " + e.localizedMessage!!)
        }

        return map
    }
}
