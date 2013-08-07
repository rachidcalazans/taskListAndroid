package br.com.rcalazans.tasklist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import br.com.rcalazans.tasklist.model.SimpleGeofence;
import br.com.rcalazans.tasklist.model.SimpleGeofenceStore;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

public class MainActivity extends FragmentActivity implements ConnectionCallbacks, 
		OnConnectionFailedListener, OnAddGeofencesResultListener {

	 // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
    /*
     * Use to set an expiration time for a geofence. After this amount
     * of time Location Services will stop tracking the geofence.
     */
    private static final long SECONDS_PER_HOUR 			   = 60;
    private static final long MILLISECONDS_PER_SECOND      = 1000;
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_TIME     = GEOFENCE_EXPIRATION_IN_HOURS *
            SECONDS_PER_HOUR * MILLISECONDS_PER_SECOND;
    
    private EditText mLatitude1;
    private EditText mLongitude1;
    private EditText mRadius1;
    private EditText mLatitude2;
    private EditText mLongitude2;
    private EditText mRadius2;
    
    private SimpleGeofence mUIGeofence1;
    private SimpleGeofence mUIGeofence2;
    
    List<Geofence> mGeofenceList;
    
    private SimpleGeofenceStore mGeofenceStorage;
    
 // Holds the location client
    private LocationClient mLocationClient;
    // Stores the PendingIntent used to request geofence monitoring
    private PendingIntent mGeofenceRequestIntent;
    // Defines the allowable request types.
    public enum REQUEST_TYPE {ADD, REMOVE_INTENT}
    private REQUEST_TYPE mRequestType;
    // Flag that indicates if a request is underway.
    private boolean mInProgress;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 // Start with the request flag set to false
        mInProgress = false;
		
        mLatitude1  = (EditText) findViewById(R.id.lat1);
        mLongitude1 = (EditText) findViewById(R.id.long1);
        mRadius1    = (EditText) findViewById(R.id.rad1);
        
        mLatitude2  = (EditText) findViewById(R.id.lat2);
        mLongitude2 = (EditText) findViewById(R.id.long2);
        mRadius2    = (EditText) findViewById(R.id.rad2);
        
		if (servicesConnected()) {
			// Instantiate a new geofence storage area
			mGeofenceStorage = new SimpleGeofenceStore(this);

	        // Instantiate the current List of geofences
			mGeofenceList = new ArrayList<Geofence>();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		Log.d("MainActResult",
                "Entrou onActivityResult");
		Log.d("RequestCode",
                ""+requestCode);
		  // Decide what to do based on the original request code
        switch (requestCode) {
            
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
            
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
            
                    break;
                }
            
        }
	}
	
	private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Geofence Detection",
                    "Google Play services is available.");
            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
        	Log.d("Error_Google_Service", GooglePlayServicesUtil.getErrorString(resultCode));
        	return false;
        }
    }
	
	/**
     * Get the geofence parameters for each geofence from the UI
     * and add them to a List.
     */
    public void createGeofences() {
    	Log.d("createGeofences", "ENTRANDO CREATE_GEO");
    	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double mLongitude = location.getLongitude();
		double mLatitude = location.getLatitude();
		float mRadius = 1;
		
		Log.d("createGeofences", "DEPOIS DE PEGAR LOCATIONS");
		
//		Toast.makeText(this,
//				"Longitude " + mLongitude + " - Latitude " + mLatitude,
//				Toast.LENGTH_SHORT).show();

		Log.d("Locations LAT", "Longitude " + mLongitude + " - Latitude " + mLatitude);
		
		 mUIGeofence1 = new SimpleGeofence(
	                "1",
	                mLongitude,
	                mLatitude,
	                mRadius,
	                Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER
					| Geofence.GEOFENCE_TRANSITION_EXIT);
		
    	/*
         * Create an internal object to store the data. Set its
         * ID to "1". This is a "flattened" object that contains
         * a set of strings
         */
//        mUIGeofence1 = new SimpleGeofence(
//                "1",
//                Double.valueOf(mLatitude1.getText().toString()),
//                Double.valueOf(mLongitude1.getText().toString()),
//                Float.valueOf(mRadius1.getText().toString()),
//                GEOFENCE_EXPIRATION_TIME,
//                // This geofence records only entry transitions
//                Geofence.GEOFENCE_TRANSITION_ENTER);
        // Store this flat version
        mGeofenceStorage.setGeofence("1", mUIGeofence1);
        
        // Create another internal object. Set its ID to "2"
//        mUIGeofence2 = new SimpleGeofence(
//                "2",
//                Double.valueOf(mLatitude2.getText().toString()),
//                Double.valueOf(mLongitude2.getText().toString()),
//                Float.valueOf(mRadius2.getText().toString()),
//                GEOFENCE_EXPIRATION_TIME,
//                // This geofence records both entry and exit transitions
//                Geofence.GEOFENCE_TRANSITION_ENTER |
//                Geofence.GEOFENCE_TRANSITION_EXIT);
        
        Log.d("createGeofences", "Antes de add no list");
        // Store this flat version
//        mGeofenceStorage.setGeofence("2", mUIGeofence2);
        mGeofenceList.add(mUIGeofence1.toGeofence());
//        mGeofenceList.add(mUIGeofence2.toGeofence());
//        addGeofences();
        Log.d("createGeofences", "Depois de add no list");
    }
    
    /*
     * Create a PendingIntent that triggers an IntentService in your
     * app when a geofence transition occurs.
     */
    private PendingIntent getTransitionPendingIntent() {
        // Create an explicit Intent
        Intent intent = new Intent(this,
                ReceiveTransitionsIntentService.class);
        /*
         * Return the PendingIntent
         */
        return PendingIntent.getService(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

	@Override
	public void onConnected(Bundle connectionHint) {
		switch (mRequestType) {
        case ADD :
            // Get the PendingIntent for the request
        	mGeofenceRequestIntent =
                    getTransitionPendingIntent();
            // Send a request to add the current geofences
            mLocationClient.addGeofences(mGeofenceList, mGeofenceRequestIntent, this);
            break;
        case REMOVE_INTENT :
//            mLocationClient.removeGeofences(mGeofenceRequestIntent, (OnRemoveGeofencesResultListener) this);
            break;
		}
		
	}

	@Override
	public void onDisconnected() {
		// Turn off the request flag
        mInProgress = false;
        // Destroy the current location client
        mLocationClient = null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Turn off the request flag
        mInProgress = false;
        /*
         * If the error has a resolution, start a Google Play services
         * activity to resolve it.
         */
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        // If no resolution is available, display an error dialog
        } else {
            // Get the error code
            int errorCode = connectionResult.getErrorCode();
            Log.d("onConnectionFailed", "Error: "+errorCode);
        }
		
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		// If adding the geofences was successful
        if (LocationStatusCodes.SUCCESS == statusCode) {
            /*
             * Handle successful addition of geofences here.
             * You can send out a broadcast intent or update the UI.
             * geofences into the Intent's extended data.
             */
        	Log.d("onAddGeofencesResult", "Success: "+geofenceRequestIds);
        } else {
        // If adding the geofences failed
            /*
             * Report errors here.
             * You can log the error using Log.e() or update
             * the UI.
             */
        	Log.d("onAddGeofencesResult", "Error");
        }
        // Turn off the in progress flag and disconnect the client
        mInProgress = false;
        mLocationClient.disconnect();		
	}
	
	/**
     * Start a request for geofence monitoring by calling
     * LocationClient.connect().
     */
    public void addGeofences() {
        // Start a request to add geofences
        mRequestType = REQUEST_TYPE.ADD;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the proper request
         * can be restarted.
         */
        if (!servicesConnected()) {
            return;
        }
        /*
         * Create a new location client object. Since the current
         * activity class implements ConnectionCallbacks and
         * OnConnectionFailedListener, pass the current activity object
         * as the listener for both parameters
         */
        mLocationClient = new LocationClient(this, this, this);
        // If a request is not already underway
        if (!mInProgress) {
            // Indicate that a request is underway
            mInProgress = true;
            // Request a connection from the client to Location Services
            mLocationClient.connect();
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
    }
    
    public void removeGeofences(PendingIntent requestIntent) {
        // Record the type of removal request
        mRequestType = REQUEST_TYPE.REMOVE_INTENT;
        /*
         * Test for Google Play services after setting the request type.
         * If Google Play services isn't present, the request can be
         * restarted.
         */
        if (!servicesConnected()) {
            return;
        }
        // Store the PendingIntent
        mGeofenceRequestIntent = requestIntent;
        /*
         * Create a new location client object. Since the current
         * activity class implements ConnectionCallbacks and
         * OnConnectionFailedListener, pass the current activity object
         * as the listener for both parameters
         */
        mLocationClient = new LocationClient(this, this, this);
        // If a request is not already underway
        if (!mInProgress) {
            // Indicate that a request is underway
            mInProgress = true;
            // Request a connection from the client to Location Services
            mLocationClient.connect();
        } else {
            /*
             * A request is already underway. You can handle
             * this situation by disconnecting the client,
             * re-setting the flag, and then re-trying the
             * request.
             */
        }
    }
    
    public void add(View v) {
    	Log.d("BT ADD PRESSED", "BT PRESSED");
    	Toast.makeText(this, "Btn Add pressed",
				Toast.LENGTH_SHORT).show();
    	createGeofences();
    	Log.d("ADD", "Antes Add_GEOFENCE");
    	addGeofences();
    }
}
