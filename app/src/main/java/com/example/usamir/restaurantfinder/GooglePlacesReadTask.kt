package com.example.usamir.restaurantfinder

import android.os.AsyncTask
import android.util.Log

import com.google.android.gms.maps.GoogleMap

/**
 * Created by usamir on 10.7.2016.
 */
class GooglePlacesReadTask : AsyncTask<Any, Int, String>() {
    private var mMap: GoogleMap? = null

    override fun doInBackground(vararg inputObj: Any): String {
        var data = ""

        // Create task
        try {
            mMap = inputObj[0] as GoogleMap
            val googlePlacesUrl = inputObj[1] as String
            val http = Http()
            data = http.read(googlePlacesUrl)
        } catch (e: Exception) {
            Log.d("GooglePlacesReadTask", "Error: $e")
        }

        return data
    }

    /**
     * Run task to display objects
     * @param result
     */
    override fun onPostExecute(result: String) {
        val placesDisplayTask = PlacesDisplayTask()
        val toPass = arrayOfNulls<Any>(2)
        toPass[0] = mMap
        toPass[1] = result
        placesDisplayTask.execute(*toPass)
    }
}
