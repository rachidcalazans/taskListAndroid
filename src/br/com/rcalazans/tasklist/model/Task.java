package br.com.rcalazans.tasklist.model;

import java.io.Serializable;

public class Task implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long   id;
	private long   geofenceTaskId;
	private String description;
	private String notes;
	private int    alert;
	private int    status;
	
	public Task(long id, long geofenceTaskId, String description, String notes, int alert,
			int status) {
		super();
		
		this.id 			= id;
		this.geofenceTaskId = geofenceTaskId;
		this.description 	= description;
		this.notes 	 	 	= notes;
		this.alert 			= alert;
		this.status 		= status;
		
	}
	
	public Task(long geofenceTaskId, String description, String notes, int alert,
			int status) {
		super();

		this.geofenceTaskId = geofenceTaskId;
		this.description 	= description;
		this.notes 	 	 	= notes;
		this.alert 			= alert;
		this.status 		= status;
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGeofenceTaskId() {
		return geofenceTaskId;
	}

	public void setGeofenceTaskId(long geofenceTaskId) {
		this.geofenceTaskId = geofenceTaskId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getAlert() {
		return alert;
	}

	public void setAlert(int alert) {
		this.alert = alert;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
