package br.com.rcalazans.tasklist.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.rcalazans.tasklist.model.GeofenceTask;

public class GeofenceDao {
	
	private DbHelper helper;

	public GeofenceDao(Context context) {
		helper = new DbHelper(context);
	}

	public void deletar(GeofenceTask geofenceTask) {
		if (geofenceTask.getId() != 0) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.delete("geofence_tasks", "_id = " + geofenceTask.getId(), null);
			db.close();
		}
	}

	public void inserirAlterar(GeofenceTask geofenceTask) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		
		cv.put("latitude",            geofenceTask.getLatitude());
		cv.put("longitude",	          geofenceTask.getLongitude());
		cv.put("radius", 		      geofenceTask.getRadius());
		cv.put("expiration_duration", geofenceTask.getExpirationDuration());
		cv.put("transition_type", 	  geofenceTask.getTransitionType());

		if (geofenceTask.getId() == 0) {
			geofenceTask.setId(db.insert("geofence_tasks", null, cv));
		} else {
			db.update("geofence_tasks", cv, "_id = " + geofenceTask.getId(), null);
		}

		db.close();
	}

	public List<GeofenceTask> listGeofenceTasks() {
		ArrayList<GeofenceTask> geofenceTasks = new ArrayList<GeofenceTask>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from geofence_tasks", null);

		while (cursor.moveToNext()) {
			
			long   id 			      = cursor.getLong(cursor.getColumnIndex("_id"));
		    double latitude			  = cursor.getDouble(cursor.getColumnIndex("latitude"));
		    double longitude		  = cursor.getDouble(cursor.getColumnIndex("longitude"));
		    float  radius  	    	  = cursor.getFloat(cursor.getColumnIndex("radius"));
		    long   expirationDuration = cursor.getLong(cursor.getColumnIndex("expiration_duration"));
		    int    transitionType     = cursor.getInt(cursor.getColumnIndex("transition_type"));
			
		    GeofenceTask geofenceTask = new GeofenceTask(id, latitude, longitude, radius, expirationDuration, transitionType);

		    geofenceTasks.add(geofenceTask);
		}

		cursor.close();
		db.close();

		return geofenceTasks;
	}
	
	public List<GeofenceTask> listGeofenceTasksWithTasksIds(List<Long> geofencesIds) {
		ArrayList<GeofenceTask> geofenceTasks = new ArrayList<GeofenceTask>();
		String ids = new String();
		
		if (geofencesIds != null && !geofencesIds.isEmpty()) {
			for (Long id : geofencesIds) {
				ids += id.toString() + ",";
			}
		}
		
		ids += "0";
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from geofence_tasks where _id in (" + ids + ")", null);
		
		while (cursor.moveToNext()) {
			
			long   id 			      = cursor.getLong(cursor.getColumnIndex("_id"));
			double latitude			  = cursor.getDouble(cursor.getColumnIndex("latitude"));
			double longitude		  = cursor.getDouble(cursor.getColumnIndex("longitude"));
			float  radius  	    	  = cursor.getFloat(cursor.getColumnIndex("radius"));
			long   expirationDuration = cursor.getLong(cursor.getColumnIndex("expiration_duration"));
			int    transitionType     = cursor.getInt(cursor.getColumnIndex("transition_type"));
			
			GeofenceTask geofenceTask = new GeofenceTask(id, latitude, longitude, radius, expirationDuration, transitionType);
			
			geofenceTasks.add(geofenceTask);
		}
		
		cursor.close();
		db.close();
		
		return geofenceTasks;
	}
}
