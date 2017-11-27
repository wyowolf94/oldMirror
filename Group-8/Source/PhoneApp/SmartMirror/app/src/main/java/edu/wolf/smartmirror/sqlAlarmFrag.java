// All database related code from Jim Ward
// https://github.com/JimSeker/Android-Examples/tree/master/sqliteDemo/src/edu/cs4730/sqlitedemo

package edu.wolf.smartmirror;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.wolf.smartmirror.myAlarmSQLiteHelper;
import edu.wolf.smartmirror.AlarmDatabase;

public class sqlAlarmFrag extends Fragment {
    String TAG = "sqlAlarmFrag";
    Context myContext;
    Button runAgain;

    TextView output;
    AlarmDatabase db;

    public sqlAlarmFrag() {
        // Empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.add_alarm, container, false);

        db = new AlarmDatabase(myContext);
        db.open();

        method();

        runAgain = (Button) myView.findViewById(R.id.alarmOk);
        runAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method();
            }
        });

        return myView;
    }

    public void appendthis(String item) {
        output.append(item + "\n");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db == null)
            db = new AlarmDatabase(myContext);
        if (!db.isOpen())
            db.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (db.isOpen())
            db.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        Log.d(TAG, "onAttach");
    }

    public void method() {
        appendthis("Start of Demo code.");

        Cursor c;
        c = db.getAllAlarms();

        if (c == null) {
            //nothing in the database ?  maybe the database query failed.
            appendthis("No DB found, creating and inserting data");
            //insert data.
            db.insertAlarm("12:00", "Fix this");
            db.insertAlarm("12:01", "And this");
            return;
        }
        //check to see if no data?
        if (c.getCount() == 0) {
            //no data return
            appendthis("empty DB, inserting data");
            db.insertAlarm("12:00", "Fix this");
            db.insertAlarm("12:01", "And this");
            return;
        }
        appendthis("There is already data, so just displaying it.");
        //display any data.
        //moveToFirst() should not be needed, but just in case.
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            //Column: 0 ROWID, 1 name, 2 score
            //We could just use c.getString(1) because we know column 1 is name, but it
            //not very readable and if we were change the DB, then this could would not work
            //instead using a longer, but more readable and DB change method.
            //so not using this appendthis(	c.getString(1) + " " + c.getInt(2));
            appendthis(c.getString(c.getColumnIndex(myAlarmSQLiteHelper.KEY_TIME)) + " " +
                    c.getString(c.getColumnIndex(myAlarmSQLiteHelper.KEY_TITLE)));
        }
        c.close();  //release the resources, before I use it again.
        //test on return 1 item.

        c = db.get1alarm("12:00");  //test of query for 1.
        //A note, get1name only returns two columns, name and score.  while getall returns three
        //columns.

        //c = db.get1nameR("Jim");   //test of rawQuery
        if (c.getCount() == 0) {
            appendthis("failed on select 1 item.");
        } else {
            appendthis("Select on 12:00 returned: " +
                    c.getString(c.getColumnIndex(myAlarmSQLiteHelper.KEY_TIME)) + " " +  //column 0
                    c.getString(c.getColumnIndex(myAlarmSQLiteHelper.KEY_TITLE))            //column 1
            );
        }
        c.close(); //release the resources
        c = null;
    }
}

