package alberto.android.todomanager;

import java.util.ArrayList;
import java.util.List;

import alberto.android.todomanager.ToDoItem.Priority;
import alberto.android.todomanager.ToDoItem.Status;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ToDoListAdapter extends BaseAdapter {

	// List of ToDoItems
	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	
	private final Context mContext;
	private final Activity mActivity;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context, Activity activity) {

		mContext = context;
		mActivity = activity;
		
	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}
	
	// Clears the list adapter of all items.
	
	public void clear(){

		mItems.clear();
		notifyDataSetChanged();
	
	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}
	

	//Create a View to display the ToDoItem 
	// at specified position in mItems

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		//TODO - Get the current ToDoItem
		final ToDoItem toDoItem = (ToDoItem) getItem(position);

		//TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final RelativeLayout itemLayout = (RelativeLayout) inflater.inflate(R.layout.todo_item, parent, false);
		
		//TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined 
		// in the layout file 

		//TODO - Display Title in TextView

		final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
		titleView.setText(toDoItem.getTitle());
		
		// TODO - Set up Status CheckBox
	
		final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
		statusView.setChecked(toDoItem.getStatus() == Status.DONE);
		
		
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				log("Entered onCheckedChanged()");
				
				// TODO - Set up and implement an OnCheckedChangeListener, which 
				// is called when the user toggles the status checkbox
				if (isChecked) {
					toDoItem.setStatus(Status.DONE);
					itemLayout.setBackgroundResource(R.color.done);
				} else {
					toDoItem.setStatus(Status.NOTDONE);
					itemLayout.setBackgroundResource(R.color.not_done);
				}
			}
		});

		//TODO - Display Priority in a TextView

//		final TextView priorityView = (TextView) itemLayout.findViewById(R.id.priorityView);
//		priorityView.setText(toDoItem.getPriority().toString());
		final Spinner prioritySpinner = (Spinner) itemLayout.findViewById(R.id.prioritySpinner);
		setPrioritySpinner(prioritySpinner, toDoItem);
		
		
		prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				switch (position) {
				case 0:
					toDoItem.setPriority(Priority.LOW);
					break;
				case 1:
					toDoItem.setPriority(Priority.MED);
					break;
				case 2:
					toDoItem.setPriority(Priority.HIGH);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				setPrioritySpinner(prioritySpinner, toDoItem);
			}
		});
		
		
		
		// TODO - Display Time and Date. 
		// Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and time String

		final TextView dateView = (TextView) itemLayout.findViewById(R.id.dateView);
		dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));
				
		
		// Diferent background color if the item is done or not
		if (toDoItem.getStatus() == Status.DONE) {
			itemLayout.setBackgroundResource(R.color.done);
		} else {
			itemLayout.setBackgroundResource(R.color.not_done);
		}

		
		// Delete item
		itemLayout.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				log("Entered onLongClick()");
				
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
				alertBuilder.setMessage("Are you sure do you want to delete this task?");
				alertBuilder.setCancelable(true);
				alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	log("Delete task");
	                	mItems.remove(position);
	                	notifyDataSetChanged();
	                    dialog.cancel();
	                }
	            });
				alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                }
	            });

	            AlertDialog alert = alertBuilder.create();
	            alert.show();
	            
				return false;
			}
		});
		

		// Return the View you just created
		return itemLayout;

	}

	
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

	
	private void setPrioritySpinner(Spinner prioritySpinner, ToDoItem toDoItem) {
		switch (toDoItem.getPriority()) {
			case LOW:
				prioritySpinner.setSelection(0);
				break;
			case MED:
				prioritySpinner.setSelection(1);
				break;
			case HIGH:
				prioritySpinner.setSelection(2);
				break;
		}
	}
}
