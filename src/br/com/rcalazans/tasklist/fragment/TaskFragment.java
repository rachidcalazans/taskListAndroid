package br.com.rcalazans.tasklist.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import br.com.rcalazans.tasklist.R;
import br.com.rcalazans.tasklist.ReaderWebService;
import br.com.rcalazans.tasklist.dao.GeofenceDao;
import br.com.rcalazans.tasklist.dao.TaskDao;
import br.com.rcalazans.tasklist.model.GeofenceTask;
import br.com.rcalazans.tasklist.model.Task;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

public class TaskFragment extends SherlockFragment implements OnClickListener {

	private CheckBox checkStatus;
	private EditText edtDescription;
	private CheckBox checkAlert;
	private EditText edtAddress;
	private EditText edtNotes;
	private Button   btSearchAddress;
	private Button   btCurrentLocation;
	
	private com.actionbarsherlock.view.Menu menu;
	
	private Task task;
	private GeofenceTask geofenceTask;
	private TaskDao daoTask;
	private GeofenceDao daoGeofence;
	boolean canSave = true;
	
	private DetailTaskListerner listener;
	
	private ReaderAsyncTask readerAsynTask;
	
	public static TaskFragment createNewInstance(Task task) {
		TaskFragment result = new TaskFragment();
		result.task = task;
		return result;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		daoTask 	= new TaskDao(getActivity());
		daoGeofence = new GeofenceDao(getActivity());
		
		setHasOptionsMenu(true);
		
		View v	 = inflater.inflate(br.com.rcalazans.tasklist.R.layout.fragment_task, null);
		
		checkStatus       = (CheckBox) v.findViewById(R.id.checkStatus);
		edtDescription    = (EditText) v.findViewById(R.id.edtDescription);
		checkAlert        = (CheckBox) v.findViewById(R.id.checkAlert);
		edtAddress        = (EditText) v.findViewById(R.id.edtAddress);
		edtNotes          = (EditText) v.findViewById(R.id.edtNotes);
		btSearchAddress   = (Button) v.findViewById(R.id.btSearchAddress);
		btCurrentLocation = (Button) v.findViewById(R.id.btCurrentLocation);

		btSearchAddress.setOnClickListener(this);
		btCurrentLocation.setOnClickListener(this);
		
		if (savedInstanceState != null && savedInstanceState.containsKey("task")) {
			task = (Task) savedInstanceState.getSerializable("task");
		}
		
		buildFormWithTask();
		
		if (menu != null) {
			menu.findItem(br.com.rcalazans.tasklist.R.id.action_deletar).setVisible(task != null);	
		}
		
		return v;

	}
	
	@Override
	public void onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu,
			com.actionbarsherlock.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(br.com.rcalazans.tasklist.R.menu.detail_task, menu);
		this.menu = menu;
		menu.findItem(br.com.rcalazans.tasklist.R.id.action_deletar).setVisible(task != null);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
				
		if (item.getItemId() == br.com.rcalazans.tasklist.R.id.action_salvar) {
			
			String msgError    = new String();
			boolean campoVazio = false;
			canSave 		   = true;
			boolean novo 	   = task == null;
			
			if (novo) {
				task = new Task();
			}
												
			buildTaskWithInputsForm();
			
			if (task.getDescription().equals("")) {
				int descriptionRequire = R.string.description_require;
				msgError +=  getString(descriptionRequire)+" \n";
				campoVazio = true;
			}
			
			if (task.getAlert() == 1 && task.getAddress().equals("")) {
				int addressRequire = R.string.address_require;
				msgError +=  getString(addressRequire)+" \n";
				campoVazio = true;
			}
			
			if (campoVazio) {
				Toast.makeText(getActivity(), msgError, Toast.LENGTH_SHORT).show();
			} else {
				
				//ir webservice para buscar latitue e long. com endereco
				if (task.getAlert() == 1 && task.getGeofenceTaskId() == 0) {
					
					int addressNotFound = R.string.address_not_found;
					msgError +=  getString(addressNotFound)+" \n";
					
					Toast.makeText(getActivity(), msgError, Toast.LENGTH_SHORT).show();
					canSave = false;
					
				}
				
				if (canSave) {
					
					daoTask.inserirAlterar(task);
					if (listener != null) {
						cleanForm();
						listener.onSaveClick();
					}
				}
				
			}			
			
		} else if (item.getItemId() == br.com.rcalazans.tasklist.R.id.action_deletar) {
			daoTask.deletar(task);

			if (listener != null) {
				cleanForm();
				listener.onDeleteClick();
			}
		} else if (item.getItemId() == br.com.rcalazans.tasklist.R.id.action_cancelar) {
			task = null;

			cleanForm();

			listener.onCancelClick();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void cleanForm() {
		edtDescription.setText("");
		edtAddress.setText("");
		edtNotes.setText("");
		checkStatus.setChecked(false);
		checkAlert.setChecked(false);
	}

	private void buildTaskWithInputsForm() {
		
		if (task == null) {
			task = new Task();
		}
		
		task.setDescription(edtDescription.getText().toString());
		task.setAddress(edtAddress.getText().toString());
		task.setNotes(edtNotes.getText().toString());
		
		if (checkStatus.isChecked()) {
			task.setStatus(1);
		} else {
			task.setStatus(0);
		}
		
		if (checkAlert.isChecked()) {
			task.setAlert(1);
		} else {
			task.setAlert(0);
		}		
	}
	
	private void buildFormWithTask() {
		if (task != null) {
			edtDescription.setText(task.getDescription());
			edtAddress.setText(task.getAddress());
			edtNotes.setText(task.getNotes());
			checkStatus.setChecked(task.getStatus() == 1);
			checkAlert.setChecked(task.getAlert() == 1);
		}
	} 

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (task == null) {
			task = new Task();
		}
		
		buildTaskWithInputsForm();
		//bundle nao esta funcionando
		outState.putSerializable("task", task);
	}
	
