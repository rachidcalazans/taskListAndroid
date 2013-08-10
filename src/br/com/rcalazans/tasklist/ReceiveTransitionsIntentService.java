package br.com.rcalazans.tasklist;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public class ReceiveTransitionsIntentService extends IntentService {

	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
//		Toast.makeText(this, "RecTransIntServ Intiated",
//				Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d("rachid", "Receive_Service_OnBIND");
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("rachid", "Receive_Service_OnStartCommand");
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// First check for errors
		if (LocationClient.hasError(intent)) {
			// Get the error code with a static method
			int errorCode = LocationClient.getErrorCode(intent);
			// Log the error
			Log.e("rachid",
					"ReceiveTransitionsIntentService: Location Services error: " + Integer.toString(errorCode));
			/*
			 * You can also send the error code to an Activity or Fragment with
			 * a broadcast Intent
			 */
			/*
			 * If there's no error, get the transition type and the IDs of the
			 * geofence or geofences that triggered the transition
			 */
		} else {
			// Get the type of transition (entry or exit)
			int transitionType = LocationClient.getGeofenceTransition(intent);
			
			Log.e("rachid",
					"onHandleIntent: transitionType: " + transitionType);
			
			// Test that a valid transition was reported
			if ((transitionType == Geofence.GEOFENCE_TRANSITION_ENTER)
					|| (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT)) {
				List<Geofence> triggerList = LocationClient
						.getTriggeringGeofences(intent);

				String[] triggerIds = new String[triggerList.size()];

				for (int i = 0; i < triggerIds.length; i++) {
					// Store the Id of each geofence
					triggerIds[i] = triggerList.get(i).getRequestId();
					Log.e("rachid",
							"triggerIds: index: " + i + ": " + triggerList.get(i).getRequestId());
				}
				
				/*
				 * At this point, you can store the IDs for further use display
				 * them, or display the details associated with them.
				 */
			} else { // An invalid transition was reported
				Log.e("ReceiveTransitionsIntentService",
						"Geofence transition error: " + transitionType);
			}
		}
	}

}
