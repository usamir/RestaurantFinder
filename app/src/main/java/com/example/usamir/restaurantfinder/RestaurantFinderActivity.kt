@file:Suppress("DEPRECATION")

package com.example.usamir.restaurantfinder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment


class RestaurantFinderActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Define google map
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null

    companion object {

        //Define a request code to send to Google Play services
        private const val CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000
        // Log support
        private const val TAG = "RestaurantFinder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_finder)


        // Check for permission for location
        if (Utility.checkPermission(this@RestaurantFinderActivity, Manifest.permission.ACCESS_FINE_LOCATION) && Utility.checkPermission(this@RestaurantFinderActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Check is GPS enabled on device
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser()
            }

            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null) {
                mGoogleApiClient = GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .enableAutoManage(this, this)
                        .build()
            }

            // Create the LocationRequest object
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval((5 * 1000).toLong())         // 5 seconds, in milliseconds
                    .setFastestInterval(1000)      // 1 second, in milliseconds
                    .setSmallestDisplacement(20f)  // smallest displacement to 20 meters

            // Create map fragment
            val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

        }
    }

    /**
     * GPS Alert Dialog
     */
    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS"
                ) { _, _ ->
                    val callGPSSettingIntent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(callGPSSettingIntent)
                }
        alertDialogBuilder.setNegativeButton("Cancel"
        ) { dialog, _ -> dialog.cancel() }
        val alert = alertDialogBuilder.create()
        alert.show()
    }


    /**
     * Add listeners or move the camera.
     */
    override fun onMapReady(map: GoogleMap) {

        mMap = map
        // We will provide our own zoom controls.
        //mMap.getUiSettings().setZoomControlsEnabled(false);

        // Set that my current location is enabled and visible as "blue dot" on map
        mMap!!.isMyLocationEnabled = true

    }

    private fun startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest,
                this)
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        //Disconnect from API onPause()
        if (mGoogleApiClient!!.isConnected) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mGoogleApiClient!!.isConnected) {
            startLocationUpdates()
        }
    }

    /***
     * Get current location latitude and longitude, and call changeLocation action
     * @param bundle
     */
    override fun onConnected(bundle: Bundle?) {
        val location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (location == null) {
            startLocationUpdates()

        } else {
            changeLocation(location)
        }
    }

    /**
     * Change current location, and draw marker to place where we are currently
     * @param location
     */
    private fun changeLocation(location: Location) {
        //If everything went fine lets get latitude and longitude
        val currentLatitude = location.latitude
        val currentLongitude = location.longitude

        // Move camera to current position
        val currentLocation = LatLng(currentLatitude, currentLongitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))


        // Needed options to create REST API
        val PROXIMITY_RADIUS = 2000 // distance in meters within return place results
        val GOOGLE_API_KEY = "AIzaSyCjf8oqcfm6g0BCwEcMmGljh5orZOqCGdM"

        // Create REST API call to get all restaurants in nearby area
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
        url.append("location=$currentLatitude,$currentLongitude")
        url.append("&radius=$PROXIMITY_RADIUS")
        url.append("&types=" + "restaurant")
        url.append("&sensor=true")
        url.append("&key=$GOOGLE_API_KEY")

        Log.i(TAG, "URL: $url")
        // Call function to create task
        val googlePlacesReadTask = GooglePlacesReadTask()
        val urlObj = arrayOfNulls<Any>(2)
        urlObj[0] = mMap
        urlObj[1] = url.toString()
        googlePlacesReadTask.execute(*urlObj)

    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection is suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onConnectionFailed(result: ConnectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (result.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST)
                /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (e: IntentSender.SendIntentException) {
                // Log the error
                e.printStackTrace()
            }

        } else {
            /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e(TAG, "Location services connection failed with code " + result.errorCode)
        }
    }

    override fun onLocationChanged(location: Location) {
        changeLocation(location)
    }
}
