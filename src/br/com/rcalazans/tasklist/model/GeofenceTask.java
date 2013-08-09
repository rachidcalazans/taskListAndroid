package br.com.rcalazans.tasklist.model;

import java.io.Serializable;

import com.google.android.gms.location.Geofence;

public class GeofenceTask implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long   mId;
    private double mLatitude;
    private double mLongitude;
    private float  mRadius;
    private long   mExpirationDuration;
    private int    mTransitionType;
    private String mFormattedAdrress;
    
    public GeofenceTask() {
    	super();
    }
    
	public GeofenceTask(long mId, double mLatitude, double mLongitude,
			float mRadius, long mExpirationDuration, int mTransitionType) {
		super();

		this.mId 				 = mId;
		this.mLatitude  		 = mLatitude;
		this.mLongitude 		 = mLongitude;
		this.mRadius 			 = mRadius;
		this.mExpirationDuration = mExpirationDuration;
		this.mTransitionType 	 = mTransitionType;
	}
	
	public GeofenceTask(double mLatitude, double mLongitude,
			float mRadius, long mExpirationDuration, int mTransitionType) {
		super();

		this.mLatitude  		 = mLatitude;
		this.mLongitude 		 = mLongitude;
		this.mRadius 			 = mRadius;
		this.mExpirationDuration = mExpirationDuration;
		this.mTransitionType 	 = mTransitionType;
	}

	public long getId() {
        return mId;
    }
	 
    public void setId(long mId) {
		this.mId = mId;
	}

	public double getLatitude() {
        return mLatitude;
    }
	
	public void setLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}
    
    public double getLongitude() {
        return mLongitude;
    }
    
    public void setLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}
    
    public float getRadius() {
        return mRadius;
    }
    
    public void setRadius(float mRadius) {
		this.mRadius = mRadius;
	}

    public long getExpirationDuration() {
        return mExpirationDuration;
    }

    public void setExpirationDuration(long mExpirationDuration) {
		this.mExpirationDuration = mExpirationDuration;
	}
    
    public int getTransitionType() {
        return mTransitionType;
    }
    

	public void setTransitionType(int mTransitionType) {
		this.mTransitionType = mTransitionType;
	}
    
    public String getFormattedAdrress() {
		return mFormattedAdrress;
	}

	public void setFormattedAdrress(String mFormattedAdrress) {
		this.mFormattedAdrress = mFormattedAdrress;
	}

	/**
     * Creates a Location Services Geofence object from a
     * SimpleGeofence.
     *
     * @return A Geofence object
     */
    public Geofence toGeofence() {
        // Build a new Geofence object
        return new Geofence.Builder()
                .setRequestId(String.valueOf(getId()))
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(getLatitude(), getLongitude(), getRadius())
                .setExpirationDuration(mExpirationDuration)
                .build();
    }
    
}
