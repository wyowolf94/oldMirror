// Used: http://stackoverflow.com/questions/9544737/read-file-from-assets

package edu.wolf.smartmirror;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewSwitcher switcher;

    EditText ip, port;
    String ipString, portString, sendMessage;
    Thread myNet;

    //MyReceiver mReceiver;
    public static final String ACTION = "edu.wolf.smartmirror.MainActivity";

    String a1String = ""; String a1Current = "";
    String a2String = ""; String a2Current = "";
    String a3String = ""; String a3Current = "";
    String b1String = ""; String b1Current = "";
    String b2String = ""; String b2Current = "";
    String b3String = ""; String b3Current = "";
    String c1String = ""; String c1Current = "";
    String c2String = ""; String c2Current = "";
    String c3String = ""; String c3Current = "";

    Button connect, sleepCancel, sleepOk, powerCancel, powerOk, clearCancel, clearOk, clockCancel, clockOk,
            gesturesCancel, gesturesOk, vcCancel, vcOk, colorCancel, colorOk, textColor, backColor,
            weatherCancel, weatherOk, addReminder, reminderCancel, reminderOk, addRCancel, addROk, addRDelete,
            addAlarm, alarmCancel, alarmOk, addACancel, addAOk, addADelete, dateButton, timeButton,
            timePickCancel, timePickOk, datePickCancel, datePickOk;
    Button text1, text2, text3, text4, text5, text6, text7, text8;
    Button back1, back2, back3, back4, back5, back6, back7, back8;

    Button a1, a2, a3, b1, b2, b3, c1, c2, c3;

    RadioButton sleepSelected, powerSelected, formatSelected, timezoneSelected, gesturesSelected, vcSelected;
    RadioButton a1format, a2format, a3format, b1format, b2format, b3format, c1format, c2format, c3format;
    RadioButton a1timezone, a2timezone, a3timezone, b1timezone, b2timezone, b3timezone, c1timezone, c2timezone, c3timezone;

    int a1text, a2text, a3text, b1text, b2text, b3text, c1text, c2text, c3text, textTemp;
    int a1back, a2back, a3back, b1back, b2back, b3back, c1back, c2back, c3back, backTemp;

    int sleepID, powerID, gestureID, voiceID;
    int a1timezoneID, a2timezoneID, a3timezoneID, b1timezoneID, b2timezoneID, b3timezoneID, c1timezoneID, c2timezoneID, c3timezoneID;
    int a1formatID, a2formatID, a3formatID, b1formatID, b2formatID, b3formatID, c1formatID, c2formatID, c3formatID;

    Spinner stateSpinner, citySpinner;
    CitiesDatabase citydb;
    TextView cityText;
    String stateSelected, citySelected;
    String a1state, a2state, a3state, b1state, b2state, b3state, c1state, c2state, c3state;
    int a1city = -1, a2city = -1, a3city = -1, b1city = -1, b2city = -1, b3city = -1, c1city = -1, c2city = -1, c3city = -1;

    TimePicker timePick;
    DatePicker datePick;
    TextView timePicked, datePicked, reminderText;
    ReminderDatabase db;
    String rDate, rTime, rText;

    TextView alarmText;
    AlarmDatabase adb;
    String aTime, aText;

    SimpleCursorAdapter dataAdapter;
    SimpleCursorAdapter alarmDataAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);

        return true;
    }

    // What happens when an item is selected from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // If the user selects the "Network Settings" option
            //     If you are on the mirror layout
            //         Exit the method
            //     If you didn't exit above, switch the view.
            //     Exit the method
            case R.id.network:
                if(findViewById(R.id.networkSwitch).isShown())
                {
                    return true;
                }
                switcher.showNext();
                return true;

            // If the user selects the "Sleep Timer" option
            //     Create a new Dialog and set the layout to the correct file
            //
            case R.id.sleep:
                final Dialog sleepDialog = new Dialog(this);
                sleepDialog.setContentView(R.layout.sleep);

                List<String> sleepList = new ArrayList<>();
                sleepList.add("30 Seconds");
                sleepList.add("10 Minutes");
                sleepList.add("20 Minutes");
                sleepList.add("30 Minutes");
                sleepList.add("1 Hour");
                sleepList.add("2 Hours");
                sleepList.add("No Sleep");

                final RadioGroup rg = (RadioGroup) sleepDialog.findViewById(R.id.sleepGroup);
                for (int i=0; i<sleepList.size(); i++)
                {
                    RadioButton rb = new RadioButton(this);
                    rb.setText(sleepList.get(i));
                    rb.setId(i+1);
                    rg.addView(rb);
                    if(sleepSelected != null)
                    {
                        if(i == (sleepSelected.getId()-1))
                        {
                            rb.toggle();
                        }
                    }
                    else if(sleepID != 0)
                    {
                        if(i == (sleepID-1))
                        {
                            rb.toggle();
                        }
                    }
                    else
                    {
                        if(i == 0)
                        {
                            rb.toggle();
                        }
                    }
                }

                sleepCancel = (Button) sleepDialog.findViewById(R.id.sleepCancel);
                sleepCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sleepDialog.dismiss();
                    }
                });

                sleepOk = (Button) sleepDialog.findViewById(R.id.sleepOk);
                sleepOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = rg.getCheckedRadioButtonId();
                        if(id == 1)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will sleep after 30 Seconds", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 2)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will sleep after 10 Minutes", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 3)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will sleep after 20 Minutes", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 4)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will sleep after 30 Minutes", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 5)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will sleep after 1 Hour", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 6)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will sleep after 2 Hours", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 7)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror will not sleep", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                        }
                        sleepSelected = (RadioButton) sleepDialog.findViewById(id);
                        sleepDialog.dismiss();
                        sleepID = sleepSelected.getId();
                    }
                });

                sleepDialog.show();

                return true;

            case R.id.power:
                final Dialog powerDialog = new Dialog(this);

                powerDialog.setContentView(R.layout.power);
                List<String> powerList = new ArrayList<>();
                powerList.add("Mirror On");
                powerList.add("Mirror Off");

                final RadioGroup rg2 = (RadioGroup) powerDialog.findViewById(R.id.sleepGroup);
                for (int i=0; i<powerList.size(); i++)
                {
                    RadioButton rb2 = new RadioButton(this);
                    rb2.setText(powerList.get(i));
                    rb2.setId(i+1);
                    rg2.addView(rb2);
                    if(powerSelected != null)
                    {
                        if(i == (powerSelected.getId()-1))
                        {
                            rb2.toggle();
                        }
                    }
                    else if(powerID != 0)
                    {
                        if(i == (powerID-1))
                        {
                            rb2.toggle();
                        }
                    }
                    else
                    {
                        if(i == 0)
                        {
                            rb2.toggle();
                        }
                    }
                }

                powerCancel = (Button) powerDialog.findViewById(R.id.powerCancel);
                powerCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        powerDialog.dismiss();
                    }
                });

                powerOk = (Button) powerDialog.findViewById(R.id.powerOk);
                powerOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = rg2.getCheckedRadioButtonId();
                        if(id == 1)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror On", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 2)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Mirror Off", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                        }
                        powerSelected = (RadioButton) powerDialog.findViewById(id);
                        powerDialog.dismiss();
                        powerID = powerSelected.getId();
                    }
                });

                powerDialog.show();

                return true;

            case R.id.handGestures:
                final Dialog gestureDialog = new Dialog(this);

                gestureDialog.setContentView(R.layout.hand_gestures);
                List<String> gestureList = new ArrayList<>();
                gestureList.add("On");
                gestureList.add("Off");

                final RadioGroup gestureGroup = (RadioGroup) gestureDialog.findViewById(R.id.gesturesGroup);
                for (int i=0; i<gestureList.size(); i++)
                {
                    RadioButton gestureRadio = new RadioButton(this);
                    gestureRadio.setText(gestureList.get(i));
                    gestureRadio.setId(i+1);
                    gestureGroup.addView(gestureRadio);
                    if(gesturesSelected != null)
                    {
                        if(i == (gesturesSelected.getId()-1))
                        {
                            gestureRadio.toggle();
                        }
                    }
                    else if(gestureID != 0)
                    {
                        if(i == (gestureID-1))
                        {
                            gestureRadio.toggle();
                        }
                    }
                    else
                    {
                        if(i == 0)
                        {
                            gestureRadio.toggle();
                        }
                    }
                }

                gesturesCancel = (Button) gestureDialog.findViewById(R.id.gesturesCancel);
                gesturesCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gestureDialog.dismiss();
                    }
                });

                gesturesOk = (Button) gestureDialog.findViewById(R.id.gesturesOk);
                gesturesOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = gestureGroup.getCheckedRadioButtonId();
                        if(id == 1)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Hand Gestures On", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 2)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Hand Gestures Off", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                        }
                        gesturesSelected = (RadioButton) gestureDialog.findViewById(id);
                        gestureDialog.dismiss();
                        gestureID = gesturesSelected.getId();
                    }
                });

                gestureDialog.show();

                return true;

            case R.id.voiceControl:
                final Dialog vcDialog = new Dialog(this);

                vcDialog.setContentView(R.layout.voice_control);
                List<String> vcList = new ArrayList<>();
                vcList.add("On");
                vcList.add("Off");

                final RadioGroup vcGroup = (RadioGroup) vcDialog.findViewById(R.id.vcontrolGroup);
                for (int i=0; i<vcList.size(); i++)
                {
                    RadioButton vcRadio = new RadioButton(this);
                    vcRadio.setText(vcList.get(i));
                    vcRadio.setId(i+1);
                    vcGroup.addView(vcRadio);
                    if(vcSelected != null)
                    {
                        if(i == (vcSelected.getId()-1))
                        {
                            vcRadio.toggle();
                        }
                    }
                    else if(voiceID != 0)
                    {
                        if(i == (voiceID-1))
                        {
                            vcRadio.toggle();
                        }
                    }
                    else
                    {
                        if(i == 0)
                        {
                            vcRadio.toggle();
                        }
                    }
                }

                vcCancel = (Button) vcDialog.findViewById(R.id.vcontrolCancel);
                vcCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vcDialog.dismiss();
                    }
                });

                vcOk = (Button) vcDialog.findViewById(R.id.vcontrolOk);
                vcOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = vcGroup.getCheckedRadioButtonId();
                        if(id == 1)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Voice Control On", Toast.LENGTH_LONG).show();
                        }
                        else if (id == 2)
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Voice Control Off", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("ID", String.valueOf(id));
                            Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                        }
                        vcSelected = (RadioButton) vcDialog.findViewById(id);
                        vcDialog.dismiss();
                        voiceID = vcSelected.getId();
                    }
                });

                vcDialog.show();

                return true;
            case R.id.clear:
                final Dialog clearDialog = new Dialog(this);

                clearDialog.setContentView(R.layout.clear_mirror);

                clearCancel = (Button) clearDialog.findViewById(R.id.clearCancel);
                clearCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearDialog.dismiss();
                    }
                });

                clearOk = (Button) clearDialog.findViewById(R.id.clearOk);
                clearOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        a1.setBackgroundColor(Color.parseColor("#000000"));
                        a1.setTextColor(Color.parseColor("#ffffff"));
                        a1.setText("+");
                        a2.setBackgroundColor(Color.parseColor("#000000"));
                        a2.setTextColor(Color.parseColor("#ffffff"));
                        a2.setText("+");
                        a3.setBackgroundColor(Color.parseColor("#000000"));
                        a3.setTextColor(Color.parseColor("#ffffff"));
                        a3.setText("+");

                        b1.setBackgroundColor(Color.parseColor("#000000"));
                        b1.setTextColor(Color.parseColor("#ffffff"));
                        b1.setText("+");
                        b2.setBackgroundColor(Color.parseColor("#000000"));
                        b2.setTextColor(Color.parseColor("#ffffff"));
                        b2.setText("+");
                        b3.setBackgroundColor(Color.parseColor("#000000"));
                        b3.setTextColor(Color.parseColor("#ffffff"));
                        b3.setText("+");

                        c1.setBackgroundColor(Color.parseColor("#000000"));
                        c1.setTextColor(Color.parseColor("#ffffff"));
                        c1.setText("+");
                        c2.setBackgroundColor(Color.parseColor("#000000"));
                        c2.setTextColor(Color.parseColor("#ffffff"));
                        c2.setText("+");
                        c3.setBackgroundColor(Color.parseColor("#000000"));
                        c3.setTextColor(Color.parseColor("#ffffff"));
                        c3.setText("+");

                        a1back = 0; a1text = 0;
                        a2back = 0; a2text = 0;
                        a3back = 0; a3text = 0;
                        b1back = 0; b1text = 0;
                        b2back = 0; b2text = 0;
                        b3back = 0; b3text = 0;
                        c1back = 0; c1text = 0;
                        c2back = 0; c2text = 0;
                        c3back = 0; c3text = 0;

                        backTemp = 0; textTemp = Color.parseColor("#ffffff");

                        sleepID = 0;
                        powerID = 0;
                        gestureID = 0;
                        voiceID = 0;

                        a1timezoneID = 0; a1formatID = 0; a1format = null; a1timezone = null;
                        a2timezoneID = 0; a2formatID = 0; a2format = null; a2timezone = null;
                        a3timezoneID = 0; a3formatID = 0; a3format = null; a3timezone = null;
                        b1timezoneID = 0; b1formatID = 0; b1format = null; b1timezone = null;
                        b2timezoneID = 0; b2formatID = 0; b2format = null; b2timezone = null;
                        b3timezoneID = 0; b3formatID = 0; b3format = null; b3timezone = null;
                        c1timezoneID = 0; c1formatID = 0; c1format = null; c1timezone = null;
                        c2timezoneID = 0; c2formatID = 0; c2format = null; c2timezone = null;
                        c3timezoneID = 0; c3formatID = 0; c3format = null; c3timezone = null;

                        sleepSelected = null;
                        powerSelected = null;
                        formatSelected = null;
                        timezoneSelected = null;
                        gesturesSelected = null;
                        vcSelected = null;

                        a1String = ""; a1Current = "";
                        a2String = ""; a2Current = "";
                        a3String = ""; a3Current = "";
                        b1String = ""; b1Current = "";
                        b2String = ""; b2Current = "";
                        b3String = ""; b3Current = "";
                        c1String = ""; c1Current = "";
                        c2String = ""; c2Current = "";
                        c3String = ""; c3Current = "";

                        a1city = -1; a1state = "";
                        a2city = -1; a2state = "";
                        a3city = -1; a3state = "";
                        b1city = -1; b1state = "";
                        b2city = -1; b2state = "";
                        b3city = -1; b3state = "";
                        c1city = -1; c1state = "";
                        c2city = -1; c2state = "";
                        c3city = -1; c3state = "";

                        stateSelected = ""; citySelected = "";

                        clearDialog.dismiss();
                    }
                });

                clearDialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Creates the dialog for the textColorPicker
    // The user has 8 options for text color, labeled text1 through text8
    // Each option is a button whose on click listener sets textTemp to the
    //     color chosen and then dismisses the textColorPicker
    public void textColorPicker()
    {
        final Dialog colorPicker = new Dialog(this);

        colorPicker.setContentView(R.layout.text_color_picker);

        text1 = (Button) colorPicker.findViewById(R.id.text1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#ff7903");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text2 = (Button) colorPicker.findViewById(R.id.text2);
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#ffbc03");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text3 = (Button) colorPicker.findViewById(R.id.text3);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#29ff03");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text4 = (Button) colorPicker.findViewById(R.id.text4);
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#03ffd5");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text5 = (Button) colorPicker.findViewById(R.id.text5);
        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#929aff");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text6 = (Button) colorPicker.findViewById(R.id.text6);
        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#c655ff");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text7 = (Button) colorPicker.findViewById(R.id.text7);
        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#ff0019");
                textColor.setBackgroundColor(textTemp);
                colorPicker.dismiss();
            }
        });

        text8 = (Button) colorPicker.findViewById(R.id.text8);
        text8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTemp = Color.parseColor("#ffffff");
                textColor.setBackgroundResource(R.drawable.button);
                colorPicker.dismiss();
            }
        });

        colorPicker.show();

        //return textTemp;
    }

    // Creates the dialog for the backgroundColorPicker
    // The user has 8 options for background color, labeled back1 through back8
    // Each option is a button whose on click listener sets backTemp to the
    //     color chosen and then dismisses the textColorPicker
    public void backColorPicker()
    {
        final Dialog colorPicker = new Dialog(this);

        colorPicker.setContentView(R.layout.back_color_picker);

        back1 = (Button) colorPicker.findViewById(R.id.back1);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#330e01");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back2 = (Button) colorPicker.findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#0b4600");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back3 = (Button) colorPicker.findViewById(R.id.back3);
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#005042");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back4 = (Button) colorPicker.findViewById(R.id.back4);
        back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#15172e");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back5 = (Button) colorPicker.findViewById(R.id.back5);
        back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#290c38");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back6 = (Button) colorPicker.findViewById(R.id.back6);
        back6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#4e032b");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back7 = (Button) colorPicker.findViewById(R.id.back7);
        back7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#4b0007");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        back8 = (Button) colorPicker.findViewById(R.id.back8);
        back8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backTemp = Color.parseColor("#000000");
                backColor.setBackgroundColor(backTemp);
                colorPicker.dismiss();
            }
        });

        colorPicker.show();

        //return backTemp;
    }

    public void colorOptions(final String row, final int column)
    {
        // Create a dialog and set the content to color_options.xml
        final Dialog colorDialog = new Dialog(this);
        colorDialog.setContentView(R.layout.color_options);

        // Register buttons and set the default button for the background color to black
        textColor = (Button) colorDialog.findViewById(R.id.textColorButton);
        backColor = (Button) colorDialog.findViewById(R.id.backColorButton);

        textColor.setBackgroundResource(R.drawable.button);
        backColor.setBackgroundColor(Color.parseColor("#000000"));

        // Determine the panel that is being edited
        //     Set the background of the textColor and backColor buttons
        //     If the background of the textColor button is white, set the
        //       background resource such that the button gets an outline.
        //     Log the color value for debugging purposes
        if(row.equals("A") && column == 1)
        {
            if(a1text != 0)
            {
                textColor.setBackgroundColor(a1text);
                Log.d("A1", "a1text != 0");
            }
            if(a1text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(a1back != 0)
            {
                backColor.setBackgroundColor(a1back);
            }
            textTemp = a1text;
            backTemp = a1back;
            Log.d("A1_ColorOptions", String.valueOf(a1text) + " " + String.valueOf(a1back));
        }
        else if(row.equals("A") && column == 2)
        {
            if(a2text != 0)
            {
                textColor.setBackgroundColor(a2text);
            }
            if(a2text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(a2back != 0)
            {
                backColor.setBackgroundColor(a2back);
            }
            textTemp = a2text;
            backTemp = a2back;
            Log.d("A2_ColorOptions", String.valueOf(a2text) + " " + String.valueOf(a2back));
        }
        else if(row.equals("A") && column == 3)
        {
            if(a3text != 0)
            {
                textColor.setBackgroundColor(a3text);
            }
            if(a3text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(a3back != 0)
            {
                backColor.setBackgroundColor(a3back);
            }
            textTemp = a3text;
            backTemp = a3back;
            Log.d("A3_ColorOptions", String.valueOf(a3text) + " " + String.valueOf(a3back));
        }
        else if(row.equals("B") && column == 1)
        {
            if(b1text != 0)
            {
                textColor.setBackgroundColor(b1text);
            }
            if(b1text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(b1back != 0)
            {
                backColor.setBackgroundColor(b1back);
            }
            textTemp = b1text;
            backTemp = b1back;
            Log.d("B1_ColorOptions", String.valueOf(b1text) + " " + String.valueOf(b1back));
        }
        else if(row.equals("B") && column == 2)
        {
            if(b2text != 0)
            {
                textColor.setBackgroundColor(b2text);
            }
            if(b2text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(b2back != 0)
            {
                backColor.setBackgroundColor(b2back);
            }
            textTemp = b2text;
            backTemp = b2back;
            Log.d("B2_ColorOptions", String.valueOf(b2text) + " " + String.valueOf(b2back));
        }
        else if(row.equals("B") && column == 3)
        {
            if(b3text != 0)
            {
                textColor.setBackgroundColor(b3text);
            }
            if(b3text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(b3back != 0)
            {
                backColor.setBackgroundColor(b3back);
            }
            textTemp = b3text;
            backTemp = b3back;
            Log.d("B3_ColorOptions", String.valueOf(b3text) + " " + String.valueOf(b3back));
        }
        else if(row.equals("C") && column == 1)
        {
            if(c1text != 0)
            {
                textColor.setBackgroundColor(c1text);
            }
            if(c1text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(c1back != 0)
            {
                backColor.setBackgroundColor(c1back);
            }
            textTemp = c1text;
            backTemp = c1back;
            Log.d("C1_ColorOptions", String.valueOf(c1text) + " " + String.valueOf(c1back));
        }
        else if(row.equals("C") && column == 2)
        {
            if(c2text != 0)
            {
                textColor.setBackgroundColor(c2text);
            }
            if(c2text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(c2back != 0)
            {
                backColor.setBackgroundColor(c2back);
            }
            textTemp = c2text;
            backTemp = c2back;
            Log.d("C2_ColorOptions", String.valueOf(c2text) + " " + String.valueOf(c2back));
        }
        else if(row.equals("C") && column == 3)
        {
            if(c3text != 0)
            {
                textColor.setBackgroundColor(c3text);
            }
            if(c3text == -1)
            {
                textColor.setBackgroundResource(R.drawable.button);
            }
            if(c3back != 0)
            {
                backColor.setBackgroundColor(c3back);
            }
            textTemp = c3text;
            backTemp = c3back;
            Log.d("C3_ColorOptions", String.valueOf(c3text) + " " + String.valueOf(c3back));
        }
        else
        {
            Log.d("CRAP", "Why are you here?");
        }

        // If the user selects the textTemp button, run the code to change
        // the color. Save the new color in textTemp.
        // Log the color for debugging purposes.
        textColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textColorPicker();
                Log.d("textTemp", String.valueOf(textTemp));
            }
        });

        // If the user selects the backTemp button, run the code to change
        // the color. Store the new color in textTemp.
        // Log the color for debugging purposes.
        backColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backColorPicker();
                Log.d("backTemp", String.valueOf(backTemp));
            }
        });

        // Register the cancel button. If the user selects cancel,
        // dismiss the colorDialog
        colorCancel = (Button) colorDialog.findViewById(R.id.colorCancel);
        colorCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorDialog.dismiss();
            }
        });

        // Register the ok button. If the user selects ok, store the
        // color in textTemp to the appropriate panel color variable.
        //
        // Example:
        //     a1text - stores the text color for the a1 panel.
        //     c3back - stores the background color for the c3 panel.
        //
        // Dismiss the colorDialog when finished.
        // Log colors for debugging purposes.
        colorOk = (Button) colorDialog.findViewById(R.id.colorOk);

        colorOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(row.equals("A") && column == 1)
                {
                    a1text = textTemp;
                    a1.setTextColor(a1text);
                    a1back = backTemp;
                    a1.setBackgroundColor(a1back);
                    colorDialog.dismiss();
                }
                else if(row.equals("A") && column == 2)
                {
                    a2text = textTemp;
                    a2.setTextColor(a2text);
                    a2back = backTemp;
                    a2.setBackgroundColor(a2back);
                    colorDialog.dismiss();
                }
                else if(row.equals("A") && column == 3)
                {
                    a3text = textTemp;
                    a3.setTextColor(a3text);
                    a3back = backTemp;
                    a3.setBackgroundColor(a3back);
                    colorDialog.dismiss();
                }
                else if(row.equals("B") && column == 1)
                {
                    b1text = textTemp;
                    b1.setTextColor(b1text);
                    b1back = backTemp;
                    b1.setBackgroundColor(b1back);
                    colorDialog.dismiss();
                }
                else if(row.equals("B") && column == 2)
                {
                    b2text = textTemp;
                    b2.setTextColor(b2text);
                    b2back = backTemp;
                    b2.setBackgroundColor(b2back);
                    colorDialog.dismiss();
                }
                else if(row.equals("B") && column == 3)
                {
                    b3text = textTemp;
                    b3.setTextColor(b3text);
                    b3back = backTemp;
                    b3.setBackgroundColor(b3back);
                    colorDialog.dismiss();
                }
                else if(row.equals("C") && column == 1)
                {
                    c1text = textTemp;
                    c1.setTextColor(c1text);
                    c1back = backTemp;
                    c1.setBackgroundColor(c1back);
                    colorDialog.dismiss();
                }
                else if(row.equals("C") && column == 2)
                {
                    c2text = textTemp;
                    c2.setTextColor(c2text);
                    c2back = backTemp;
                    c2.setBackgroundColor(c2back);
                    colorDialog.dismiss();
                }
                else if(row.equals("C") && column == 3)
                {
                    c3text = textTemp;
                    c3.setTextColor(c3text);
                    c3back = backTemp;
                    c3.setBackgroundColor(c3back);
                    colorDialog.dismiss();
                }
                else
                {
                    Log.d("CRAP", "Why are you here?");
                }
            }
        });

        // If the text color is white, surround the textColor
        // button with a black outline.
        if(textTemp == -1)
        {
            textColor.setBackgroundResource(R.drawable.button);
        }

        // Display the dialog.
        colorDialog.show();

        //backTemp = 0; textTemp = Color.parseColor("#ffffff");
    }

    public void dismissOption(String rowFinal, int columnFinal, String current)
    {
        if(rowFinal.equals("A") && columnFinal == 1)
        {
            a1.setText(current);
        }
        else if(rowFinal.equals("A") && columnFinal == 2)
        {
            a2.setText(current);
        }
        else if(rowFinal.equals("A") && columnFinal == 3)
        {
            a3.setText(current);
        }
        else if(rowFinal.equals("B") && columnFinal == 1)
        {
            b1.setText(current);
        }
        else if(rowFinal.equals("B") && columnFinal == 2)
        {
            b2.setText(current);
        }
        else if(rowFinal.equals("B") && columnFinal == 3)
        {
            b3.setText(current);
        }
        else if(rowFinal.equals("C") && columnFinal == 1)
        {
            c1.setText(current);
        }
        else if(rowFinal.equals("C") && columnFinal == 2)
        {
            c2.setText(current);
        }
        else if(rowFinal.equals("C") && columnFinal == 3)
        {
            c3.setText(current);
        }
    }

    public void clockOptions(String row, int column, Boolean longC, final String currentSetting)
    {
        final String rowFinal = row;
        final int columnFinal = column;
        final Boolean longClick = longC;

        // Create a dialog and set the content to clock_options.xml
        final Dialog clockDialog = new Dialog(this);
        clockDialog.setContentView(R.layout.clock_options);

        // Create an array list of clock formats (formatClockList)
        List<String> formatClockList = new ArrayList<>();
        formatClockList.add("Standard");
        formatClockList.add("Military");

        // Create a radio group and link to clockFormatGroup in clock_options.xml
        // For each item in the formatClockList:
        //     Create a radio button
        //     Set the ID and text of the button.
        //     Add the radio button the the radio group
        //     If the ID of the format previously selected is the current id,
        //       toggle the radio button such that it is checked.
        final RadioGroup format = (RadioGroup) clockDialog.findViewById(R.id.clockFormatGroup);
        for (int i=0; i<formatClockList.size(); i++)
        {
            RadioButton formatRadio = new RadioButton(this);
            formatRadio.setText(formatClockList.get(i));
            formatRadio.setId(i+1);
            format.addView(formatRadio);
            if(row.equals("A") && column == 1)
            {
                if(a1format != null && i == (a1format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(a1formatID != 0 && i == a1formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("A") && column == 2)
            {
                if(a2format != null && i == (a2format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(a2formatID != 0 && i == a2formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("A") && column == 3)
            {
                if(a3format != null && i == (a3format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(a3formatID != 0 && i == a3formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("B") && column == 1)
            {
                if(b1format != null && i == (b1format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(b1formatID != 0 && i == b1formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("B") && column == 2)
            {
                if(b2format != null && i == (b2format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(b2formatID != 0 && i == b2formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("B") && column == 3)
            {
                if(b3format != null && i == (b3format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(b3formatID != 0 && i == b3formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("C") && column == 1)
            {
                if(c1format != null && i == (c1format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(c1formatID != 0 && i == c1formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("C") && column == 2)
            {
                if(c2format != null && i == (c2format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(c2formatID != 0 && i == c2formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else if(row.equals("C") && column == 3)
            {
                if(c3format != null && i == (c3format.getId()-1))
                {
                    formatRadio.toggle();
                }
                else if(c3formatID != 0 && i == c3formatID-1)
                {
                    formatRadio.toggle();
                }
            }
            else
            {
                Log.d("CRAP", "Why are you here?");
            }
        }

        // Create an array list of timezones
        List<String> timezoneList = new ArrayList<>();
        timezoneList.add("Eastern");
        timezoneList.add("Central");
        timezoneList.add("Mountain");
        timezoneList.add("Mountain (No DST)");
        timezoneList.add("Pacific");
        timezoneList.add("Alaska");
        timezoneList.add("Hawaii-Aleutian");
        timezoneList.add("Hawaii-Aleutian (No DST)");

        final RadioGroup timezone = (RadioGroup) clockDialog.findViewById(R.id.clockTimezoneGroup);
        for (int i = 0; i<timezoneList.size(); i++)
        {
            RadioButton zoneRadio = new RadioButton(this);
            zoneRadio.setText(timezoneList.get(i));
            zoneRadio.setId(i+1);
            timezone.addView(zoneRadio);

            if(row.equals("A") && column == 1)
            {
                if(a1timezone != null && i == (a1timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(a1timezoneID != 0 && i == a1timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("A") && column == 2)
            {
                if(a2timezone != null && i == (a2timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(a2timezoneID != 0 && i == a2timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("A") && column == 3)
            {
                if(a3timezone != null && i == (a3timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(a3timezoneID != 0 && i == a3timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("B") && column == 1)
            {
                if(b1timezone != null && i == (b1timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(b1timezoneID != 0 && i == b1timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("B") && column == 2)
            {
                if(b2timezone != null && i == (b2timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(b2timezoneID != 0 && i == b2timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("B") && column == 3)
            {
                if(b3timezone != null && i == (b3timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(b3timezoneID != 0 && i == b3timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("C") && column == 1)
            {
                if(c1timezone != null && i == (c1timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(c1timezoneID != 0 && i == c1timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("C") && column == 2)
            {
                if(c2timezone != null && i == (c2timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(c2timezoneID != 0 && i == c2timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else if(row.equals("C") && column == 3)
            {
                if(c3timezone != null && i == (c3timezone.getId()-1))
                {
                    zoneRadio.toggle();
                }
                else if(c2timezoneID != 0 && i == c2timezoneID-1)
                {
                    zoneRadio.toggle();
                }
            }
            else
            {
                Log.d("CRAP", "Why are you here?");
            }

        }

        clockCancel = (Button) clockDialog.findViewById(R.id.clockCancel);
        clockCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!longClick)
                {
                    dismissOption(rowFinal, columnFinal, currentSetting);
                }
                clockDialog.dismiss();
            }
        });

        clockOk = (Button) clockDialog.findViewById(R.id.clockOk);
        clockOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int formatID = format.getCheckedRadioButtonId();
                int timezoneID = timezone.getCheckedRadioButtonId();

                String toastStr = "";

                if(formatID == 1)
                {
                    Log.d("ID", String.valueOf(formatID));
                    toastStr = toastStr + "Standard ";
                }
                else if (formatID == 2)
                {
                    Log.d("ID", String.valueOf(formatID));
                    toastStr = toastStr + "Military ";
                }
                else
                {
                    Log.d("ID", String.valueOf(formatID));
                }

                if(timezoneID == 1)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Eastern Time Selected";
                }
                else if(timezoneID == 2)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Central Time Selected";
                }
                else if(timezoneID == 3)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Mountain Time Selected";
                }
                else if(timezoneID == 4)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Mountain Time (No DST) Selected";
                }
                else if(timezoneID == 5)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Pacific Time Selected";
                }
                else if(timezoneID == 6)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Alaskan Time Selected";
                }
                else if(timezoneID == 7)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Hawaii-Aleutian Time Selected";
                }
                else if(timezoneID == 8)
                {
                    Log.d("ID", String.valueOf(timezoneID));
                    toastStr = toastStr + "- Hawaii-Aleutian Time (No DST) Selected";
                }
                else
                {
                    Log.d("ID", String.valueOf(timezoneID));
                }

                formatSelected = (RadioButton) clockDialog.findViewById(formatID);
                timezoneSelected = (RadioButton) clockDialog.findViewById(timezoneID);

                if(rowFinal.equals("A") && columnFinal == 1)
                {
                    if(formatSelected != null)
                    {
                        a1format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        a1timezone = timezoneSelected;
                    }
                    if(a1format != null)
                    {
                        a1formatID = a1format.getId();
                    }
                    if(a1timezone != null)
                    {
                        a1timezoneID = a1timezone.getId();
                    }
                }
                else if(rowFinal.equals("A") && columnFinal == 2)
                {
                    if(formatSelected != null)
                    {
                        a2format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        a2timezone = timezoneSelected;
                    }
                    if(a2format != null)
                    {
                        a2formatID = a2format.getId();
                    }
                    if(a2timezone != null)
                    {
                        a2timezoneID = a2timezone.getId();
                    }
                }
                else if(rowFinal.equals("A") && columnFinal == 3)
                {
                    if(formatSelected != null)
                    {
                        a3format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        a3timezone = timezoneSelected;
                    }
                    if(a3format != null)
                    {
                        a3formatID = a3format.getId();
                    }
                    if(a3timezone != null)
                    {
                        a3timezoneID = a3timezone.getId();
                    }
                }
                else if(rowFinal.equals("B") && columnFinal == 1)
                {
                    if(formatSelected != null)
                    {
                        b1format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        b1timezone = timezoneSelected;
                    }
                    if(b1format != null)
                    {
                        b1formatID = b1format.getId();
                    }
                    if(b1timezone != null)
                    {
                        b1timezoneID = b1timezone.getId();
                    }
                }
                else if(rowFinal.equals("B") && columnFinal == 2)
                {
                    if(formatSelected != null)
                    {
                        b2format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        b2timezone = timezoneSelected;
                    }
                    if(b2format != null)
                    {
                        b2formatID = b2format.getId();
                    }
                    if(b2timezone != null)
                    {
                        b2timezoneID = b2timezone.getId();
                    }
                }
                else if(rowFinal.equals("B") && columnFinal == 3)
                {
                    if(formatSelected != null)
                    {
                        b3format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        b3timezone = timezoneSelected;
                    }
                    if(b3format != null)
                    {
                        b3formatID = b3format.getId();
                    }
                    if(b3timezone != null)
                    {
                        b3timezoneID = b3timezone.getId();
                    }
                }
                else if(rowFinal.equals("C") && columnFinal == 1)
                {
                    if(formatSelected != null)
                    {
                        c1format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        c1timezone = timezoneSelected;
                    }
                    if(c1format != null)
                    {
                        c1formatID = c1format.getId();
                    }
                    if(c1timezone != null)
                    {
                        c1timezoneID = c1timezone.getId();
                    }
                }
                else if(rowFinal.equals("C") && columnFinal == 2)
                {
                    if(formatSelected != null)
                    {
                        c2format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        c2timezone = timezoneSelected;
                    }
                    if(c2format != null)
                    {
                        c2formatID = c2format.getId();
                    }
                    if(c2timezone != null)
                    {
                        c2timezoneID = c2timezone.getId();
                    }
                }
                else if(rowFinal.equals("C") && columnFinal == 3)
                {
                    if(formatSelected != null)
                    {
                        c3format = formatSelected;
                    }
                    if(timezoneSelected != null)
                    {
                        c3timezone = timezoneSelected;
                    }
                    if(c3format != null)
                    {
                        c3formatID = c3format.getId();
                    }
                    if(c3timezone != null)
                    {
                        c3timezoneID = c3timezone.getId();
                    }
                }
                else
                {
                    Log.d("CRAP", "Why are you here?");
                }

                // Make sure the user selects one radio button in each group
                if(formatSelected != null && timezoneSelected != null)
                {
                    Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
                    clockDialog.dismiss();
                }
                else if(formatSelected == null && timezoneSelected != null)
                {
                    Toast.makeText(getApplicationContext(), "Please Select Format", Toast.LENGTH_LONG).show();
                }
                else if(formatSelected != null && timezoneSelected == null)
                {
                    Toast.makeText(getApplicationContext(), "Please Select Timezone", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Select Time Settings", Toast.LENGTH_LONG).show();
                }

            }
        });

        clockDialog.show();
    }

    public void weatherOptions(final String row, final int column, final Boolean longC, final String currentSetting)
    {
        final Dialog weatherDialog = new Dialog(this);
        weatherDialog.setContentView(R.layout.weather_options);

        stateSpinner = (Spinner) weatherDialog.findViewById(R.id.stateSpinner);
        citySpinner = (Spinner) weatherDialog.findViewById(R.id.citySpinner);
        cityText = (TextView) weatherDialog.findViewById(R.id.cityText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.states_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);

        stateConverter statec = null;

        if(row.equals("A") && column == 1)
        {
            statec = new stateConverter(a1state);
        }
        else if(row.equals("A") && column == 2)
        {
            statec = new stateConverter(a2state);
        }
        else if(row.equals("A") && column == 3)
        {
            statec = new stateConverter(a3state);
        }
        else if(row.equals("B") && column == 1)
        {
            statec = new stateConverter(b1state);
        }
        else if(row.equals("B") && column == 2)
        {
            statec = new stateConverter(b2state);
        }
        else if(row.equals("B") && column == 3)
        {
            statec = new stateConverter(b3state);
        }
        else if(row.equals("C") && column == 1)
        {
            statec = new stateConverter(c1state);
        }
        else if(row.equals("C") && column == 2)
        {
            statec = new stateConverter(c2state);
        }
        else if(row.equals("C") && column == 3)
        {
            statec = new stateConverter(c3state);
        }
        else
        {
            Log.d("CRAP", "How did I get here?");
        }

        stateSpinner.setAdapter(adapter);
        if(statec != null)
        {
            stateSpinner.setSelection(statec.getRelativeId());
        }
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state = stateSpinner.getSelectedItem().toString();
                stateConverter sc = new stateConverter(state);

                final Cursor c = citydb.getCitiesByState(sc.getId());

                CursorAdapter cAdapt = new CursorAdapter(getApplicationContext(), c, 0) {
                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        return LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
                        //return null;
                    }

                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        TextView textWeather = (TextView) view.findViewById(R.id.textWeather);
                        String city = cursor.getString(cursor.getColumnIndexOrThrow("City"));
                        if(city.startsWith("\""))
                        {
                            city = city.substring(1);
                        }
                        if(city.endsWith("\""))
                        {
                            city = city.substring(0, city.length()-1);
                        }
                        textWeather.setText(city);
                    }
                };

                int savedCityId = -1;
                if(row.equals("A") && column == 1)
                {
                    savedCityId = a1city;
                }
                else if(row.equals("A") && column == 2)
                {
                    savedCityId = a2city;
                }
                else if(row.equals("A") && column == 3)
                {
                    savedCityId = a3city;
                }
                else if(row.equals("B") && column == 1)
                {
                    savedCityId = b1city;
                }
                else if(row.equals("B") && column == 2)
                {
                    savedCityId = b2city;
                }
                else if(row.equals("B") && column == 3)
                {
                    savedCityId = b3city;
                }
                else if(row.equals("C") && column == 1)
                {
                    savedCityId = c1city;
                }
                else if(row.equals("C") && column == 2)
                {
                    savedCityId = c2city;
                }
                else if(row.equals("C") && column == 3)
                {
                    savedCityId = c3city;
                }
                else
                {
                    Log.d("CRAP", "How did I get here?");
                }

                citySpinner.setAdapter(cAdapt);

                // BUG
                if(sc.getId() != stateSelected)
                {
                    savedCityId = -1;
                }

                if(savedCityId != -1)
                {
                    citySpinner.setSelection(savedCityId);
                }

                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //String cityid = c.getString((int)citySpinner.getSelectedItemId());
                        int cid = c.getPosition();
                        if(row.equals("A") && column == 1)
                        {
                            a1city = cid;
                        }
                        else if(row.equals("A") && column == 2)
                        {
                            a2city = cid;
                        }
                        else if(row.equals("A") && column == 3)
                        {
                            a3city = cid;
                        }
                        else if(row.equals("B") && column == 1)
                        {
                            b1city = cid;
                        }
                        else if(row.equals("B") && column == 2)
                        {
                            b2city = cid;
                        }
                        else if(row.equals("B") && column == 3)
                        {
                            b3city = cid;
                        }
                        else if(row.equals("C") && column == 1)
                        {
                            c1city = cid;
                        }
                        else if(row.equals("C") && column == 2)
                        {
                            c2city = cid;
                        }
                        else if(row.equals("C") && column == 3)
                        {
                            c3city = cid;
                        }
                        else
                        {
                            Log.d("CRAP", "How did I get here?");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // Do Nothing
                   }
                });

                //SimpleCursorAdapter cityAdapter = new SimpleCursorAdapter(getApplicationContext(),
                //       android.R.layout.simple_spinner_dropdown_item, c,
                //        new String[] {myCitiesSQLiteHelper.KEY_CITY},
                //        new int[] {R.id.textWeather}, 0);

                //cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //citySpinner.setAdapter(cityAdapter);

                if(!stateSpinner.getSelectedItem().toString().equals("Select State"))
                {
                    cityText.setVisibility(View.VISIBLE);
                    citySpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cityText.setVisibility(View.INVISIBLE);
                citySpinner.setVisibility(View.INVISIBLE);
            }
        });

        weatherCancel = (Button) weatherDialog.findViewById(R.id.weatherCancel);
        weatherCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                if(!longC)
                {
                    dismissOption(row, column, currentSetting);
                }
                weatherDialog.dismiss();
            }
        });

        weatherOk = (Button) weatherDialog.findViewById(R.id.weatherOk);
        weatherOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                citySelected = citySpinner.getSelectedItem().toString();
                stateSelected = stateSpinner.getSelectedItem().toString();

                if(citySelected != null && stateSelected != null)
                {
                    Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    weatherDialog.dismiss();
                }
                else if(citySelected == null && stateSelected != null)
                {
                    Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_LONG).show();
                }
                else if(citySelected != null)
                {
                    Toast.makeText(getApplicationContext(), "Please Select State", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Select Weather Settings", Toast.LENGTH_LONG).show();
                }

                if(row.equals("A") && column == 1)
                {
                    a1state = stateSelected;
                }
                else if(row.equals("A") && column == 2)
                {
                    a2state = stateSelected;
                }
                else if(row.equals("A") && column == 3)
                {
                    a3state = stateSelected;
                }
                else if(row.equals("B") && column == 1)
                {
                    b1state = stateSelected;
                }
                else if(row.equals("B") && column == 2)
                {
                    b2state = stateSelected;
                }
                else if(row.equals("B") && column == 3)
                {
                    b3state = stateSelected;
                }
                else if(row.equals("C") && column == 1)
                {
                    c1state = stateSelected;
                }
                else if(row.equals("C") && column == 2)
                {
                    c2state = stateSelected;
                }
                else if(row.equals("C") && column == 3)
                {
                    c3state = stateSelected;
                }
                else
                {
                    Log.d("CRAP", "How did I get here?");
                }
            }
        });

        weatherDialog.show();
    }

    public void reminderOptions(String row, int column, Boolean longC, final String currentSetting)
    {
        final String rowFinal = row;
        final int columnFinal = column;
        final Boolean longClick = longC;

        // Create a dialog and set the content to clock_options.xml
        final Dialog reminderDialog = new Dialog(this);
        final Dialog addReminderDialog = new Dialog(this);
        final Dialog timePickDialog = new Dialog(this);
        final Dialog datePickDialog = new Dialog(this);

        reminderDialog.setContentView(R.layout.reminder_options);
        addReminderDialog.setContentView(R.layout.add_reminder);
        timePickDialog.setContentView(R.layout.time_pick);
        datePickDialog.setContentView(R.layout.date_pick);

        timePick = (TimePicker) timePickDialog.findViewById(R.id.timePicker);
        timePicked = (TextView) addReminderDialog.findViewById(R.id.timePicked);
        reminderText = (EditText) addReminderDialog.findViewById(R.id.reminderText);
        datePick = (DatePicker) datePickDialog.findViewById(R.id.datePicker);
        datePicked = (TextView) addReminderDialog.findViewById(R.id.datePicked);

        //final ReminderDatabase db;
        //final SimpleCursorAdapter dataAdapter;

        db = new ReminderDatabase(this);
        db.open();

        Cursor c = db.getAllReminders();

        if (c == null) {
            Log.i("CAA", "cursor is null...");
        }

        // The desired columns to be bound
        final String[] columns = new String[]{
                mySQLiteHelper.KEY_DATE,
                mySQLiteHelper.KEY_TIME,
                mySQLiteHelper.KEY_TITLE
        };

        // the XML defined views which the data will be bound to
        final int[] to = new int[]{
                R.id.date,
                R.id.time,
                R.id.title
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.reminder, c, columns, to, 0);

        final ListView listView = (ListView) reminderDialog.findViewById(R.id.reminderList);
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lView, View view,
                                    int position, final long id) {

                Log.d("CURRENT TIME", db.getTime(id));
                timePicked.setText(db.getTime(id));

                Log.d("CURRENT DATE", db.getDate(id));
                datePicked.setText(db.getDate(id));

                Log.d("CURRENT TITLE", db.getTitle(id));
                reminderText.setText(db.getTitle(id));

                timeButton = (Button) addReminderDialog.findViewById(R.id.timeButton);
                timeButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        int hour = 0;
                        int minute = 0;

                        Log.d("CURRENT TIME", db.getTime(id));
                        String time = db.getTime(id);
                        String sHour;
                        String sMinute;
                        String sAmPm;

                        String[] timeBreak = time.split("\\s+");
                        String[] hourMinuteBreak = timeBreak[0].split("[:]");

                        Log.d("TIME SET", timeBreak[0]);

                        sHour = hourMinuteBreak[0];
                        sMinute = hourMinuteBreak[1];
                        sAmPm = timeBreak[1];

                        hour = Integer.parseInt(sHour);
                        minute = Integer.parseInt(sMinute);

                        if(sAmPm.equals("PM"))
                        {
                            hour = hour + 12;
                        }
                        if(sAmPm.equals("AM") && hour == 12)
                        {
                            hour = 0;
                        }

                        timePick.setHour(hour);
                        timePick.setMinute(minute);

                        timePickCancel = (Button) timePickDialog.findViewById(R.id.timePickCancel);
                        timePickCancel.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                timePickDialog.dismiss();
                            }
                        });

                        timePickOk = (Button) timePickDialog.findViewById(R.id.timePickOk);
                        timePickOk.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                int militaryHour = timePick.getHour();
                                String ampm;

                                if(militaryHour < 12)
                                {
                                    ampm = " AM";
                                }
                                else
                                {
                                    ampm = " PM";
                                }

                                militaryHour = militaryHour % 12;
                                if(militaryHour == 0)
                                {
                                    militaryHour = 12;
                                }
                                String hour = Integer.toString(militaryHour);
                                if(militaryHour < 10)
                                {
                                    hour = "0" + hour;
                                }

                                int mins = timePick.getMinute();
                                String minutes = Integer.toString(mins);
                                if(mins < 10)
                                {
                                    minutes = "0" + minutes;
                                }

                                rTime = "";
                                rTime = hour + ":" + minutes + ampm;

                                timePicked.setText(rTime);

                                timePickDialog.dismiss();
                            }
                        });

                        timePickDialog.show();
                    }
                });

                dateButton = (Button) addReminderDialog.findViewById(R.id.dateButton);
                dateButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        datePickCancel = (Button) datePickDialog.findViewById(R.id.datePickCancel);
                        datePickCancel.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                datePickDialog.dismiss();
                            }
                        });

                        datePickOk = (Button) datePickDialog.findViewById(R.id.datePickOk);
                        datePickOk.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                int month = datePick.getMonth() + 1;
                                int day = datePick.getDayOfMonth();
                                int year = datePick.getYear();

                                String date = month + "/" + day + "/" + year;
                                datePicked.setText(date);

                                datePickDialog.dismiss();
                            }
                        });

                        datePickDialog.show();
                    }
                });

                addRCancel = (Button) addReminderDialog.findViewById(R.id.reminderCancel);
                addRCancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        addReminderDialog.dismiss();
                    }
                });

                addRDelete = (Button) addReminderDialog.findViewById(R.id.reminderDelete);
                addRDelete.setVisibility(View.VISIBLE);
                addRDelete.setClickable(true);
                addRDelete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        db.removeReminder(id);
                        addReminderDialog.dismiss();
                        Cursor cursor = db.getAllReminders();
                        dataAdapter.changeCursor(cursor);
                    }
                });

                addROk = (Button) addReminderDialog.findViewById(R.id.reminderOk);
                addROk.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        appendthis("Start of database code.");

                        int rmonth = datePick.getMonth() + 1;
                        int rday = datePick.getDayOfMonth();
                        int ryear = datePick.getYear();

                        int militaryHour = timePick.getHour();
                        String ampm;
                        if(militaryHour < 12)
                        {
                            ampm = " AM";
                        }
                        else
                        {
                            ampm = " PM";
                        }

                        militaryHour = militaryHour % 12;
                        if (militaryHour == 0)
                        {
                            militaryHour = 12;
                        }
                        String hour = Integer.toString(militaryHour);
                        if(militaryHour < 10)
                        {
                            hour = "0" + hour;
                        }

                        int mins = timePick.getMinute();
                        String minutes = Integer.toString(mins);
                        if(mins < 10)
                        {
                            minutes = "0" + minutes;
                        }

                        rDate = "";
                        rTime = "";
                        rText = "";

                        rDate = rmonth + "/" + rday + "/" + ryear;
                        rTime = hour + ":" + minutes + ampm;
                        rText = reminderText.getText().toString();

                        db.removeReminder(id);
                        db.insertReminder(rDate, rTime, rText);

                        Cursor cursor = db.getAllReminders();
                        dataAdapter.changeCursor(cursor);

                        addReminderDialog.dismiss();

                    }
                });

                addReminderDialog.show();
            }
        });

        addReminder = (Button) reminderDialog.findViewById(R.id.addReminder);
        addReminder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                reminderText.setText("");

                timePicked.setText(R.string.startTime);
                timePick.setHour(0);
                timePick.setMinute(0);

                String date = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
                datePicked.setText(date);

                timeButton = (Button) addReminderDialog.findViewById(R.id.timeButton);
                timeButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        int hour = 0;
                        int minute = 0;

                        String[] hourMinuteBreak = timePicked.getText().toString().split("[:]");
                        String[] minuteBreak = hourMinuteBreak[1].split("\\s+");

                        hour = Integer.parseInt(hourMinuteBreak[0]);
                        minute = Integer.parseInt(minuteBreak[0]);
                        String sAmPm = minuteBreak[1];

                        Log.d("DEBUG", sAmPm);

                        if(sAmPm.equals("PM"))
                        {
                            hour = hour + 12;
                        }
                        if(sAmPm.equals("AM") && hour == 12)
                        {
                            hour = 0;
                        }

                        timePick.setHour(hour);
                        timePick.setMinute(minute);

                        timePickCancel = (Button) timePickDialog.findViewById(R.id.timePickCancel);
                        timePickCancel.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                timePickDialog.dismiss();
                            }
                        });

                        timePickOk = (Button) timePickDialog.findViewById(R.id.timePickOk);
                        timePickOk.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                int militaryHour = timePick.getHour();
                                String ampm;

                                if(militaryHour < 12)
                                {
                                    ampm = " AM";
                                }
                                else
                                {
                                    ampm = " PM";
                                }

                                militaryHour = militaryHour % 12;
                                if(militaryHour == 0)
                                {
                                    militaryHour = 12;
                                }
                                String hour = Integer.toString(militaryHour);
                                if(militaryHour < 10)
                                {
                                    hour = "0" + hour;
                                }

                                int mins = timePick.getMinute();
                                String minutes = Integer.toString(mins);
                                if(mins < 10)
                                {
                                    minutes = "0" + minutes;
                                }

                                rTime = "";
                                rTime = hour + ":" + minutes + ampm;

                                timePicked.setText(rTime);

                                timePickDialog.dismiss();
                            }
                        });

                        timePickDialog.show();
                    }
                });

                dateButton = (Button) addReminderDialog.findViewById(R.id.dateButton);
                dateButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        datePickCancel = (Button) datePickDialog.findViewById(R.id.datePickCancel);
                        datePickCancel.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                datePickDialog.dismiss();
                            }
                        });

                        datePickOk = (Button) datePickDialog.findViewById(R.id.datePickOk);
                        datePickOk.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v)
                            {
                                int month = datePick.getMonth() + 1;
                                int day = datePick.getDayOfMonth();
                                int year = datePick.getYear();

                                String date = month + "/" + day + "/" + year;
                                datePicked.setText(date);

                                datePickDialog.dismiss();
                            }
                        });

                        datePickDialog.show();
                    }
                });

                addRCancel = (Button) addReminderDialog.findViewById(R.id.reminderCancel);
                addRCancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        addReminderDialog.dismiss();
                    }
                });

                addRDelete = (Button) addReminderDialog.findViewById(R.id.reminderDelete);
                addRDelete.setVisibility(View.INVISIBLE);

                addROk = (Button) addReminderDialog.findViewById(R.id.reminderOk);
                addROk.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        appendthis("Start of database code.");

                        rText = "";
                        rText = reminderText.getText().toString();

                        rDate = datePicked.getText().toString();

                        db.insertReminder(rDate, rTime, rText);

                        Cursor cursor = db.getAllReminders();
                        dataAdapter.changeCursor(cursor);

                        //dataAdapter.notifyDataSetChanged();
                        //listView.setAdapter(dataAdapter);

                        addReminderDialog.dismiss();

                    }
                });

                addReminderDialog.show();
                // else Toast.makeText(getApplicationContext(), "Please Set at Least One Reminder", Toast.LENGTH_LONG).show();

            }
        });

        reminderCancel = (Button) reminderDialog.findViewById(R.id.reminderCancel);
        reminderCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                if(!longClick)
                {
                    dismissOption(rowFinal, columnFinal, currentSetting);
                }
                reminderDialog.dismiss();
                db.close();
            }
        });

        reminderOk = (Button) reminderDialog.findViewById(R.id.reminderOk);
        reminderOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                reminderDialog.dismiss();
                db.close();
            }
        });

        reminderDialog.show();
    }

    public void alarmOptions(String row, int column, Boolean longC, final String currentSetting)
    {
        final String rowFinal = row;
        final int columnFinal = column;
        final Boolean longClick = longC;

        // Create a dialog and set the content to clock_options.xml
        final Dialog alarmDialog = new Dialog(this);
        final Dialog addAlarmDialog = new Dialog(this);

        alarmDialog.setContentView(R.layout.alarm_options);
        addAlarmDialog.setContentView(R.layout.add_alarm);
        timePick = (TimePicker) addAlarmDialog.findViewById(R.id.timePicker);
        alarmText = (EditText) addAlarmDialog.findViewById(R.id.alarmText);

        adb = new AlarmDatabase(this);
        adb.open();

        Cursor c = adb.getAllAlarms();

        if (c == null) {
            Log.i("CAA", "cursor is null...");
        }

        // The desired columns to be bound
        final String[] columns = new String[]{
                myAlarmSQLiteHelper.KEY_TIME,
                myAlarmSQLiteHelper.KEY_TITLE
        };

        // the XML defined views which the data will be bound to
        final int[] to = new int[]{
                R.id.time,
                R.id.title
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        alarmDataAdapter = new SimpleCursorAdapter(
                this, R.layout.alarm, c, columns, to, 0);

        final ListView listView = (ListView) alarmDialog.findViewById(R.id.alarmList);
        listView.setAdapter(alarmDataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lView, View view,
                                    int position, long id) {

                Log.d("CURRENT TITLE", adb.getTitle(id));
                alarmText.setText(adb.getTitle(id));

                int hour;
                int minute;

                Log.d("CURRENT TIME", adb.getTime(id));
                String time = adb.getTime(id);
                String sHour;
                String sMinute;
                String sAmPm;

                String[] timeBreak = time.split("\\s+");
                String[] hourMinuteBreak = timeBreak[0].split("[:]");

                sHour = hourMinuteBreak[0];
                sMinute = hourMinuteBreak[1];
                sAmPm = timeBreak[1];

                hour = Integer.parseInt(sHour);
                minute = Integer.parseInt(sMinute);

                if(sAmPm.equals("PM"))
                {
                    hour = hour + 12;
                }

                timePick.setHour(hour);
                timePick.setMinute(minute);

                final long dataid = id;

                addACancel = (Button) addAlarmDialog.findViewById(R.id.alarmCancel);
                addACancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        addAlarmDialog.dismiss();
                    }
                });

                addADelete = (Button) addAlarmDialog.findViewById(R.id.alarmDelete);
                addADelete.setVisibility(View.VISIBLE);
                addADelete.setClickable(true);
                addADelete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        adb.removeAlarm(dataid);
                        addAlarmDialog.dismiss();
                        Cursor cursor = adb.getAllAlarms();
                        alarmDataAdapter.changeCursor(cursor);
                    }
                });

                addAOk = (Button) addAlarmDialog.findViewById(R.id.alarmOk);
                addAOk.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        appendthis("Start of database code.");

                        int militaryHour = timePick.getHour();
                        String ampm;
                        if(militaryHour < 12)
                        {
                            ampm = " AM";
                        }
                        else
                        {
                            ampm = " PM";
                        }

                        militaryHour = militaryHour % 12;
                        if (militaryHour == 0)
                        {
                            militaryHour = 12;
                        }
                        String hour = Integer.toString(militaryHour);
                        if(militaryHour < 10)
                        {
                            hour = "0" + hour;
                        }

                        int mins = timePick.getMinute();
                        String minutes = Integer.toString(mins);
                        if(mins < 10)
                        {
                            minutes = "0" + minutes;
                        }

                        aTime = "";
                        aText = "";

                        aTime = hour + ":" + minutes + ampm;
                        aText = alarmText.getText().toString();

                        adb.removeAlarm(dataid);
                        adb.insertAlarm(aTime, aText);

                        Cursor cursor = adb.getAllAlarms();
                        alarmDataAdapter.changeCursor(cursor);

                        //dataAdapter.notifyDataSetChanged();
                        //listView.setAdapter(dataAdapter);

                        addAlarmDialog.dismiss();

                    }
                });

                addAlarmDialog.show();


                // display the name in a toast.
                //String time = cursor.getString(cursor.getColumnIndexOrThrow(mySQLiteHelper.KEY_TIME));
                //Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();

            }
        });

        addAlarm = (Button) alarmDialog.findViewById(R.id.addAlarm);
        addAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                alarmText.setText("");

                int hour = 0;
                int minute = 0;

                timePick.setHour(hour);
                timePick.setMinute(minute);

                addACancel = (Button) addAlarmDialog.findViewById(R.id.alarmCancel);
                addACancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        addAlarmDialog.dismiss();
                    }
                });

                addADelete = (Button) addAlarmDialog.findViewById(R.id.alarmDelete);
                addADelete.setVisibility(View.INVISIBLE);

                addAOk = (Button) addAlarmDialog.findViewById(R.id.alarmOk);
                addAOk.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        appendthis("Start of database code.");

                        int militaryHour = timePick.getHour();
                        String ampm;
                        if(militaryHour < 12)
                        {
                            ampm = " AM";
                        }
                        else
                        {
                            ampm = " PM";
                        }

                        militaryHour = militaryHour % 12;
                        if(militaryHour == 0)
                        {
                            militaryHour = 12;
                        }
                        String hour = Integer.toString(militaryHour);
                        if(militaryHour < 10)
                        {
                            hour = "0" + hour;
                        }

                        int mins = timePick.getMinute();
                        String minutes = Integer.toString(mins);
                        if(mins < 10)
                        {
                            minutes = "0" + minutes;
                        }

                        aTime = "";
                        aText = "";

                        aTime = hour + ":" + minutes + ampm;
                        aText = alarmText.getText().toString();

                        adb.insertAlarm(aTime, aText);

                        Cursor cursor = adb.getAllAlarms();
                        alarmDataAdapter.changeCursor(cursor);

                        //dataAdapter.notifyDataSetChanged();
                        //listView.setAdapter(dataAdapter);

                        addAlarmDialog.dismiss();

                    }
                });

                addAlarmDialog.show();
                // else Toast.makeText(getApplicationContext(), "Please Set at Least One Reminder", Toast.LENGTH_LONG).show();

            }
        });

        alarmCancel = (Button) alarmDialog.findViewById(R.id.alarmCancel);
        alarmCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                if(!longClick)
                {
                    dismissOption(rowFinal, columnFinal, currentSetting);
                }
                alarmDialog.dismiss();
                adb.close();
            }
        });

        alarmOk = (Button) alarmDialog.findViewById(R.id.alarmOk);
        alarmOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                alarmDialog.dismiss();
                adb.close();
            }
        });

        alarmDialog.show();
    }

    public void appendthis(String item) {
        Log.d("TAG", item + "\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mReceiver = new MyReceiver();

        switcher = (ViewSwitcher) findViewById(R.id.activity_main);

        ip = (EditText) findViewById(R.id.ipEdit);
        ipString = ip.getText().toString();

        port = (EditText) findViewById(R.id.portEdit);
        portString = port.getText().toString();

        connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNetwork stuff = new doNetwork();
                myNet = new Thread(stuff);
                myNet.start();
                switcher.showNext();
            }
        });

        String prefs = "MyPrefs";
        SharedPreferences settings = getSharedPreferences(prefs, 0);

        citydb = new CitiesDatabase(this);
        citydb.open();

        if(settings.getBoolean("firstRun", true)) {
            //Cursor c = citydb.getAllCities();

            BufferedReader br = null;
            String[] cityArr;
            try {
                br = new BufferedReader(
                        new InputStreamReader(getAssets().open("citiesOut.txt")));

                String city;
                int i = 0;
                while ((city = br.readLine()) != null) {
                    cityArr = city.split(",");
                    citydb.insertCity(cityArr[1], cityArr[2]);
                    cityArr[0] = "";
                    cityArr[1] = "";
                    cityArr[2] = "";
                    Log.d("WEATHER", Integer.toString(i));
                    i++;
                }
            } catch (IOException e) {
                Log.i("IOException", e.toString());
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Log.i("IOException", e.toString());
                    }
                }
            }

            settings.edit().putBoolean("firstRun", false).apply();
        }

        if(savedInstanceState != null)
        {
            sleepID = savedInstanceState.getInt("sleepID");
            powerID = savedInstanceState.getInt("powerID");
            gestureID = savedInstanceState.getInt("gestureID");
            voiceID = savedInstanceState.getInt("voiceID");

            a1formatID = savedInstanceState.getInt("a1formatID");
            a2formatID = savedInstanceState.getInt("a2formatID");
            a3formatID = savedInstanceState.getInt("a3formatID");
            b1formatID = savedInstanceState.getInt("b1formatID");
            b2formatID = savedInstanceState.getInt("b2formatID");
            b3formatID = savedInstanceState.getInt("b3formatID");
            c1formatID = savedInstanceState.getInt("c1formatID");
            c2formatID = savedInstanceState.getInt("c2formatID");
            c3formatID = savedInstanceState.getInt("c3formatID");

            a1timezoneID = savedInstanceState.getInt("a1timezoneID");
            a2timezoneID = savedInstanceState.getInt("a2timezoneID");
            a3timezoneID = savedInstanceState.getInt("a3timezoneID");
            b1timezoneID = savedInstanceState.getInt("b1timezoneID");
            b2timezoneID = savedInstanceState.getInt("b2timezoneID");
            b3timezoneID = savedInstanceState.getInt("b3timezoneID");
            c1timezoneID = savedInstanceState.getInt("c1timezoneID");
            c2timezoneID = savedInstanceState.getInt("c2timezoneID");
            c3timezoneID = savedInstanceState.getInt("c3timezoneID");

            a1city = savedInstanceState.getInt("a1city");
            a2city = savedInstanceState.getInt("a2city");
            a3city = savedInstanceState.getInt("a3city");
            b1city = savedInstanceState.getInt("b1city");
            b2city = savedInstanceState.getInt("b2city");
            b3city = savedInstanceState.getInt("b3city");
            c1city = savedInstanceState.getInt("c1city");
            c2city = savedInstanceState.getInt("c2city");
            c3city = savedInstanceState.getInt("c3city");

            a1state = savedInstanceState.getString("a1state");
            a2state = savedInstanceState.getString("a2state");
            a3state = savedInstanceState.getString("a3state");
            b1state = savedInstanceState.getString("b1state");
            b2state = savedInstanceState.getString("b2state");
            b3state = savedInstanceState.getString("b3state");
            c1state = savedInstanceState.getString("c1state");
            c2state = savedInstanceState.getString("c2state");
            c3state = savedInstanceState.getString("c3state");
        }

        a1 = (Button) findViewById(R.id.A1);

        if(savedInstanceState != null)
        {
            a1String = savedInstanceState.getString("a1String");
            Log.d("-----a1Text------", a1String);
            if(a1String != null)
            {
                a1.setText(a1String);
            }

            a1back = savedInstanceState.getInt("a1back");
            if(a1back != 0)
            {
                a1.setBackgroundColor(a1back);
            }

            a1text = savedInstanceState.getInt("a1text");
            if(a1text != 0)
            {
                a1.setTextColor(a1text);
            }
        }

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a1.setBackgroundColor(Color.parseColor("#fa6f04"));
                a1.setTextColor(Color.WHITE);

                a1Current = a1.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, a1);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        a1String = item.getTitle().toString();
                        switch (a1String) {
                            case "Clear Panel":
                                a1timezone = null;
                                a1format = null;
                                a1timezoneID = 0;
                                a1formatID = 0;
                                a1city = -1;
                                a1state = "";

                                a1back = Color.parseColor("#000000");
                                a1text = Color.parseColor("#ffffff");

                                a1.setBackgroundColor(Color.parseColor("#000000"));
                                a1.setTextColor(Color.parseColor("#ffffff"));
                                a1.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("A", 1);
                                popup.dismiss();
                                break;
                            case "Clock":
                                a1.setText(item.getTitle());
                                clockOptions("A", 1, false, a1Current);
                                sendMessage = "A1 clock ";// + a1formatID + a1timezoneID;
                                break;
                            case "Weather":
                                a1.setText(item.getTitle());
                                weatherOptions("A", 1, false, a1Current);
                                break;
                            case "Reminder":
                                a1.setText(item.getTitle());
                                reminderOptions("A", 1, false, a1Current);
                                break;
                            case "Alarm":
                                a1.setText(item.getTitle());
                                alarmOptions("A", 1, false, a1Current);
                                break;
                            default:
                                a1.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        a1.setBackgroundColor(a1back);
                        a1.setTextColor(a1text);
                    }
                });

                popup.show();

            }
        });

        a1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                a1Current = a1.getText().toString();

                if(a1.getText().equals("Clock"))
                {
                    clockOptions("A", 1, true, a1Current);
                }
                else if(a1.getText().equals("Weather"))
                {
                    weatherOptions("A", 1, true, a1Current);
                }
                else if(a1.getText().equals("Reminder"))
                {
                    reminderOptions("A", 1, true, a1Current);
                }
                else if(a1.getText().equals("Alarm"))
                {
                    alarmOptions("A", 1, true, a1Current);
                }
                return true;
            }
        });

        a2 = (Button) findViewById(R.id.A2);

        if(savedInstanceState != null)
        {
            a2String = savedInstanceState.getString("a2String");
            Log.d("-----a2Text------", a2String);
            if(a2String != null)
            {
                a2.setText(a2String);
            }

            a2back = savedInstanceState.getInt("a2back");
            if(a2back != 0)
            {
                a2.setBackgroundColor(a2back);
            }

            a2text = savedInstanceState.getInt("a2text");
            if(a2text != 0)
            {
                a2.setTextColor(a2text);
            }
        }

        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a2.setBackgroundColor(Color.parseColor("#fa6f04"));
                a2.setTextColor(Color.WHITE);

                a2Current = a2.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, a2);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        a2String = item.getTitle().toString();
                        switch (a2String) {
                            case "Clear Panel":
                                a2timezone = null;
                                a2format = null;
                                a2timezoneID = 0;
                                a2formatID = 0;
                                a2city = -1;
                                a2state = "";

                                a2back = Color.parseColor("#000000");
                                a2text = Color.parseColor("#ffffff");

                                a2.setBackgroundColor(Color.parseColor("#000000"));
                                a2.setTextColor(Color.parseColor("#ffffff"));
                                a2.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("A", 2);
                                popup.dismiss();
                                break;
                            case "Clock":
                                a2.setText(item.getTitle());
                                clockOptions("A", 2, false, a2Current);
                                break;
                            case "Weather":
                                a2.setText(item.getTitle());
                                weatherOptions("A", 2, false, a2Current);
                                break;
                            case "Reminder":
                                a2.setText(item.getTitle());
                                reminderOptions("A", 2, false, a2Current);
                                break;
                            case "Alarm":
                                a2.setText(item.getTitle());
                                alarmOptions("A", 2, false, a2Current);
                                break;
                            default:
                                a2.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        a2.setBackgroundColor(a2back);
                        a2.setTextColor(a2text);
                    }
                });

                popup.show();
            }
        });

        a2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                a2Current = a2.getText().toString();

                if(a2.getText().equals("Clock"))
                {
                    clockOptions("A", 2, true, a2Current);
                }
                else if(a2.getText().equals("Weather"))
                {
                    weatherOptions("A", 2, true, a2Current);
                }
                else if(a2.getText().equals("Reminder"))
                {
                    reminderOptions("A", 2, true, a2Current);
                }
                else if(a2.getText().equals("Alarm"))
                {
                    alarmOptions("A", 2, true, a2Current);
                }
                return true;
            }
        });

        a3 = (Button) findViewById(R.id.A3);

        if(savedInstanceState != null)
        {
            a3String = savedInstanceState.getString("a3String");
            Log.d("-----a3Text------", a3String);
            if(a3String != null)
            {
                a3.setText(a3String);
            }

            a3back = savedInstanceState.getInt("a3back");
            if(a3back != 0)
            {
                a3.setBackgroundColor(a3back);
            }

            a3text = savedInstanceState.getInt("a3text");
            if(a3text != 0)
            {
                a3.setTextColor(a3text);
            }
        }

        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a3.setBackgroundColor(Color.parseColor("#fa6f04"));
                a3.setTextColor(Color.WHITE);

                a3Current = a3.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, a3);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        a3String = item.getTitle().toString();
                        switch (a3String) {
                            case "Clear Panel":
                                a3timezone = null;
                                a3format = null;
                                a3timezoneID = 0;
                                a3formatID = 0;
                                a3city = -1;
                                a3state = "";

                                a3back = Color.parseColor("#000000");
                                a3text = Color.parseColor("#ffffff");

                                a3.setBackgroundColor(Color.parseColor("#000000"));
                                a3.setTextColor(Color.parseColor("#ffffff"));
                                a3.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("A", 3);
                                popup.dismiss();
                                break;
                            case "Clock":
                                a3.setText(item.getTitle());
                                clockOptions("A", 3, false, a3Current);
                                break;
                            case "Weather":
                                a3.setText(item.getTitle());
                                weatherOptions("A", 3, false, a3Current);
                                break;
                            case "Reminder":
                                a3.setText(item.getTitle());
                                reminderOptions("A", 3, false, a3Current);
                                break;
                            case "Alarm":
                                a3.setText(item.getTitle());
                                alarmOptions("A", 3, false, a3Current);
                                break;
                            default:
                                a3.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        a3.setBackgroundColor(a3back);
                        a3.setTextColor(a3text);
                    }
                });

                popup.show();
            }
        });

        a3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                a3Current = a3.getText().toString();

                if(a3.getText().equals("Clock"))
                {
                    clockOptions("A", 3, true, a3Current);
                }
                else if(a3.getText().equals("Weather"))
                {
                    weatherOptions("A", 3, true, a3Current);
                }
                else if(a3.getText().equals("Reminder"))
                {
                    reminderOptions("A", 3, true, a3Current);
                }
                else if(a3.getText().equals("Alarm"))
                {
                    alarmOptions("A", 3, true, a3Current);
                }
                return true;
            }
        });

        b1 = (Button) findViewById(R.id.B1);

        if(savedInstanceState != null)
        {
            b1String = savedInstanceState.getString("b1String");
            Log.d("-----b1Text------", b1String);
            if(b1String != null)
            {
                b1.setText(b1String);
            }

            b1back = savedInstanceState.getInt("b1back");
            if(b1back != 0)
            {
                b1.setBackgroundColor(b1back);
            }

            b1text = savedInstanceState.getInt("b1text");
            if(b1text != 0)
            {
                b1.setTextColor(b1text);
            }
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setBackgroundColor(Color.parseColor("#fa6f04"));
                b1.setTextColor(Color.WHITE);

                b1Current = b1.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, b1);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        b1String = item.getTitle().toString();
                        switch (b1String) {
                            case "Clear Panel":
                                b1timezone = null;
                                b1format = null;
                                b1timezoneID = 0;
                                b1formatID = 0;
                                b1city = -1;
                                b1state = "";

                                b1back = Color.parseColor("#000000");
                                b1text = Color.parseColor("#ffffff");

                                b1.setBackgroundColor(Color.parseColor("#000000"));
                                b1.setTextColor(Color.parseColor("#ffffff"));
                                b1.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("B", 1);
                                popup.dismiss();
                                break;
                            case "Clock":
                                b1.setText(item.getTitle());
                                clockOptions("B", 1, false, b1Current);
                                break;
                            case "Weather":
                                b1.setText(item.getTitle());
                                weatherOptions("B", 1, false, b1Current);
                                break;
                            case "Reminder":
                                b1.setText(item.getTitle());
                                reminderOptions("B", 1, false, b1Current);
                                break;
                            case "Alarm":
                                b1.setText(item.getTitle());
                                alarmOptions("B", 1, false, b1Current);
                                break;
                            default:
                                b1.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        b1.setBackgroundColor(b1back);
                        b1.setTextColor(b1text);
                    }
                });

                popup.show();
            }
        });

        b1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                b1Current = b1.getText().toString();

                if(b1.getText().equals("Clock"))
                {
                    clockOptions("B", 1, true, b1Current);
                }
                else if(b1.getText().equals("Weather"))
                {
                    weatherOptions("B", 1, true, b1Current);
                }
                else if(b1.getText().equals("Reminder"))
                {
                    reminderOptions("B", 1, true, b1Current);
                }
                else if(b1.getText().equals("Alarm"))
                {
                    alarmOptions("B", 1, true, b1Current);
                }
                return true;
            }
        });

        b2 = (Button) findViewById(R.id.B2);

        if(savedInstanceState != null)
        {
            b2String = savedInstanceState.getString("b2String");
            Log.d("-----b2Text------", b2String);
            if(b2String != null)
            {
                b2.setText(b2String);
            }

            b2back = savedInstanceState.getInt("b2back");
            if(b2back != 0)
            {
                b2.setBackgroundColor(b2back);
            }

            b2text = savedInstanceState.getInt("b2text");
            if(b2text != 0)
            {
                b2.setTextColor(b2text);
            }
        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setBackgroundColor(Color.parseColor("#fa6f04"));
                b2.setTextColor(Color.WHITE);

                b2Current = b2.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, b2);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        b2String = item.getTitle().toString();
                        switch (b2String) {
                            case "Clear Panel":
                                b2timezone = null;
                                b2format = null;
                                b2timezoneID = 0;
                                b2formatID = 0;
                                b2city = -1;
                                b2state = "";

                                b2back = Color.parseColor("#000000");
                                b2text = Color.parseColor("#ffffff");

                                b2.setBackgroundColor(Color.parseColor("#000000"));
                                b2.setTextColor(Color.parseColor("#ffffff"));
                                b2.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("B", 2);
                                popup.dismiss();
                                break;
                            case "Clock":
                                b2.setText(item.getTitle());
                                clockOptions("B", 2, false, b2Current);
                                break;
                            case "Weather":
                                b2.setText(item.getTitle());
                                weatherOptions("B", 2, false, b2Current);
                                break;
                            case "Reminder":
                                b2.setText(item.getTitle());
                                reminderOptions("B", 2, false, b2Current);
                                break;
                            case "Alarm":
                                b2.setText(item.getTitle());
                                alarmOptions("B", 2, false, b2Current);
                                break;
                            default:
                                b2.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        b2.setBackgroundColor(b2back);
                        b2.setTextColor(b2text);
                    }
                });

                popup.show();
            }
        });

        b2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                b2Current = b2.getText().toString();

                if(b2.getText().equals("Clock"))
                {
                    clockOptions("B", 2, true, b2Current);
                }
                else if(b2.getText().equals("Weather"))
                {
                    weatherOptions("B", 2, true, b2Current);
                }
                else if(b2.getText().equals("Reminder"))
                {
                    reminderOptions("B", 2, true, b2Current);
                }
                else if(b2.getText().equals("Alarm"))
                {
                    alarmOptions("B", 2, true, b2Current);
                }
                return true;
            }
        });

        b3 = (Button) findViewById(R.id.B3);

        if(savedInstanceState != null)
        {
            b3String = savedInstanceState.getString("b3String");
            Log.d("-----b3Text------", b3String);
            if(b3String != null)
            {
                b3.setText(b3String);
            }

            b3back = savedInstanceState.getInt("b3back");
            if(b3back != 0)
            {
                b3.setBackgroundColor(b3back);
            }

            b3text = savedInstanceState.getInt("b3text");
            if(b3text != 0)
            {
                b3.setTextColor(b3text);
            }
        }

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b3.setBackgroundColor(Color.parseColor("#fa6f04"));
                b3.setTextColor(Color.WHITE);

                b3Current = b3.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, b3);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        b3String = item.getTitle().toString();
                        switch (b3String) {
                            case "Clear Panel":
                                b3timezone = null;
                                b3format = null;
                                b3timezoneID = 0;
                                b3formatID = 0;
                                b3city = -1;
                                b3state = "";

                                b3back = Color.parseColor("#000000");
                                b3text = Color.parseColor("#ffffff");

                                b3.setBackgroundColor(Color.parseColor("#000000"));
                                b3.setTextColor(Color.parseColor("#ffffff"));
                                b3.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("B", 3);
                                popup.dismiss();
                                break;
                            case "Clock":
                                b3.setText(item.getTitle());
                                clockOptions("B", 3, false, b3Current);
                                break;
                            case "Weather":
                                b3.setText(item.getTitle());
                                weatherOptions("B", 3, false, b3Current);
                                break;
                            case "Reminder":
                                b3.setText(item.getTitle());
                                reminderOptions("B", 3, false, b3Current);
                                break;
                            case "Alarm":
                                b3.setText(item.getTitle());
                                alarmOptions("B", 3, false, b3Current);
                                break;
                            default:
                                b3.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        b3.setBackgroundColor(b3back);
                        b3.setTextColor(b3text);
                    }
                });

                popup.show();
            }
        });

        b3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                b3Current = b3.getText().toString();

                if(b3.getText().equals("Clock"))
                {
                    clockOptions("B", 3, true, b3Current);
                }
                else if(b3.getText().equals("Weather"))
                {
                    weatherOptions("B", 3, true, b3Current);
                }
                else if(b3.getText().equals("Reminder"))
                {
                    reminderOptions("B", 3, true, b3Current);
                }
                else if(b3.getText().equals("Alarm"))
                {
                    alarmOptions("B", 3, true, b3Current);
                }
                return true;
            }
        });

        c1 = (Button) findViewById(R.id.C1);

        if(savedInstanceState != null)
        {
            c1String = savedInstanceState.getString("c1String");
            Log.d("-----c1Text------", c1String);
            if(c1String != null)
            {
                c1.setText(c1String);
            }

            c1back = savedInstanceState.getInt("c1back");
            if(c1back != 0)
            {
                c1.setBackgroundColor(c1back);
            }

            c1text = savedInstanceState.getInt("c1text");
            if(c1text != 0)
            {
                c1.setTextColor(c1text);
            }
        }

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.setBackgroundColor(Color.parseColor("#fa6f04"));
                c1.setTextColor(Color.WHITE);

                c1Current = c1.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, c1);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        c1String = item.getTitle().toString();
                        switch (c1String) {
                            case "Clear Panel":
                                c1timezone = null;
                                c1format = null;
                                c1timezoneID = 0;
                                c1formatID = 0;
                                c1city = -1;
                                c1state = "";

                                c1back = Color.parseColor("#000000");
                                c1text = Color.parseColor("#ffffff");

                                c1.setBackgroundColor(Color.parseColor("#000000"));
                                c1.setTextColor(Color.parseColor("#ffffff"));
                                c1.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("C", 1);
                                popup.dismiss();
                                break;
                            case "Clock":
                                c1.setText(item.getTitle());
                                clockOptions("C", 1,false, c1Current);
                                break;
                            case "Weather":
                                c1.setText(item.getTitle());
                                weatherOptions("C", 1, false, c1Current);
                                break;
                            case "Reminder":
                                c1.setText(item.getTitle());
                                reminderOptions("C", 1, false, c1Current);
                                break;
                            case "Alarm":
                                c1.setText(item.getTitle());
                                alarmOptions("C", 1, false, c1Current);
                                break;
                            default:
                                c1.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        c1.setBackgroundColor(c1back);
                        c1.setTextColor(c1text);
                    }
                });

                popup.show();
            }
        });

        c1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                c1Current = c1.getText().toString();

                if(c1.getText().equals("Clock"))
                {
                    clockOptions("C", 1, true, c1Current);
                }
                else if(c1.getText().equals("Weather"))
                {
                    weatherOptions("C", 1, true, c1Current);
                }
                else if(c1.getText().equals("Reminder"))
                {
                    reminderOptions("C", 1, true, c1Current);
                }
                else if(c1.getText().equals("Alarm"))
                {
                    alarmOptions("C", 1, true, c1Current);
                }
                return true;
            }
        });

        c2 = (Button) findViewById(R.id.C2);

        if(savedInstanceState != null)
        {
            c2String = savedInstanceState.getString("c2String");
            Log.d("-----c2Text------", c2String);
            if(c2String != null)
            {
                c2.setText(c2String);
            }

            c2back = savedInstanceState.getInt("c2back");
            if(c2back != 0)
            {
                c2.setBackgroundColor(c2back);
            }

            c2text = savedInstanceState.getInt("c2text");
            if(c2text != 0)
            {
                c2.setTextColor(c2text);
            }
        }

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c2.setBackgroundColor(Color.parseColor("#fa6f04"));
                c2.setTextColor(Color.WHITE);

                c2Current = c2.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, c2);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        c2String = item.getTitle().toString();
                        switch (c2String) {
                            case "Clear Panel":
                                c2timezone = null;
                                c2format = null;
                                c2timezoneID = 0;
                                c2formatID = 0;
                                c2city = -1;
                                c2state = "";

                                c2back = Color.parseColor("#000000");
                                c2text = Color.parseColor("#ffffff");

                                c2.setBackgroundColor(Color.parseColor("#000000"));
                                c2.setTextColor(Color.parseColor("#ffffff"));
                                c2.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("C", 2);
                                popup.dismiss();
                                break;
                            case "Clock":
                                c2.setText(item.getTitle());
                                clockOptions("C", 2, false, c2Current);
                                break;
                            case "Weather":
                                c2.setText(item.getTitle());
                                weatherOptions("C", 2, false, c2Current);
                                break;
                            case "Reminder":
                                c2.setText(item.getTitle());
                                reminderOptions("C", 2, false, c2Current);
                                break;
                            case "Alarm":
                                c2.setText(item.getTitle());
                                alarmOptions("C", 2, false, c2Current);
                                break;
                            default:
                                c2.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        c2.setBackgroundColor(c2back);
                        c2.setTextColor(c2text);
                    }
                });

                popup.show();
            }
        });

        c2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                c2Current = c2.getText().toString();

                if(c2.getText().equals("Clock"))
                {
                    clockOptions("C", 2, true, c2Current);
                }
                else if(c2.getText().equals("Weather"))
                {
                    weatherOptions("C", 2, true, c2Current);
                }
                else if(c2.getText().equals("Reminder"))
                {
                    reminderOptions("C", 2, true, c2Current);
                }
                else if(c2.getText().equals("Alarm"))
                {
                    alarmOptions("C", 2, true, c2Current);
                }
                return true;
            }
        });

        c3 = (Button) findViewById(R.id.C3);

        if(savedInstanceState != null)
        {
            c3String = savedInstanceState.getString("c3String");
            Log.d("-----c3Text------", c3String);
            if(c3String != null)
            {
                c3.setText(c3String);
            }

            c3back = savedInstanceState.getInt("c3back");
            if(c3back != 0)
            {
                c3.setBackgroundColor(c3back);
            }

            c3text = savedInstanceState.getInt("c3text");
            if(c3text != 0)
            {
                c3.setTextColor(c3text);
            }
        }

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c3.setBackgroundColor(Color.parseColor("#fa6f04"));
                c3.setTextColor(Color.WHITE);

                c3Current = c3.getText().toString();

                final PopupMenu popup = new PopupMenu(MainActivity.this, c3);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        c3String = item.getTitle().toString();
                        switch (c3String) {
                            case "Clear Panel":
                                c3timezone = null;
                                c3format = null;
                                c3timezoneID = 0;
                                c3formatID = 0;
                                c3city = -1;
                                c3state = "";

                                c3back = Color.parseColor("#000000");
                                c3text = Color.parseColor("#ffffff");

                                c3.setBackgroundColor(Color.parseColor("#000000"));
                                c3.setTextColor(Color.parseColor("#ffffff"));
                                c3.setText("+");
                                break;
                            case "Color Options":
                                colorOptions("C", 3);
                                popup.dismiss();
                                break;
                            case "Clock":
                                c3.setText(item.getTitle());
                                clockOptions("C", 3, false, c3Current);
                                break;
                            case "Weather":
                                c3.setText(item.getTitle());
                                weatherOptions("C", 3, false, c3Current);
                                break;
                            case "Reminder":
                                c3.setText(item.getTitle());
                                reminderOptions("C", 3, false, c3Current);
                                break;
                            case "Alarm":
                                c3.setText(item.getTitle());
                                alarmOptions("C", 3, false, c3Current);
                                break;
                            default:
                                c3.setText(item.getTitle());
                                break;
                        }
                        return true;
                    }
                });

                popup.setOnDismissListener(new PopupMenu.OnDismissListener(){
                    public void onDismiss(PopupMenu menu) {
                        c3.setBackgroundColor(c3back);
                        c3.setTextColor(c3text);
                    }
                });

                c3.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        c3Current = c3.getText().toString();

                        if(c3.getText().equals("Clock"))
                        {
                            clockOptions("C", 3, true, c3Current);
                        }
                        else if(c3.getText().equals("Weather"))
                        {
                            weatherOptions("C", 3, true, c3Current);
                        }
                        else if(c3.getText().equals("Reminder"))
                        {
                            reminderOptions("C", 3, true, c3Current);
                        }
                        else if(c3.getText().equals("Alarm"))
                        {
                            alarmOptions("C", 3, true, c3Current);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        getprefs();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //output.append(msg.getData().getString("msg"));
            return true;
        }

    });

    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    public void onClick(View v) {
        doNetwork stuff = new doNetwork();
        myNet = new Thread(stuff);
        myNet.start();
    }

    class doNetwork implements Runnable {
        public PrintWriter out;
        public BufferedReader in;

        public void run() {


            int p = Integer.parseInt(port.getText().toString());
            String h = ipString;
            mkmsg("host is " + h + "\n");
            mkmsg(" Port is " + p + "\n");
            try {
                InetAddress serverAddr = InetAddress.getByName(h);
                mkmsg("Attempt Connecting..." + h + "\n");
                Socket socket = new Socket(serverAddr, p);
                String message = "Hello from Client android emulator";

                //made connection, setup the read (in) and write (out)
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //now send a message to the server and then read back the response.
                try {
                    //write a message to the server
                    mkmsg("Attempting to send message ...\n");
                    out.println(message);
                    mkmsg("Message sent...\n");

                    //read back a message from the server.
                    mkmsg("Attempting to receive a message ...\n");
                    String str = in.readLine();
                    mkmsg("received a message:\n" + str + "\n");

                    out.println(sendMessage);

                    mkmsg("We are done, closing connection\n");
                } catch (Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                } finally {
                    in.close();
                    out.close();
                    socket.close();
                }

            } catch (Exception e) {
                mkmsg("Unable to connect...\n");
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        a1String = a1.getText().toString();
        a2String = a2.getText().toString();
        a3String = a3.getText().toString();
        b1String = b1.getText().toString();
        b2String = b2.getText().toString();
        b3String = b3.getText().toString();
        c1String = c1.getText().toString();
        c2String = c2.getText().toString();
        c3String = c3.getText().toString();

        savedInstanceState.putString("a1String", a1String);
        savedInstanceState.putString("a2String", a2String);
        savedInstanceState.putString("a3String", a3String);
        savedInstanceState.putString("b1String", b1String);
        savedInstanceState.putString("b2String", b2String);
        savedInstanceState.putString("b3String", b3String);
        savedInstanceState.putString("c1String", c1String);
        savedInstanceState.putString("c2String", c2String);
        savedInstanceState.putString("c3String", c3String);

        savedInstanceState.putInt("a1back", a1back);
        savedInstanceState.putInt("a2back", a2back);
        savedInstanceState.putInt("a3back", a3back);
        savedInstanceState.putInt("b1back", b1back);
        savedInstanceState.putInt("b2back", b2back);
        savedInstanceState.putInt("b3back", b3back);
        savedInstanceState.putInt("c1back", c1back);
        savedInstanceState.putInt("c2back", c2back);
        savedInstanceState.putInt("c3back", c3back);

        savedInstanceState.putInt("a1text", a1text);
        savedInstanceState.putInt("a2text", a2text);
        savedInstanceState.putInt("a3text", a3text);
        savedInstanceState.putInt("b1text", b1text);
        savedInstanceState.putInt("b2text", b2text);
        savedInstanceState.putInt("b3text", b3text);
        savedInstanceState.putInt("c1text", c1text);
        savedInstanceState.putInt("c2text", c2text);
        savedInstanceState.putInt("c3text", c3text);

        savedInstanceState.putInt("sleepID", sleepID);
        savedInstanceState.putInt("powerID", powerID);
        savedInstanceState.putInt("gestureID", gestureID);
        savedInstanceState.putInt("voiceID", voiceID);

        savedInstanceState.putInt("a1formatID", a1formatID);
        savedInstanceState.putInt("a2formatID", a2formatID);
        savedInstanceState.putInt("a3formatID", a3formatID);
        savedInstanceState.putInt("b1formatID", b1formatID);
        savedInstanceState.putInt("b2formatID", b2formatID);
        savedInstanceState.putInt("b3formatID", b3formatID);
        savedInstanceState.putInt("c1formatID", c1formatID);
        savedInstanceState.putInt("c2formatID", c2formatID);
        savedInstanceState.putInt("c3formatID", c3formatID);

        savedInstanceState.putInt("a1timezoneID", a1timezoneID);
        savedInstanceState.putInt("a2timezoneID", a2timezoneID);
        savedInstanceState.putInt("a3timezoneID", a3timezoneID);
        savedInstanceState.putInt("b1timezoneID", b1timezoneID);
        savedInstanceState.putInt("b2timezoneID", b2timezoneID);
        savedInstanceState.putInt("b3timezoneID", b3timezoneID);
        savedInstanceState.putInt("c1timezoneID", c1timezoneID);
        savedInstanceState.putInt("c2timezoneID", c2timezoneID);
        savedInstanceState.putInt("c3timezoneID", c3timezoneID);

        savedInstanceState.putInt("a1city", a1city);
        savedInstanceState.putInt("a2city", a2city);
        savedInstanceState.putInt("a3city", a3city);
        savedInstanceState.putInt("b1city", b1city);
        savedInstanceState.putInt("b2city", b2city);
        savedInstanceState.putInt("b3city", b3city);
        savedInstanceState.putInt("c1city", c1city);
        savedInstanceState.putInt("c2city", c2city);
        savedInstanceState.putInt("c3city", c3city);

        savedInstanceState.putString("a1state", a1state);
        savedInstanceState.putString("a2state", a2state);
        savedInstanceState.putString("a3state", a3state);
        savedInstanceState.putString("b1state", b1state);
        savedInstanceState.putString("b2state", b2state);
        savedInstanceState.putString("b3state", b3state);
        savedInstanceState.putString("c1state", c1state);
        savedInstanceState.putString("c2state", c2state);
        savedInstanceState.putString("c3state", c3state);

        super.onSaveInstanceState(savedInstanceState);
    }

    void getprefs()
    {
        SharedPreferences prefs = getSharedPreferences("name", MODE_PRIVATE);

        sleepID = prefs.getInt("sleepID", 0);
        powerID = prefs.getInt("powerID", 0);
        gestureID = prefs.getInt("gestureID", 0);
        voiceID = prefs.getInt("voiceID", 0);

        a1formatID = prefs.getInt("a1formatID", 0);
        a2formatID = prefs.getInt("a2formatID", 0);
        a3formatID = prefs.getInt("a3formatID", 0);
        b1formatID = prefs.getInt("b1formatID", 0);
        b2formatID = prefs.getInt("b2formatID", 0);
        b3formatID = prefs.getInt("b3formatID", 0);
        c1formatID = prefs.getInt("c1formatID", 0);
        c2formatID = prefs.getInt("c2formatID", 0);
        c3formatID = prefs.getInt("c3formatID", 0);

        a1timezoneID = prefs.getInt("a1timezoneID", 0);
        a2timezoneID = prefs.getInt("a2timezoneID", 0);
        a3timezoneID = prefs.getInt("a3timezoneID", 0);
        b1timezoneID = prefs.getInt("b1timezoneID", 0);
        b2timezoneID = prefs.getInt("b2timezoneID", 0);
        b3timezoneID = prefs.getInt("b3timezoneID", 0);
        c1timezoneID = prefs.getInt("c1timezoneID", 0);
        c2timezoneID = prefs.getInt("c2timezoneID", 0);
        c3timezoneID = prefs.getInt("c3timezoneID", 0);

        a1city = prefs.getInt("a1city", -1);
        a2city = prefs.getInt("a2city", -1);
        a3city = prefs.getInt("a3city", -1);
        b1city = prefs.getInt("b1city", -1);
        b2city = prefs.getInt("b2city", -1);
        b3city = prefs.getInt("b3city", -1);
        c1city = prefs.getInt("c1city", -1);
        c2city = prefs.getInt("c2city", -1);
        c3city = prefs.getInt("c3city", -1);

        a1state = prefs.getString("a1state", "");
        a2state = prefs.getString("a2state", "");
        a3state = prefs.getString("a3state", "");
        b1state = prefs.getString("b1state", "");
        b2state = prefs.getString("b2state", "");
        b3state = prefs.getString("b3state", "");
        c1state = prefs.getString("c1state", "");
        c2state = prefs.getString("c2state", "");
        c3state = prefs.getString("c3state", "");

        a1String = prefs.getString("a1String", "");
        a2String = prefs.getString("a2String", "");
        a3String = prefs.getString("a3String", "");
        b1String = prefs.getString("b1String", "");
        b2String = prefs.getString("b2String", "");
        b3String = prefs.getString("b3String", "");
        c1String = prefs.getString("c1String", "");
        c2String = prefs.getString("c2String", "");
        c3String = prefs.getString("c3String", "");

        a1back = prefs.getInt("a1back", Color.parseColor("#000000"));
        a2back = prefs.getInt("a2back", Color.parseColor("#000000"));
        a3back = prefs.getInt("a3back", Color.parseColor("#000000"));
        b1back = prefs.getInt("b1back", Color.parseColor("#000000"));
        b2back = prefs.getInt("b2back", Color.parseColor("#000000"));
        b3back = prefs.getInt("b3back", Color.parseColor("#000000"));
        c1back = prefs.getInt("c1back", Color.parseColor("#000000"));
        c2back = prefs.getInt("c2back", Color.parseColor("#000000"));
        c3back = prefs.getInt("c3back", Color.parseColor("#000000"));

        a1text = prefs.getInt("a1text", Color.parseColor("#ffffff"));
        a2text = prefs.getInt("a2text", Color.parseColor("#ffffff"));
        a3text = prefs.getInt("a3text", Color.parseColor("#ffffff"));
        b1text = prefs.getInt("b1text", Color.parseColor("#ffffff"));
        b2text = prefs.getInt("b2text", Color.parseColor("#ffffff"));
        b3text = prefs.getInt("b3text", Color.parseColor("#ffffff"));
        c1text = prefs.getInt("c1text", Color.parseColor("#ffffff"));
        c2text = prefs.getInt("c2text", Color.parseColor("#ffffff"));
        c3text = prefs.getInt("c3text", Color.parseColor("#ffffff"));

        if(a1String!=null){
            a1.setText(a1String);
        }
        if(a1String.equals("")){
            a1.setText("+");
        }

        if(a2String!=null){
            a2.setText(a2String);
        }
        if(a2String.equals("")){
            a2.setText("+");
        }

        if(a3String!=null){
            a3.setText(a3String);
        }
        if(a3String.equals("")){
            a3.setText("+");
        }

        if(b1String!=null){
            b1.setText(b1String);
        }
        if(b1String.equals("")){
            b1.setText("+");
        }

        if(b2String!=null){
            b2.setText(b2String);
        }
        if(b2String.equals("")){
            b2.setText("+");
        }

        if(b3String!=null){
            b3.setText(b3String);
        }
        if(b3String.equals("")){
            b3.setText("+");
        }

        if(c1String!=null){
            c1.setText(c1String);
        }
        if(c1String.equals("")){
            c1.setText("+");
        }

        if(c2String!=null){
            c2.setText(c2String);
        }
        if(c2String.equals("")){
            c2.setText("+");
        }

        if(c3String!=null){
            c3.setText(c3String);
        }
        if(c3String.equals("")){
            c3.setText("+");
        }

        if(a1back!=0){
            a1.setBackgroundColor(a1back);
        }
        if(a1back==0){
            a1.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(a2back!=0)
        {
            a2.setBackgroundColor(a2back);
        }
        if(a2back==0){
            a2.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(a3back!=0){
            a3.setBackgroundColor(a3back);
        }
        if(a3back==0){
            a3.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(b1back!=0){
            b1.setBackgroundColor(b1back);
        }
        if(b1back==0){
            b1.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(b2back!=0){
            b2.setBackgroundColor(b2back);
        }
        if(b2back==0){
            b2.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(b3back!=0){
            b3.setBackgroundColor(b3back);
        }
        if(b3back==0){
            b3.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(c1back!=0){
            c1.setBackgroundColor(c1back);
        }
        if(c1back==0){
            c1.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(c2back!=0){
            c2.setBackgroundColor(c2back);
        }
        if(c2back==0){
            c2.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(c3back!=0){
            c3.setBackgroundColor(c3back);
        }
        if(c3back==0){
            c3.setBackgroundColor(Color.parseColor("#000000"));
        }

        if(a1text!=0){
            a1.setTextColor(a1text);
        }
        if(a1text==0){
            a1.setTextColor(Color.parseColor("#ffffff"));
        }

        if(a2text!=0)
        {
            a2.setTextColor(a2text);
        }
        if(a2text==0){
            a2.setTextColor(Color.parseColor("#ffffff"));
        }

        if(a3text!=0){
            a3.setTextColor(a3text);
        }
        if(a3text==0){
            a3.setTextColor(Color.parseColor("#ffffff"));
        }

        if(b1text!=0){
            b1.setTextColor(b1text);
        }
        if(b1text==0){
            b1.setTextColor(Color.parseColor("#ffffff"));
        }

        if(b2text!=0){
            b2.setTextColor(b2text);
        }
        if(b2text==0){
            b2.setTextColor(Color.parseColor("#ffffff"));
        }

        if(b3text!=0){
            b3.setTextColor(b3text);
        }
        if(b3text==0){
            b3.setTextColor(Color.parseColor("#ffffff"));
        }

        if(c1text!=0){
            c1.setTextColor(c1text);
        }
        if(c1text==0){
            c1.setTextColor(Color.parseColor("#ffffff"));
        }

        if(c2text!=0){
            c2.setTextColor(c2text);
        }
        if(c2text==0){
            c2.setTextColor(Color.parseColor("#ffffff"));
        }

        if(c3text!=0){
            c3.setTextColor(c3text);
        }
        if(c3text==0){
            c3.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public void onPause()
     {
        //getBaseContext().unregisterReceiver(mReceiver);

        super.onPause();
        Log.d("PAUSE", "Not in focus");

        SharedPreferences prefs = getSharedPreferences("name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("sleepID", sleepID);
        editor.putInt("powerID", powerID);
        editor.putInt("gestureID", gestureID);
        editor.putInt("voiceID", voiceID);

        editor.putInt("a1formatID", a1formatID);
        editor.putInt("a2formatID", a2formatID);
        editor.putInt("a3formatID", a3formatID);
        editor.putInt("b1formatID", b1formatID);
        editor.putInt("b2formatID", b2formatID);
        editor.putInt("b3formatID", b3formatID);
        editor.putInt("c1formatID", c1formatID);
        editor.putInt("c2formatID", c2formatID);
        editor.putInt("c3formatID", c3formatID);

        editor.putInt("a1timezoneID", a1timezoneID);
        editor.putInt("a2timezoneID", a2timezoneID);
        editor.putInt("a3timezoneID", a3timezoneID);
        editor.putInt("b1timezoneID", b1timezoneID);
        editor.putInt("b2timezoneID", b2timezoneID);
        editor.putInt("b3timezoneID", b3timezoneID);
        editor.putInt("c1timezoneID", c1timezoneID);
        editor.putInt("c2timezoneID", c2timezoneID);
        editor.putInt("c3timezoneID", c3timezoneID);

        editor.putInt("a1city", a1city);
        editor.putInt("a2city", a2city);
        editor.putInt("a3city", a3city);
        editor.putInt("b1city", b1city);
        editor.putInt("b2city", b2city);
        editor.putInt("b3city", b3city);
        editor.putInt("c1city", c1city);
        editor.putInt("c2city", c2city);
        editor.putInt("c3city", c3city);

        editor.putString("a1state", a1state);
        editor.putString("a2state", a2state);
        editor.putString("a3state", a3state);
        editor.putString("b1state", b1state);
        editor.putString("b2state", b2state);
        editor.putString("b3state", b3state);
        editor.putString("c1state", c1state);
        editor.putString("c2state", c2state);
        editor.putString("c3state", c3state);

        editor.putInt("a1back", a1back);
        editor.putInt("a2back", a2back);
        editor.putInt("a3back", a3back);
        editor.putInt("b1back", b1back);
        editor.putInt("b2back", b2back);
        editor.putInt("b3back", b3back);
        editor.putInt("c1back", c1back);
        editor.putInt("c2back", c2back);
        editor.putInt("c3back", c3back);

        editor.putInt("a1text", a1text);
        editor.putInt("a2text", a2text);
        editor.putInt("a3text", a3text);
        editor.putInt("b1text", b1text);
        editor.putInt("b2text", b2text);
        editor.putInt("b3text", b3text);
        editor.putInt("c1text", c1text);
        editor.putInt("c2text", c2text);
        editor.putInt("c3text", c3text);

        a1String = a1.getText().toString();
        a2String = a2.getText().toString();
        a3String = a3.getText().toString();
        b1String = b1.getText().toString();
        b2String = b2.getText().toString();
        b3String = b3.getText().toString();
        c1String = c1.getText().toString();
        c2String = c2.getText().toString();
        c3String = c3.getText().toString();

        editor.putString("a1String", a1String);
        editor.putString("a2String", a2String);
        editor.putString("a3String", a3String);
        editor.putString("b1String", b1String);
        editor.putString("b2String", b2String);
        editor.putString("b3String", b3String);
        editor.putString("c1String", c1String);
        editor.putString("c2String", c2String);
        editor.putString("c3String", c3String);

        editor.apply(); //FLAG
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("RESUME", "Became visible");
        getprefs();
        //getBaseContext().registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
    }

    //@Override
    //protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
    //}


}
