package br.com.rcalazans.tasklist;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.rcalazans.tasklist.MainActivity.ResponseReceiver;
import br.com.rcalazans.tasklist.dao.TaskDao;
import br.com.rcalazans.tasklist.model.Task;

public class TaskAdapter extends BaseAdapter implements OnClickListener{

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
			holder.imgStatus 		  = (ImageView) convertView.findViewById(R.id.imgStatus);
			holder.txtTaskDescription = (TextView) convertView.findViewById(R.id.txtTaskDescription);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Task task = tasks.get(position);
		
		holder.txtTaskDescription.setText(task.getDescription());
		
		Bitmap imgStatusTrue  = null;
		Bitmap imgStatusFalse = null;
		
		try {
			imgStatusTrue  = BitmapFactory.decodeStream(context.getAssets().open("btn_check_on_focused_holo_dark.png"));
			imgStatusFalse = BitmapFactory.decodeStream(context.getAssets().open("btn_check_off_normal_holo_light.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		holder.imgStatus.setOnClickListener(this);
		
		if (task.getStatus() == 1) {
			holder.imgStatus.setImageBitmap(imgStatusTrue);
			holder.imgStatus.setTag(task);
//			convertView.setBackgroundColor(0xff00ff00);
//			holder.txtTaskDescription.setTextColor(0xff00ff00);
			
//			holder.checkStatus.setChecked(true);			
		} else {
			holder.imgStatus.setImageBitmap(imgStatusFalse);
			holder.imgStatus.setTag(task);
//			convertView.setBackgroundColor(0xffffffff);
//			holder.txtTaskDescription.setTextColor(0xffffffff);
//			holder.checkStatus.setChecked(false);
		}

		return convertView;
	}
	
	private static class ViewHolder {
		ImageView imgStatus;
		TextView txtTaskDescription;
	}

	@Override
	public void onClick(View v) {
		Log.d("rachid", "IMG CLIKED");
		
		ImageView imgStatus = (ImageView) v;
		
		Bitmap imgStatusTrue  = null;
		Bitmap imgStatusFalse = null;
		
		try {
			imgStatusTrue  = BitmapFactory.decodeStream(v.getContext().getAssets().open("btn_check_on_focused_holo_dark.png"));
			imgStatusFalse = BitmapFactory.decodeStream(v.getContext().getAssets().open("btn_check_off_normal_holo_light.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Task task = (Task)imgStatus.getTag();
		
		if (task.getStatus() == 1) {
			task.setStatus(0);
			imgStatus.setImageBitmap(imgStatusFalse);
			imgStatus.setTag(task);
		} else {
			task.setStatus(1);
			imgStatus.setImageBitmap(imgStatusTrue);
			imgStatus.setTag(task);
		}
		
		new TaskDao(v.getContext()).inserirAlterar(task);
		
		Intent broadcastIntent = new Intent();
    	broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
    	broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
    	v.getContext().sendBroadcast(broadcastIntent);
		
	}

}
