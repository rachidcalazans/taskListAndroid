package br.com.rcalazans.tasklist;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import br.com.rcalazans.tasklist.model.Task;

public class TaskAdapter extends BaseAdapter{

	List<Task> tasks;
	
	public TaskAdapter(List<Task> tasks) {
		super();
		this.tasks = tasks;
	}

	@Override
	public int getCount() {
		return tasks.size();
	}

	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Context context = parent.getContext();
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView  	    	  = LayoutInflater.from(context).inflate(R.layout.item_task, null);
			holder 		  	     	  = new ViewHolder();
//			holder.checkStatus 		  = (CheckBox) convertView.findViewById(R.id.checkStatus);
			holder.txtTaskDescription = (TextView) convertView.findViewById(R.id.txtTaskDescription);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Task task = tasks.get(position);
		
		holder.txtTaskDescription.setText(task.getDescription());
		
		if (task.getStatus() == 1) {
			convertView.setBackgroundColor(0xff00ff00);
//			holder.txtTaskDescription.setTextColor(0xff00ff00);
			
//			holder.checkStatus.setChecked(true);			
		} else {
			convertView.setBackgroundColor(0xffffffff);
//			holder.txtTaskDescription.setTextColor(0xffffffff);
//			holder.checkStatus.setChecked(false);
		}

		return convertView;
	}
	
	private static class ViewHolder {
//		CheckBox checkStatus;
		TextView txtTaskDescription;
	}

}
