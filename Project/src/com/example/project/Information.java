package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Information extends Activity {
	
	private Button backBtn;
	private Button acceptBtn;
	private TextView mTextVeiw;
	public String task;
	public String address;
	
	private AcceptTaskDataBase mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		
		mDbHelper = new AcceptTaskDataBase(this);
		mDbHelper.open();
		
		Intent intentMsg = getIntent();
		task = intentMsg.getStringExtra("task");
		address = intentMsg.getStringExtra("address");

		mTextVeiw = ((TextView)findViewById(R.id.information));
		mTextVeiw.setText("任务：" + task + "\n" + "地址：" + address);
		
		acceptBtn = (Button)findViewById(R.id.back);
		acceptBtn.setOnClickListener(acceptOnclickListener);
		
		backBtn = (Button)findViewById(R.id.back);
		backBtn.setOnClickListener(backOnclickListener);
		
	}
	
	private OnClickListener acceptOnclickListener = new OnClickListener() {
		public void onClick(View v)
		{
			Toast.makeText(Information.this, "成功接受任务",
					Toast.LENGTH_SHORT).show();
			
			mDbHelper.createTask(task, address);
            
			Intent intentPersonal = new Intent();
			intentPersonal.setClass(Information.this, Personal.class);

			startActivity(intentPersonal);
			
			finish();
		}
	};
	
	private OnClickListener backOnclickListener = new OnClickListener() {
		public void onClick(View v)
		{
			Intent intentMain = new Intent();
			intentMain.setClass(Information.this, MainActivity.class);
			
			startActivity(intentMain);
			
			finish();
		}
	};

}
