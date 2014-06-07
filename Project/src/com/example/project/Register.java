package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity{
	
	EditText userEdt = null;
	EditText passwordEdt = null;
	
	Button sureBtn = null;
	Button cancerBtn = null;
	
	String username = "";
	String password = "";
	
	private UserDataBase mDbHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		mDbHelper = new UserDataBase(this);
		mDbHelper.open();
		
		userEdt = (EditText) findViewById(R.id.register_et01);
		passwordEdt = (EditText) findViewById(R.id.register_et02);
		
		sureBtn = (Button) findViewById(R.id.register_bt01);
		cancerBtn = (Button) findViewById(R.id.register_bt02);
		
		sureBtn.setOnClickListener(sureOnClickListener);
		cancerBtn.setOnClickListener(cancerOnClickListener);
	}
	
	private OnClickListener sureOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {

			username = userEdt.getText().toString();
			password = passwordEdt.getText().toString();

			if (username.trim().equals("")
					|| password.trim().equals("")) {
				Toast.makeText(Register.this, "用户名密码或者电话号码不能为空!",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				Cursor cursor = mDbHelper.getDiary(username);
				if(cursor.moveToFirst()){
					Toast.makeText(Register.this, "用户名已存在！",
							Toast.LENGTH_SHORT).show();
				}else{
					mDbHelper.createUser(username, password);
					Toast.makeText(Register.this, "注册成功，等待登录...",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(Register.this, Login.class);
					startActivity(intent);
				}
			}
		}
	};
	
	private OnClickListener cancerOnClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Register.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
		
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Register.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
	
}