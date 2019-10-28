package com.example.usamir.restaurantfinder

import android.os.AsyncTask
import android.util.Log

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import org.json.JSONObject

import java.util.HashMap

/**
 * Created by usamir on 10.7.2016.
 */
class PlacesDisplayTask : AsyncTask<Any, Int, List<HashMap<String, String>>>() {
    private var mMap: GoogleMap? = null
    private val TAG = "PlacesDisplayTask"

    override fun doInBackground(vararg inputObj: Any): List<HashMap<String, String>>? {
        var googlePlacesList: List<HashMap<String, String>>? = null
        val placeJsonParser = Places()
        val googlePlacesJson: JSONObject

        // Parse json
        try {
            mMap = inputObj[0] as GoogleMap
            googlePlacesJson = JSONObject(inputObj[1] as String)
            googlePlacesList = placeJsonParser.parse(googlePlacesJson)
        } catch (e: Exception) {
            Log.d(TAG, "Error: $e")
        }

        return googlePlacesList
    }

    /**
     * Place markers of nearby objects to map
     * @param list list where to place markers
     */
    override fun onPostExecute(list: List<HashMap<String, String>>) {
        mMap!!.clear()
        for (i in list.indices) {

            // Get google place, and get lat / lang of the object founded
            val place = list[i]
            val objLatLng = LatLng(java.lang.Double.parseDouble(place["lat"]!!),
                    java.lang.Double.parseDouble(place["lng"]!!))

            val openNow = place["open_now"]
            var working = openNow
            if (openNow!!.contains("true")) {
                working = " open"
            } else if (openNow.contains("false")) {
                working = " closed"
            }

            // Create marker for find nearby objects
            val markerOptions = MarkerOptions()
                    .position(objLatLng)
                    .title(place["place_name"] + " | Working: " + working + " | Rating: " + place["rating"])
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.restaurant_building))
            mMap!!.addMarker(markerOptions)

        }
    }
}
