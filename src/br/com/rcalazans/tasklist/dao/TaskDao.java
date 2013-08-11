package br.com.rcalazans.tasklist.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.rcalazans.tasklist.model.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskDao {

	private DbHelper helper;

	public TaskDao(Context context) {
		helper = new DbHelper(context);
	}

	public void deletar(Task task) {
		if (task.getId() != 0) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.delete("tasks", "_id = " + task.getId(), null);
			db.close();
		}
	}

	public void inserirAlterar(Task task) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		
		cv.put("geofence_task_id", task.getGeofenceTaskId());
		cv.put("description",	   task.getDescription());
		cv.put("notes",    		   task.getNotes());
		cv.put("address",  		   task.getAddress());
		cv.put("alert", 		   task.getAlert());
		cv.put("status", 		   task.getStatus());

		if (task.getId() == 0) {
			task.setId(db.insert("tasks", null, cv));
		} else {
			db.update("tasks", cv, "_id = " + task.getId(), null);
		}

		db.close();
	}

	public Task taskByGeofenceTaskId(long geofenceTaskId) {
		Task task = null;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from tasks where geofence_task_id = " + geofenceTaskId, null);

		while (cursor.moveToNext()) {
			
			long   id 			  = cursor.getLong(cursor.getColumnIndex("_id"));
			long   geofenceId     = cursor.getLong(cursor.getColumnIndex("geofence_task_id"));
			String description    = cursor.getString(cursor.getColumnIndex("description"));
			String notes		  = cursor.getString(cursor.getColumnIndex("notes"));
			String address		  = cursor.getString(cursor.getColumnIndex("address"));
			int    alert 		  = cursor.getInt(cursor.getColumnIndex("alert"));
			int    status 		  = cursor.getInt(cursor.getColumnIndex("status"));
			
			task = new Task(id, geofenceId, description, notes, address, alert, status);

		}

		cursor.close();
		db.close();
		return task;
	}
	
	public List<Task> listTasksByStatus(int taskStatus) {
		ArrayList<Task> tasks = new ArrayList<Task>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from tasks where status = " + taskStatus + " order by description", null);

		while (cursor.moveToNext()) {
			
			long   id 			  = cursor.getLong(cursor.getColumnIndex("_id"));
			long   geofenceTaskId = cursor.getLong(cursor.getColumnIndex("geofence_task_id"));
			String description    = cursor.getString(cursor.getColumnIndex("description"));
			String notes		  = cursor.getString(cursor.getColumnIndex("notes"));
			String address		  = cursor.getString(cursor.getColumnIndex("address"));
			int    alert 		  = cursor.getInt(cursor.getColumnIndex("alert"));
			int    status 		  = cursor.getInt(cursor.getColumnIndex("status"));
			
			Task task = new Task(id, geofenceTaskId, description, notes, address, alert, status);

			tasks.add(task);
		}

		cursor.close();
		db.close();

		return tasks;
	}
	
	public List<Task> listTasksByStatusByAlert(int taskStatus, int alertStatus) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from tasks where status = " + taskStatus + " and alert = " + alertStatus + " order by description", null);
		
		while (cursor.moveToNext()) {
			
			long   id 			  = cursor.getLong(cursor.getColumnIndex("_id"));
			long   geofenceTaskId = cursor.getLong(cursor.getColumnIndex("geofence_task_id"));
			String description    = cursor.getString(cursor.getColumnIndex("description"));
			String notes		  = cursor.getString(cursor.getColumnIndex("notes"));
			String address		  = cursor.getString(cursor.getColumnIndex("address"));
			int    alert 		  = cursor.getInt(cursor.getColumnIndex("alert"));
			int    status 		  = cursor.getInt(cursor.getColumnIndex("status"));
			
			Task task = new Task(id, geofenceTaskId, description, notes, address, alert, status);
			
			tasks.add(task);
		}
		
		cursor.close();
		db.close();
		
		return tasks;
	}
	
	public List<Task> listTasks() {
		ArrayList<Task> tasks = new ArrayList<Task>();
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from tasks", null);
		
		while (cursor.moveToNext()) {
			
			long   id 			  = cursor.getLong(cursor.getColumnIndex("_id"));
			long   geofenceTaskId = cursor.getLong(cursor.getColumnIndex("geofence_task_id"));
			String description    = cursor.getString(cursor.getColumnIndex("description"));
			String notes		  = cursor.getString(cursor.getColumnIndex("notes"));
			String address		  = cursor.getString(cursor.getColumnIndex("address"));
			int    alert 		  = cursor.getInt(cursor.getColumnIndex("alert"));
			int    status 		  = cursor.getInt(cursor.getColumnIndex("status"));
			
			Task task = new Task(id, geofenceTaskId, description, notes, address, alert, status);
			
			tasks.add(task);
		}
		
		cursor.close();
		db.close();
		
		return tasks;
	}
}
