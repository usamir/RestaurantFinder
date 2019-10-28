package com.example.usamir.restaurantfinder

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

/**
 * Created by usamir on 20.3.2017.
 */


object Utility {
    private var permissionRequest: Int = 0
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    private val MY_PERMISSIONS_REQUEST_CAMERA = 124
    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 125
    private val MY_PERMISSIONs_REQUEST_ACCESS_FINE_LOCATION = 126
    private val MY_PERMISSIONs_REQUEST_ACCESS_COARSE_LOCATION = 127
    private val TAG = "Utility Class"

    /**
     * This method will check permission at runtime for
     * Marshmallow & greater than Marshmallow version.
     *
     * @param context from which activity
     * @return If current API version is less than Marshmallow,
     * then checkPermission() will return true, which means permission
     * is granted (in Manifest file, no support for runtime permission).
     * If current API version is Marshmallow or greater, and if permission
     * is already granted then the method returns true.
     * Otherwise, the method returns false and will show a dialog box to a user
     * with allow or deny options.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    internal fun checkPermission(context: Context, exactPermission: String): Boolean {
        when (exactPermission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> permissionRequest = MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            Manifest.permission.CAMERA -> permissionRequest = MY_PERMISSIONS_REQUEST_CAMERA
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> permissionRequest = MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            Manifest.permission.ACCESS_FINE_LOCATION -> permissionRequest = MY_PERMISSIONs_REQUEST_ACCESS_FINE_LOCATION
            Manifest.permission.ACCESS_COARSE_LOCATION -> permissionRequest = MY_PERMISSIONs_REQUEST_ACCESS_COARSE_LOCATION
            else -> permissionRequest = 0
        }

        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, exactPermission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, exactPermission)) {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(android.R.string.yes) { _, _ -> ActivityCompat.requestPermissions(context, arrayOf(exactPermission), permissionRequest) }
                    val alert = alertBuilder.create()
                    alert.show()

                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(exactPermission), permissionRequest)
                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }
}
