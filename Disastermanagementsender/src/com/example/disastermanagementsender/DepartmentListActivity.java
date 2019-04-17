package com.example.disastermanagementsender;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class DepartmentListActivity extends Activity {

	static ArrayList<String> arrayl;
	static ArrayAdapter<String> aa;
	LocationManager locationManager;
	ListView lv;
	

	/** Called when the activity is first created. */

	EditText placename;
	String name = "police";
	static String dept = "";
	
	List<String> depts = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.departmentlist);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		arrayl = new ArrayList<String>();

		
		depts.add("Technical");
		depts.add("Accounts");
		depts.add("Transport");
		depts.add("Human Resource");
		depts.add("House keeping");
		depts.add("Foods");
		depts.add("Facility");
		
		
		lv = (ListView) findViewById(R.id.listview);
		
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, depts);
		lv.setAdapter(aa);
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				dept = depts.get(arg2);
				Intent intent = new Intent(DepartmentListActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});

	}
}