package br.com.rcalazans.tasklist.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

	public DbHelper(Context context) {
		super(context, "dbTasks", null, 2);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table tasks (_id integer primary key autoincrement, geofence_task_id integer, description text, alert integer, status integer);");
		db.execSQL("create table geofence_tasks (_id integer primary key autoincrement, latitude real, longitude real, radius real, expiration_duration real, transition_type integer);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			db.execSQL("drop table tasks; drop table geofence_tasks;");
			onCreate(db);
		}		
	}

}
