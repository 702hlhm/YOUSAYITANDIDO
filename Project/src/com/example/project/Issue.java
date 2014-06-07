package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

public class Issue extends Activity{
	
	SimpleAdapter simpleAdapter;
	ArrayList<HashMap<String, String>> listItem;

	EditText nameStr = null;
	EditText taskStr = null;
	EditText phoneStr = null;
	EditText addressStr = null;
	EditText payStr = null;
	EditText timeStr = null;
	
	Button sureBtn = null;
	Button cancerBtn = null;
	
	private IssueTaskDataBase mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.issue);
		
		mDbHelper = new IssueTaskDataBase(this);
		mDbHelper.open();
		
		nameStr = (EditText)findViewById(R.id.issue_et01);
		taskStr = (EditText)findViewById(R.id.issue_et02);
		phoneStr = (EditText)findViewById(R.id.issue_et03);
		addressStr = (EditText)findViewById(R.id.issue_et04);
		payStr = (EditText)findViewById(R.id.issue_et05);
		timeStr = (EditText)findViewById(R.id.issue_et06);
		
		sureBtn = (Button)findViewById(R.id.issue_bt01);
		cancerBtn = (Button)findViewById(R.id.issue_bt02);
		
		sureBtn.setOnClickListener(sureOnClickListenner);
		cancerBtn.setOnClickListener(cancerOnClickListenner);
	}
	
	private OnClickListener sureOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String task = taskStr.getText().toString();
			String address = addressStr.getText().toString();
			
			mDbHelper.createTask(task, address);
            
			Intent intentMain = new Intent();
			intentMain.setClass(Issue.this, MainActivity.class);

			startActivity(intentMain);
			
			finish();
			
		}
	};
	
	private OnClickListener cancerOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Issue.this,MainActivity.class);
			startActivity(intent);
			//finish();
		}
	};
	

}