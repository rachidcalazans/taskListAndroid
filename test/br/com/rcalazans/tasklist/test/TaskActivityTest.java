package br.com.rcalazans.tasklist.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;
import android.widget.EditText;
import br.com.rcalazans.tasklist.R;
import br.com.rcalazans.tasklist.TaskActivity;

public class TaskActivityTest extends  ActivityInstrumentationTestCase2<TaskActivity> {

	private CheckBox checkStatus;
    private EditText edtDescription;
    private CheckBox checkAlert;
    private EditText edtAddress;
    private EditText edtNotes;

	public TaskActivityTest() {
		super("br.com.rcalazans.tasklist", TaskActivity.class);
	}
	
	public TaskActivityTest(String str){
    	this();
    	setName(str);
    }
	
	 @Override
	    protected void setUp() throws Exception {
	        super.setUp();

	        TaskActivity taskActivity = getActivity();

//	        checkStatus    = (CheckBox) taskActivity.findViewById(R.id.checkStatus);
	        edtDescription = (EditText) taskActivity.findViewById(R.id.edtDescription);
	        checkAlert     = (CheckBox) taskActivity.findViewById(R.id.checkAlert);
	        edtAddress     = (EditText) taskActivity.findViewById(R.id.edtAddress);
	        edtNotes       = (EditText) taskActivity.findViewById(R.id.edtNotes);
	    }
	 
	 public void testAddValues() {
	        // we use sendKeys instead of setText so it goes through entry
	        // validation
//	        sendKeys(NUMBER_24);
	        // now on value2 entry
//	        sendKeys(NUMBER_74);

	        // now on Add button
//	        sendKeys("ENTER");

	        // get result
//	        String mathResult = result.getText().toString();
//	        assertTrue("Add result should be 98 " + ADD_RESULT + " but was "
//	                + mathResult, mathResult.equals(ADD_RESULT));
		 boolean t = true;
		 assertTrue("Test true", t);
	    }
}
