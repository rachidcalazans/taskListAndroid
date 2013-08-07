package br.com.rcalazans.tasklist.model;

import com.google.android.gms.location.Geofence;

public class SimpleGeofence {

    private final String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;
    
	public SimpleGeofence(String mId, double mLatitude, double mLongitude,
			float mRadius, long mExpirationDuration, int mTransitionType) {
		
		super();
		this.mId = mId;
		this.mLatitude = mLatitude;
		this.mLongitude = mLongitude;
		this.mRadius = mRadius;
		this.mExpirationDuration = mExpirationDuration;
		this.mTransitionType = mTransitionType;
	}
	
	 public String getId() {
         return mId;
     }
	 
     public double getLatitude() {
         return mLatitude;
     }
     
     public double getLongitude() {
         return mLongitude;
     }
     
     public float getRadius() {
         return mRadius;
     }
     
     public long getExpirationDuration() {
         return mExpirationDuration;
     }
     
     public int getTransitionType() {
         return mTransitionType;
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
                 .setRequestId(getId())
                 .setTransitionTypes(mTransitionType)
                 .setCircularRegion(getLatitude(), getLongitude(), getRadius())
                 .setExpirationDuration(mExpirationDuration)
                 .build();
     }
 
    
}
