package com.example.usamir.restaurantfinder;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by usamir on 20.3.2017.
 */


public class Utility {
    static int permissionRequest;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 125;
    private static final int MY_PERMISSIONs_REQUEST_ACCESS_FINE_LOCATION = 126;
    private static final int MY_PERMISSIONs_REQUEST_ACCESS_COARSE_LOCATION = 127;
    private final static String TAG = "Utility Class";

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
    static boolean checkPermission(final Context context, final String exactPermission) {
        switch (exactPermission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                permissionRequest = MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
                break;
            case Manifest.permission.CAMERA:
                permissionRequest = MY_PERMISSIONS_REQUEST_CAMERA;
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                permissionRequest = MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                permissionRequest = MY_PERMISSIONs_REQUEST_ACCESS_FINE_LOCATION;
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                permissionRequest = MY_PERMISSIONs_REQUEST_ACCESS_COARSE_LOCATION;
                break;
            default:
                permissionRequest = 0;
                break;
        }

        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, exactPermission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, exactPermission)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{exactPermission}, permissionRequest);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{exactPermission}, permissionRequest);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
