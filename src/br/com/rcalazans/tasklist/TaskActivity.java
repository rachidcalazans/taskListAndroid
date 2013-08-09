package br.com.rcalazans.tasklist;

import android.app.Activity;
import android.os.Bundle;
import br.com.rcalazans.tasklist.fragment.TaskFragment;
import br.com.rcalazans.tasklist.fragment.TaskFragment.DetailTaskListerner;
import br.com.rcalazans.tasklist.model.Task;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TaskActivity extends SherlockFragmentActivity implements DetailTaskListerner{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Task task = (Task) getIntent().getSerializableExtra("task");

		TaskFragment fragment = TaskFragment.createNewInstance(task);

		fragment.setListener(this);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.detail, fragment, "detail").commit();
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
