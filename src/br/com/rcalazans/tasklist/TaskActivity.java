package br.com.rcalazans.tasklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import br.com.rcalazans.tasklist.MainActivity.ResponseReceiver;
import br.com.rcalazans.tasklist.fragment.TaskFragment;
import br.com.rcalazans.tasklist.fragment.TaskFragment.DetailTaskListerner;
import br.com.rcalazans.tasklist.model.Task;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TaskActivity extends SherlockFragmentActivity implements DetailTaskListerner{
	
	private TaskFragment fragment;
	private Task task; 
	private boolean taskComeNotification = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		if (savedInstanceState != null && savedInstanceState.containsKey("task")) {
			task = (Task) savedInstanceState.getSerializable("task");
		} else if (getIntent().hasExtra("task")) {
			task = (Task) getIntent().getSerializableExtra("task");
		} else {
			task = null;
		}
		
		if (getIntent().hasExtra("task_come_notification")) {
			taskComeNotification = getIntent().getBooleanExtra("task_come_notification", false);
		}
		
		fragment = TaskFragment.createNewInstance(task);

		fragment.setListener(this);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.detail, fragment, "detail").commit();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		fragment.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCancelClick() {
		setResult(Activity.RESULT_CANCELED);
		finish();
		
	}

	@Override
	public void onSaveClick() {
		setResult(Activity.RESULT_OK);
		finish();
	}

	@Override
	public void onDeleteClick() {
		setResult(Activity.RESULT_OK);
		finish();		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (taskComeNotification) {
			Intent broadcastIntent = new Intent();
	    	broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
	    	broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
	    	sendBroadcast(broadcastIntent);
		}
	}
	
}
