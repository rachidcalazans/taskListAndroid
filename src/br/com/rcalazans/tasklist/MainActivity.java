package br.com.rcalazans.tasklist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import br.com.rcalazans.tasklist.dao.GeofenceDao;
import br.com.rcalazans.tasklist.dao.TaskDao;
import br.com.rcalazans.tasklist.fragment.ListTasksFragment;
import br.com.rcalazans.tasklist.fragment.ListTasksFragment.TaskSelectListener;
import br.com.rcalazans.tasklist.fragment.TaskFragment;
import br.com.rcalazans.tasklist.fragment.TaskFragment.DetailTaskListerner;
import br.com.rcalazans.tasklist.model.GeofenceTask;
import br.com.rcalazans.tasklist.model.Task;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationStatusCodes;

public class MainActivity extends SherlockFragmentActivity implements ConnectionCallbacks, 
		OnConnectionFailedListener, OnAddGeofencesResultListener, TaskSelectListener, TabListener,
		DetailTaskListerner{

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
    
    FrameLayout rootDetalhe = null;
    
    private List<Geofence> mGeofenceList;
    private GeofenceDao daoGeofence;
    private GeofenceTask geofenceTask;
    private TaskDao daoTask;
    
 // Holds the location client
    private LocationClient mLocationClient;
    // Stores the PendingIntent used to request geofence monitoring
    private PendingIntent mGeofenceRequestIntent;
    // Defines the allowable request types.
    public enum REQUEST_TYPE {ADD, REMOVE_INTENT}
    private REQUEST_TYPE mRequestType;
    // Flag that indicates if a request is underway.
    private boolean mInProgress;
    
    
    private ListTasksFragment listCompleted;
    private ListTasksFragment listUnCompleted;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		setContentView(R.layout.activity_main);
		
		daoGeofence   = new GeofenceDao(this);
        daoTask       = new TaskDao(this);
        mGeofenceList = new ArrayList<Geofence>();
		
        Log.d("rachid", "tasks size list: "+daoTask.listTasks().size());
        Log.d("rachid", "geofences size list: "+daoGeofence.listGeofenceTasks().size());
        
        List<GeofenceTask> list = daoGeofence.listGeofenceTasks();
		
		for (GeofenceTask g : list) {
			mGeofenceList.add(g.toGeofence());
		}

    	addGeofences();
        
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		
		listUnCompleted = (ListTasksFragment) getSupportFragmentManager().findFragmentByTag("listUnCompleted");

		if (listUnCompleted == null) {
			listUnCompleted = ListTasksFragment.createNewInstance(0);
			fragmentTransaction = fragmentTransaction.add(R.id.lista, listUnCompleted, "listUnCompleted").hide(listUnCompleted);
		}
		
		listCompleted = (ListTasksFragment) getSupportFragmentManager().findFragmentByTag("listCompleted");
		
		if (listCompleted == null) {
			listCompleted = ListTasksFragment.createNewInstance(1);
			fragmentTransaction = fragmentTransaction.add(R.id.lista, listCompleted, "listCompleted").hide(listCompleted);
		}
		
		rootDetalhe = (FrameLayout) findViewById(R.id.detail);
		
		listUnCompleted.setListener(this);
		listCompleted.setListener(this);
		
		fragmentTransaction.commit();
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab aba1 = actionBar.newTab();
		aba1.setText(R.string.uncompleted);
		aba1.setTabListener(this);
		actionBar.addTab(aba1);

		Tab aba2 = actionBar.newTab();
		aba2.setText(R.string.completed);
		aba2.setTabListener(this);
		actionBar.addTab(aba2);
		
		 // Start with the request flag set to false
        mInProgress = false;
        
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.tasks, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_novo) {
			onClick(null);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private boolean isTablet() {
		return rootDetalhe != null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		List<GeofenceTask> list = daoGeofence.listGeofenceTasks();
		
		for (GeofenceTask g : list) {
			mGeofenceList.add(g.toGeofence());
		}

    	addGeofences();
		
		Log.d("rachid", "Entrou onActivityResult");

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
                
            case 1:
            	
            	if (resultCode == Activity.RESULT_OK) {
            		listCompleted.refreshListTasks(1);
            		listUnCompleted.refreshListTasks(0);
            	}
                
        }
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
        	Log.d("rachid", "onAddGeofencesResult: Success: "+geofenceRequestIds);
        } else {
        // If adding the geofences failed
            /*
             * Report errors here.
             * You can log the error using Log.e() or update
             * the UI.
             */
        	Log.d("rachid", "onAddGeofencesResult: Error");
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
    
    public void addGeo(View v) {
    	Toast.makeText(this, "Add GEO",
    			Toast.LENGTH_SHORT).show();
    	
		List<GeofenceTask> list = daoGeofence.listGeofenceTasks();
		
		for (GeofenceTask g : list) {
			mGeofenceList.add(g.toGeofence());
		}

    	addGeofences();
    }
    
    /**
     * Checa o status do GooglePlayService
     * @return true. Caso esteja dispon’vel
     */
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
    
    public class ResponseReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "A";

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "COCK SLAM - received response",
					Toast.LENGTH_SHORT).show();
		}
	}

    // Click no item da lista
	@Override
	public void onClick(Task task) {
		
		if (isTablet()) {
			TaskFragment fragment = TaskFragment.createNewInstance(task);
			fragment.setListener(this);

			getSupportFragmentManager().beginTransaction().replace(R.id.detail, fragment, "detail").commit();
		} else {
			Intent it = new Intent(this, TaskActivity.class);
			it.putExtra("task", task);
			startActivityForResult(it, 1);
		}
		
			
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft = ft.hide(listUnCompleted).hide(listCompleted);
		switch (tab.getPosition()) {
		case 0:
			ft.show(listUnCompleted);
			break;
		case 1:
			ft.show(listCompleted);
			break;
		}		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaveClick() {
		listCompleted.refreshListTasks(1);
		listUnCompleted.refreshListTasks(0);
		
	}

	@Override
	public void onDeleteClick() {
		listCompleted.refreshListTasks(1);
		listUnCompleted.refreshListTasks(0);
	}

}
