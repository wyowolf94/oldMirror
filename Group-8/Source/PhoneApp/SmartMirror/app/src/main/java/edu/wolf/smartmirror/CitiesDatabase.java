// All database related code from Jim Ward
// https://github.com/JimSeker/Android-Examples/tree/master/sqliteDemo/src/edu/cs4730/sqlitedemo

package edu.wolf.smartmirror;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CitiesDatabase {
    private myCitiesSQLiteHelper DBHelper;
    private SQLiteDatabase db;

    public CitiesDatabase(Context ctx) {
        DBHelper = new myCitiesSQLiteHelper(ctx);
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

    public long insertCity(String city, String state) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(myCitiesSQLiteHelper.KEY_CITY, city);
        initialValues.put(myCitiesSQLiteHelper.KEY_STATE, state);
        return db.insert(myCitiesSQLiteHelper.DATABASE_TABLE, null, initialValues);
    }

    public Cursor getCitiesByState(String stateid) throws SQLException {
        Cursor mCursor =
                db.query(true, myCitiesSQLiteHelper.DATABASE_TABLE,
                        new String[] {myCitiesSQLiteHelper.KEY_ROWID, myCitiesSQLiteHelper.KEY_CITY},
                        myCitiesSQLiteHelper.KEY_STATE + "=\'" + stateid + "\'", null, null, null, null, null);

        if(mCursor != null)
        {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public void removeCity(long id) throws SQLException
    {
        String idString = String.valueOf(id);
        String whereClause = mySQLiteHelper.KEY_ROWID + " = '" + idString + "'";
        String[] args = new String[0];
        db.delete(myCitiesSQLiteHelper.DATABASE_TABLE, whereClause, args);
    }

    public boolean updateRow(String name, String state) {
        ContentValues args = new ContentValues();
        args.put(myCitiesSQLiteHelper.KEY_CITY, name);
        args.put(myCitiesSQLiteHelper.KEY_STATE, state);
        return db.update(myCitiesSQLiteHelper.DATABASE_TABLE, args, myCitiesSQLiteHelper.KEY_CITY + "= \'" + name +"\'", null) > 0;
    }

    public Cursor getAllCities() {
        Cursor c = db.query(myCitiesSQLiteHelper.DATABASE_TABLE,
                new String[] {myCitiesSQLiteHelper.KEY_ROWID, myCitiesSQLiteHelper.KEY_CITY, myCitiesSQLiteHelper.KEY_STATE},
                null, null, null, null,
                myCitiesSQLiteHelper.KEY_CITY);
        if (c != null )
            c.moveToFirst();
        return c;
    }

    public void emptyAllCities() {
        db.delete(myCitiesSQLiteHelper.DATABASE_TABLE,null,null);
    }
}
