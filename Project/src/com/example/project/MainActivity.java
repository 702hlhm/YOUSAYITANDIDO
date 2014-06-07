package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	
	private List<Map<String,String>> mDataList = new ArrayList<Map<String, String>>();
	private ListView mListView;
	
	Button bt01 = null;
	Button bt02 = null;
	Button bt03 = null;
	Button bt04 = null;
	
	private IssueTaskDataBase mDbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDbHelper = new IssueTaskDataBase(this);
		mDbHelper.open();
		
		bt01 = (Button)findViewById(R.id.register);
		bt02 = (Button)findViewById(R.id.login);
		bt03 = (Button)findViewById(R.id.issue);
		bt04 = (Button)findViewById(R.id.personal);
		
        mListView = (ListView)findViewById(R.id.listview1);
		setData();
		SimpleAdapter listItemAdapter  = new SimpleAdapter(this,mDataList,R.layout.item,new String[]{"task","address"},
				            new int[]{R.id.task,R.id.address});
		mListView.setAdapter(listItemAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		
		bt01.setOnClickListener(registerOnClickListenner);
		bt02.setOnClickListener(loginOnClickListenner);
		bt03.setOnClickListener(issueOnClickListenner);
		bt04.setOnClickListener(personalOnClickListenner);
	}
	
	private void setData() {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("task", "Ò»·¹Å£Èâ»¬µ°8Ôª");
		map.put("address", "É÷Ë¼Ô°ÁùºÅ202");
		mDataList.add(map);
		
		map = new HashMap<String,String>();
		map.put("task", "¶þ·¹¼¦ÅÅ·¹7Ôª");
		map.put("address", "ÖÁÉÆÔ°ÎåºÅ303");
		mDataList.add(map);
		
		map = new HashMap<String,String>();
		map.put("task", "Èý·¹Å£Èâ³´·¹7Ôª");
		map.put("address", "Ã÷µÂÔ°ËÄºÅ404");
		mDataList.add(map);
		
		map = new HashMap<String,String>();
		map.put("task", "ËÄ·¹¹¬±£¼¦¶¡8Ôª");
		map.put("address", "¸ñÖÂÔ°¶þºÅ505");
		mDataList.add(map);
		
		map = new HashMap<String,String>();
		map.put("task", "Ò»·¹ÖíÅÅ·¹8Ôª");
		map.put("address", "É÷Ë¼Ô°ÆßºÅ606");
		mDataList.add(map);
		
		map = new HashMap<String,String>();
		map.put("task", "Èý·¹ÖíÈâ³´Ã×·¹7Ôª");
		map.put("address", "ÖÁÉÆÔ°ÎåºÅ707");
		mDataList.add(map);
	}
	
	/*******listViewµÄ¼àÌýÆ÷**********/
	private OnItemClickListener mItemClickListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			Bundle bundle = new Bundle();
			bundle.putString("task",(String)(mDataList.get(arg2).get("task")));
			bundle.putSerializable("address",(String)(mDataList.get(arg2).get("address")));
			Intent intentInfo = new Intent();
			intentInfo.setClass(MainActivity.this, Information.class);
			
			intentInfo.putExtras(bundle);
			startActivity(intentInfo);
			
			//finish();			
		}
	};
	
	private OnClickListener registerOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this,Register.class);
			startActivity(intent);
			//finish();
		}
	};
	
	
	private OnClickListener loginOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this,Login.class);
			startActivity(intent);
			//finish();
		}
	};
	
	private OnClickListener issueOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this,Issue.class);
			startActivity(intent);
			//finish();
		}
	};
		
	private OnClickListener personalOnClickListenner = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(MainActivity.this,Personal.class);
			startActivity(intent);
			//finish();
		}
	};
		
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.exit);
		menu.add(0, 2, 2, R.string.about);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == 1){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
