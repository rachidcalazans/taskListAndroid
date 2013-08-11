package br.com.rcalazans.tasklist.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import br.com.rcalazans.tasklist.TaskAdapter;
import br.com.rcalazans.tasklist.dao.TaskDao;
import br.com.rcalazans.tasklist.model.Task;

import com.actionbarsherlock.app.SherlockListFragment;

public class ListTasksFragment extends SherlockListFragment {

	private TaskSelectListener listener;
	private int taskStatus;

	public static ListTasksFragment createNewInstance(int taskStatus) {
		ListTasksFragment result = new ListTasksFragment();
		result.taskStatus = taskStatus;
		return result;
	}
	
	public void setListener(TaskSelectListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null && savedInstanceState.containsKey("taskStatus")) {
			taskStatus = savedInstanceState.getInt("taskStatus");
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
//		List<Task> tasks = new TaskDao(getActivity()).listTasksByStatus(taskStatus);
//		setListAdapter(new TaskAdapter(tasks));
		refreshListTasks(taskStatus);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View layout = inflater.inflate(br.com.rcalazans.tasklist.R.layout.fragment_list, null);
		
		return layout;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt("taskStatus", taskStatus);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if (listener != null) {
			listener.onClick((Task)l.getAdapter().getItem(position) );
		}
	}
	
	public void refreshListTasks(int taskStatus) {
		List<Task> tasks = new TaskDao(getActivity()).listTasksByStatus(taskStatus);
		setListAdapter(new TaskAdapter(tasks));
	}
	
	public interface TaskSelectListener {
		void onClick(Task task);
	}
}
