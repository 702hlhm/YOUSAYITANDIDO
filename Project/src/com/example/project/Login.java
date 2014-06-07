package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity{
	
	EditText passwordET;
	Button loginBtn;
	Button cancerBtn;
	
	AutoCompleteTextView cardNumAuto;
	CheckBox savePasswordCB;
	SharedPreferences sp;
	String cardNumStr = "";
	String passwordStr = "";
	
	private UserDataBase mDbHelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		mDbHelper = new UserDataBase(this);
		mDbHelper.open();
		
		cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		passwordET = (EditText) findViewById(R.id.passwordEdt);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		cancerBtn = (Button) findViewById(R.id.cancerBtn);
		loginBtn.setOnClickListener(loginOnClickListener);
		cancerBtn.setOnClickListener(cancerOnClickListener);

		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
		savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);// 默认为记住密码
		cardNumAuto.setThreshold(1);// 输入1个字母就开始自动提示
		passwordET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		cardNumAuto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String[] allUserName = new String[sp.getAll().size()];
				allUserName = sp.getAll().keySet().toArray(new String[0]);

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						Login.this,
						android.R.layout.simple_dropdown_item_1line,
						allUserName);

				cardNumAuto.setAdapter(adapter);// 设置数据适配器

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				passwordET.setText(sp.getString(cardNumAuto.getText()
						.toString(), ""));

			}
		});
	}
		
		private OnClickListener loginOnClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {

				cardNumStr = cardNumAuto.getText().toString();
				passwordStr = passwordET.getText().toString();
				if((cardNumStr == null||cardNumStr.equalsIgnoreCase("")) || (passwordStr == null||passwordStr.equalsIgnoreCase(""))){
					Toast.makeText(Login.this, "用户名或者密码输入为空!",
							Toast.LENGTH_SHORT).show();
				}else{
					Cursor cursor = mDbHelper.getDiary(cardNumStr);

					if(!cursor.moveToFirst()){
						Toast.makeText(Login.this,  "用户名不存在！",
								Toast.LENGTH_SHORT).show();
					}else if (!passwordStr.equals(cursor.getString(2))) {
						Toast.makeText(Login.this, "密码错误，请重新输入！",
								Toast.LENGTH_SHORT).show();
					} else {
						if (savePasswordCB.isChecked()) {// 登陆成功才保存密码
							sp.edit().putString(cardNumStr, passwordStr).commit();
						}
						Toast.makeText(Login.this, "登录成功！",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.putExtra("username", cardNumStr);
						intent.setClass(Login.this, MainActivity.class);
						startActivity(intent);

					}
				}				
			}
		};
		
		private OnClickListener cancerOnClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Login.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}

}
