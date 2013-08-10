package br.com.rcalazans.tasklist;

import android.app.Activity;
import android.os.Bundle;
import br.com.rcalazans.tasklist.fragment.TaskFragment;
import br.com.rcalazans.tasklist.fragment.TaskFragment.DetailTaskListerner;
import br.com.rcalazans.tasklist.model.Task;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TaskActivity extends SherlockFragmentActivity implements DetailTaskListerner{
	
	private TaskFragment fragment;
	private Task task; 
	
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

}
