package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Personal extends Activity{
	
	private List<Map<String,String>> mDataList = new ArrayList<Map<String, String>>();
	
	Button bt01 = null;
	ListView mListView = null;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal);
		
		mListView = (ListView)findViewById(R.id.listview1);
		/*
		Intent intentMsg = getIntent();
		String task = intentMsg.getStringExtra("task");
		String address = intentMsg.getStringExtra("address");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("task", task);
		map.put("address", address);
		mDataList.add(map);
		
		SimpleAdapter listItemAdapter  = new SimpleAdapter(this,mDataList,R.layout.item,new String[]{"task","address"},
				            new int[]{R.id.task,R.id.address});
		mListView.setAdapter(listItemAdapter);
		*/
		
		bt01 = (Button)findViewById(R.id.back);
		bt01.setOnClickListener(backOnClickListenner);
	}
	
	
	private OnClickListener backOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Personal.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
	};

		
		
		
	}
