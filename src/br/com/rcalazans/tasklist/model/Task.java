package br.com.rcalazans.tasklist.model;

import java.io.Serializable;

public class Task implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long   id;
	private long   geofenceTaskId;
	private String description;
	private String notes;
	private String address;
	private int    alert;
	private int    status;
	
	public Task() {
		super();
	}
	
	public Task(long id, long geofenceTaskId, String description, String notes, String address, int alert,
			int status) {
		super();
		
		this.id 			= id;
		this.geofenceTaskId = geofenceTaskId;
		this.description 	= description;
		this.notes 	 	 	= notes;
		this.address     	= address;
		this.alert 			= alert;
		this.status 		= status;
		
	}
	
	public Task(long geofenceTaskId, String description, String notes, String address, int alert,
			int status) {
		super();

		this.geofenceTaskId = geofenceTaskId;
		this.description 	= description;
		this.notes 	 	 	= notes;
		this.address     	= address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
	
	// Retira os acentos
	public String addressFormated() {
		String newAddress = new String();
		
		if (!address.equals("")) {
			
			String[] minusculas = {"á","à","ã","â","é","è","ẽ","ê","í","ì","ĩ","î","ó","ò","õ","ô","ú","ù","ũ","û","ç"};
			String[] maiusculas = {"Á","À","Ã","Â","É","È","Ẽ","Ê","Í","Ì","Ĩ","Î","Ó","Ò","Õ","Ô","Ú","Ù","Ũ","Û","Ç"};
			String[] semAcento =  {"a","a","a","a","e","e","e","e","i","i","i","i","o","o","o","o","u","u","u","u","c"};

			int addressSize = address.length();
			
			for (int i = 0; i < addressSize; i++) {
		        
		        Character letra = address.charAt(i);
		        
		        boolean foiInserido = false;
		        
		        for (int j = 0; j < minusculas.length; j++) {
		        	Character charMinu = minusculas[j].charAt(0);
					if (charMinu.equals(letra)) {
						newAddress += semAcento[j];
						foiInserido = true;
					}
				}
		        
		        for (int k = 0; k < maiusculas.length; k++) {
		        	Character charMai = maiusculas[k].charAt(0);
					if (charMai.equals(letra)) {
		        		newAddress += semAcento[k];
		        		foiInserido = true;
		        	} 
		        }
		        
		        if (!foiInserido) {
		        	newAddress += letra;
		        }
		        
				
			}
		
		}
		
		return newAddress;
	}

}
