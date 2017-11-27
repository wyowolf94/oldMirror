// All database related code from Jim Ward
// https://github.com/JimSeker/Android-Examples/tree/master/sqliteDemo/src/edu/cs4730/sqlitedemo

package edu.wolf.smartmirror;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlarmDatabase {
    private myAlarmSQLiteHelper DBHelper;
    private SQLiteDatabase adb;

    public AlarmDatabase(Context ctx) {
        DBHelper = new myAlarmSQLiteHelper(ctx);
    }

    public void open() throws SQLException 	{
        adb = DBHelper.getWritableDatabase();
    }

    public boolean isOpen() throws SQLException {
        return adb.isOpen();
    }

    public void close() {
        DBHelper.close();
        adb.close();
    }

    public long insertAlarm(String time, String alarm) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(myAlarmSQLiteHelper.KEY_TIME, time);
        initialValues.put(myAlarmSQLiteHelper.KEY_TITLE, alarm);
        return adb.insert(myAlarmSQLiteHelper.DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllAlarms() {
        Cursor c = adb.query(myAlarmSQLiteHelper.DATABASE_TABLE,
                new String[] {myAlarmSQLiteHelper.KEY_ROWID, myAlarmSQLiteHelper.KEY_TIME,
                        myAlarmSQLiteHelper.KEY_TITLE}, null, null, null, null, myAlarmSQLiteHelper.KEY_TIME);
        if (c != null )
            c.moveToFirst();
        return c;
    }

    public Cursor get1alarm(String time) throws SQLException  {
        Cursor mCursor =
                adb.query(true, myAlarmSQLiteHelper.DATABASE_TABLE,
                        new String[] { myAlarmSQLiteHelper.KEY_TIME, myAlarmSQLiteHelper.KEY_TITLE, },
                        myAlarmSQLiteHelper.KEY_TIME + "=\'" + time +"\'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String getTitle(long id) throws SQLException
    {
        //String idString = String.valueOf(id);
        Cursor mCursor = adb.query(true, myAlarmSQLiteHelper.DATABASE_TABLE,
               new String[] {myAlarmSQLiteHelper.KEY_TIME, myAlarmSQLiteHelper.KEY_TITLE, },
                myAlarmSQLiteHelper.KEY_ROWID + "=\'" + id + "\'", null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Log.d("MCURSOR", mCursor.toString());

        String title = mCursor.getString(1);
        Log.d("TITLE", title);
        return title;
    }

    public String getTime(long id) throws SQLException
    {
        //String idString = String.valueOf(id);
        Cursor mCursor = adb.query(true, myAlarmSQLiteHelper.DATABASE_TABLE,
                new String[] {myAlarmSQLiteHelper.KEY_TIME, myAlarmSQLiteHelper.KEY_TITLE, },
                myAlarmSQLiteHelper.KEY_ROWID + "=\'" + id + "\'", null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Log.d("MCURSOR", mCursor.toString());

        String title = mCursor.getString(0);
        Log.d("TITLE", title);
        return title;
    }

    public void removeAlarm(long id) throws SQLException
    {
        String idString = String.valueOf(id);
        String whereClause = myAlarmSQLiteHelper.KEY_ROWID + " = '" + idString + "'";
        String[] args = new String[0];
        adb.delete(myAlarmSQLiteHelper.DATABASE_TABLE, whereClause, args);
    }


    public boolean updateRow(String time, String title) {
        ContentValues args = new ContentValues();
        args.put(myAlarmSQLiteHelper.KEY_TIME, time);
        return adb.update(myAlarmSQLiteHelper.DATABASE_TABLE, args, myAlarmSQLiteHelper.KEY_TITLE + "= \'" + title +"\'", null) > 0;
    }

    public void emptyAllAlarms() {
        adb.delete(myAlarmSQLiteHelper.DATABASE_TABLE,null,null);
    }
}
