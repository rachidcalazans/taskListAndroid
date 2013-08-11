package br.com.rcalazans.tasklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import br.com.rcalazans.tasklist.model.GeofenceTask;

import com.google.android.gms.location.Geofence;

public abstract class ReaderWebService  extends AsyncTask<String, Void, GeofenceTask>{
	
	//Monta o geofence de acordo com o resultado do webservice
	@Override
	protected GeofenceTask doInBackground(String... params) {
		GeofenceTask geofenceTask = null;
		
		try {
			
			long geofenceTaskId = Long.parseLong(params[1]); 
			
			String json = urlToStr(params[0]);
			
			Log.d("rachid", "Json: "+json);
			
			JSONObject jsonObject 	   = new JSONObject(json);
			
			if (jsonObject.getString("status").equals("OK")) {
				Log.d("rachid", "Status OK ");
				
				JSONArray  jsonResults     = jsonObject.getJSONArray("results");
				JSONObject jsonObjectFirst = jsonResults.getJSONObject(0);
//				JSONObject jsonAddress     = jsonObjectFirst.getJSONObject("formatted_address");
				JSONObject jsonGeometry    = jsonObjectFirst.getJSONObject("geometry");
				JSONObject jsonLocation    = jsonGeometry.getJSONObject("location");
				
				
				
				try {
					float radius = 100;
					double lat   = jsonLocation.getDouble("lat");
					double lng   = jsonLocation.getDouble("lng");
					
					if (geofenceTaskId == 0) {
						geofenceTask = new GeofenceTask(lat, lng, radius, 
								Geofence.NEVER_EXPIRE, 
								Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
					} else {
						geofenceTask = new GeofenceTask(geofenceTaskId ,lat, lng, radius, 
								Geofence.NEVER_EXPIRE, 
								Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
					}
					
					geofenceTask.setFormattedAdrress(jsonObjectFirst.getString("formatted_address"));
					
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.d("rachid", "Status NO_RESULTS ");
				return null;
			}
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return geofenceTask;
	}

	private String urlToStr(String urlStr) throws IOException {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                        con.getInputStream()));
	        String inputLine;
	        String s= in.readLine();
	        while ((inputLine = in.readLine()) != null) 
	            s += inputLine;
	        in.close();
	        return s;
	}

	@Override
	abstract protected void onPostExecute(GeofenceTask result);
}
