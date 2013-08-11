package br.com.rcalazans.tasklist;

import java.util.List;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.rcalazans.tasklist.dao.TaskDao;
import br.com.rcalazans.tasklist.model.Task;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public class ReceiveTransitionsIntentService extends IntentService {

	private TaskDao daoTask;
	
	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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
		} else {
			// Get the type of transition (entry or exit)
			int transitionType = LocationClient.getGeofenceTransition(intent);
			
			daoTask     = new TaskDao(this);
			
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
					
					long geofenceTaskId = Long.parseLong(triggerList.get(i).getRequestId());
					
					buildNotificationTaskGeofence(geofenceTaskId);
					
					Log.e("rachid", "triggerIds: index: " + i + ": " + triggerList.get(i).getRequestId());
					
				}
				
			} else { // An invalid transition was reported
				Log.e("ReceiveTransitionsIntentService",
						"Geofence transition error: " + transitionType);
			}
		}
	}

	private void buildNotificationTaskGeofence(long geofenceTaskId) {
		Intent it = new Intent(this, TaskActivity.class);
		Task task = daoTask.taskByGeofenceTaskId(geofenceTaskId);
		it.putExtra("task", task);
		it.putExtra("task_come_notification", true);
		
		PendingIntent pit = PendingIntent.getActivity(this, 0, it, 0);
		
		int found = R.string.found;
		int showYourTask = R.string.show_your_task;
		
		String ticker 		= "TaskList: " + task.getDescription() + " " + getString(found);
		String contentTitle = "TaskList: " + task.getDescription() + " " + getString(found);
		String contentText  = getString(showYourTask);
		
		Notification notificacao = new NotificationCompat.Builder(this)
				.setTicker(ticker)
				.setContentTitle(contentTitle)
				.setContentText(contentText)
				.setSmallIcon(R.drawable.tasklist)
				.setWhen(System.currentTimeMillis())
				.setContentIntent(pit)
				.setAutoCancel(true)
				.getNotification();
				
		
		NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify((int) geofenceTaskId, notificacao);
	}

}