	public void setListener(DetailTaskListerner listener) {
		this.listener = listener;
	}
	
	@Override
	public void onClick(View view) {
		Button bt 					= (Button)view;
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info 			= manager.getActiveNetworkInfo();
		String params				= new String();
		
		buildTaskWithInputsForm();
		
		if (bt.getId() == R.id.btSearchAddress) {
			int searhingAddress = R.string.searhing_address;
			Toast.makeText(getActivity(), searhingAddress, Toast.LENGTH_SHORT).show();
			
			String addressFormated = task.addressFormated();
			params = "address=" + addressFormated.replace(" ", "+");

		}
		
		if (bt.getId() == R.id.btCurrentLocation) {
			int searhingCurrentLocation= R.string.searhing_current_location;
			Toast.makeText(getActivity(), searhingCurrentLocation, Toast.LENGTH_SHORT).show();
			
			LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			double mLongitude = 0.0;
			double mLatitude  = 0.0;
			
			if (location != null)
			{
				mLongitude = location.getLongitude();
				mLatitude = location.getLatitude();
			}
			
			
			params = "latlng=" + String.valueOf(mLatitude) + "," + String.valueOf(mLongitude);
		}
		
		if (info != null && info.isConnected()) {
			if (readerAsynTask == null) {
				readerAsynTask = new ReaderAsyncTask();
//				task.setAddress("rua josé carneiro da cunha sarmento, maceio, brasil");
				
//				String addressFormated = task.addressFormated();
//				String params = addressFormated.replace(" ", "+");
//				String params = task.getAddress().replace(" ", "+");
				
				String url = "http://maps.googleapis.com/maps/api/geocode/json?" + params + "&sensor=true"; 
//				String url = "http://maps.googleapis.com/maps/api/geocode/json?address=rua+jose+carneiro+da+cunha+sarmento,+maceio,+brasil&sensor=true"; 
				readerAsynTask.execute(url, String.valueOf(task.getGeofenceTaskId()));
			}
		} else {
			
			int internetFailure = R.string.internet_failure;
			Toast.makeText(getActivity(), getString(internetFailure), Toast.LENGTH_SHORT).show();
		}
	}
	
	public interface DetailTaskListerner {
		public void onCancelClick();

		public void onSaveClick();

		public void onDeleteClick();
	}
	
	class ReaderAsyncTask extends ReaderWebService {

		@Override
		protected void onPostExecute(GeofenceTask result) {
			
			if (result == null) {
				int addressNotFound = R.string.address_not_found;
				Toast.makeText(getActivity(), getString(addressNotFound), Toast.LENGTH_SHORT).show();
			} else {
				
				int addressFound = R.string.address_found;
				Toast.makeText(getActivity(), getString(addressFound), Toast.LENGTH_SHORT).show();
				
				geofenceTask = result;
				daoGeofence.inserirAlterar(geofenceTask);
				task.setGeofenceTaskId(geofenceTask.getId());
				task.setAddress(geofenceTask.getFormattedAdrress());
				
				buildFormWithTask();
				
			}
		}

	}
	
}
