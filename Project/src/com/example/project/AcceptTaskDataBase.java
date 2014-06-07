package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AcceptTaskDataBase {
	public static final String KEY_USERNAME = "task";
	public static final String KEY_PASSWORD = "address";
	public static final String KEY_ROWID = "_id";

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table task (_id integer primary key autoincrement, "
			+ "task text not null, address text not null);";

	private static final String DATABASE_NAME = "database";
	private static final String DATABASE_TABLE = "task";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS user");
			onCreate(db);
		}
	}

	public AcceptTaskDataBase(Context ctx) {
		this.mCtx = ctx;
	}

	public AcceptTaskDataBase open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void closeclose() {
		mDbHelper.close();
	}

	public long createTask(String task, String address) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERNAME, task);
		initialValues.put(KEY_PASSWORD, address);		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}	

	public Cursor getAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
				KEY_PASSWORD }, null, null, null, null, null);
	}

	public Cursor getDiary(String task) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
				KEY_PASSWORD }, KEY_USERNAME + "='" + task+"'", null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
}
