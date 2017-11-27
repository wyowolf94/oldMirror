// All database related code from Jim Ward
// https://github.com/JimSeker/Android-Examples/tree/master/sqliteDemo/src/edu/cs4730/sqlitedemo

package edu.wolf.smartmirror;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class myAlarmSQLiteHelper extends SQLiteOpenHelper{
    public static final String KEY_TIME = "Time";
    public static final String KEY_TITLE = "Alarm";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "alarm.db";
    public static final String DATABASE_TABLE = "Alarms";

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE =
            "CREATE TABLE Alarms (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_TIME + " TEXT, " +
                    KEY_TITLE + " TEXT );";

    myAlarmSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Version changing from " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
