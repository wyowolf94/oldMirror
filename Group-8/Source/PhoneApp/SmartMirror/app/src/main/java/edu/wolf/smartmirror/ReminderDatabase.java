// All database related code from Jim Ward
// https://github.com/JimSeker/Android-Examples/tree/master/sqliteDemo/src/edu/cs4730/sqlitedemo

package edu.wolf.smartmirror;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReminderDatabase {
    private mySQLiteHelper DBHelper;
    private SQLiteDatabase db;

    public ReminderDatabase(Context ctx) {
        DBHelper = new mySQLiteHelper(ctx);
    }

    public void open() throws SQLException 	{
        db = DBHelper.getWritableDatabase();
    }

    public boolean isOpen() throws SQLException {
        return db.isOpen();
    }

    public void close() {
        DBHelper.close();
        db.close();
    }

    public long insertReminder(String date, String time, String reminder) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(mySQLiteHelper.KEY_DATE, date);
        initialValues.put(mySQLiteHelper.KEY_TIME, time);
        initialValues.put(mySQLiteHelper.KEY_TITLE, reminder);
        return db.insert(mySQLiteHelper.DATABASE_TABLE, null, initialValues);
    }

    public Cursor getAllReminders() {
        Cursor c = db.query(mySQLiteHelper.DATABASE_TABLE,
                new String[] {mySQLiteHelper.KEY_ROWID, mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_TIME, mySQLiteHelper.KEY_TITLE},
                null, null, null, null,
                mySQLiteHelper.KEY_DATE);
        if (c != null )
            c.moveToFirst();
        return c;
    }

    public Cursor get1reminder(String time) throws SQLException  {
        Cursor mCursor =
                db.query(true, mySQLiteHelper.DATABASE_TABLE,
                        new String[] { mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_TIME, mySQLiteHelper.KEY_TITLE, },
                        mySQLiteHelper.KEY_TIME + "=\'" + time +"\'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String getTitle(long id) throws SQLException
    {
        //String idString = String.valueOf(id);
        Cursor mCursor = db.query(true, mySQLiteHelper.DATABASE_TABLE,
               new String[] {mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_TIME, mySQLiteHelper.KEY_TITLE, },
                mySQLiteHelper.KEY_ROWID + "=\'" + id + "\'", null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Log.d("MCURSOR", mCursor.toString());

        String title = mCursor.getString(2);
        Log.d("TITLE", title);
        return title;
    }

    public String getDate(long id) throws SQLException
    {
        //String idString = String.valueOf(id);
        Cursor mCursor = db.query(true, mySQLiteHelper.DATABASE_TABLE,
                new String[] {mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_TIME, mySQLiteHelper.KEY_TITLE, },
                mySQLiteHelper.KEY_ROWID + "=\'" + id + "\'", null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Log.d("MCURSOR", mCursor.toString());

        String date = mCursor.getString(0);
        Log.d("DATE", date);
        return date;
    }

    public String getTime(long id) throws SQLException
    {
        //String idString = String.valueOf(id);
        Cursor mCursor = db.query(true, mySQLiteHelper.DATABASE_TABLE,
                new String[] {mySQLiteHelper.KEY_DATE, mySQLiteHelper.KEY_TIME, mySQLiteHelper.KEY_TITLE, },
                mySQLiteHelper.KEY_ROWID + "=\'" + id + "\'", null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Log.d("MCURSOR", mCursor.toString());

        String time = mCursor.getString(1);
        Log.d("TIME", time);
        return time;
    }

    public void removeReminder(long id) throws SQLException
    {
        String idString = String.valueOf(id);
        String whereClause = mySQLiteHelper.KEY_ROWID + " = '" + idString + "'";
        String[] args = new String[0];
        db.delete(mySQLiteHelper.DATABASE_TABLE, whereClause, args);
    }


    public boolean updateRow(String date, String time, String title) {
        ContentValues args = new ContentValues();
        args.put(mySQLiteHelper.KEY_DATE, date);
        args.put(mySQLiteHelper.KEY_TIME, time);
        return db.update(mySQLiteHelper.DATABASE_TABLE, args, mySQLiteHelper.KEY_TITLE + "= \'" + title +"\'", null) > 0;
    }

    public void emptyAllReminders() {
        db.delete(mySQLiteHelper.DATABASE_TABLE,null,null);
    }
}
