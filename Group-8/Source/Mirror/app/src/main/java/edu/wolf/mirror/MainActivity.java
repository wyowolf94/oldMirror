package edu.wolf.mirror;

import android.app.Service;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    String TAG = "TCPServer", message = "State of Mirror";
    Thread myNet;

    TextView ip;
    ViewSwitcher switcher;
    TextView a1;
    Button  a2, b1, b2, b3, c1, c2, c3;
    LinearLayout a3;

    // a1 -> 0 | a2 -> 1 | a3 -> 2 | b1 -> 3 | b2 -> 4 | b3 -> 5 | c1 -> 6 | c2 -> 7 | c3 -> 8
    int[] text;
    int[] back;
    String[] formats;
    String[] timezones;
    String[] states;
    String[] cities;

    TextClock a1clock;
    ImageView a3alarm;

    Boolean clear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = (ViewSwitcher) findViewById(R.id.activity_main);
        switcher.showNext();

        WifiManager wm = (WifiManager) getSystemService(Service.WIFI_SERVICE);
        WifiInfo info = wm.getConnectionInfo();
        int ipint = info.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ipint);
        //String ipAddress = Integer.toString(wm.getConnectionInfo().getIpAddress());

        Log.d("IP", ipAddress);
        ip = (TextView) findViewById(R.id.ip);
        ip.setTextColor(Color.parseColor("#ffffff"));
        String display = "IP: " + ipAddress;
        ip.setText(display);

        //a1 = (TextView) findViewById(R.id.A1);
        a1clock = (TextClock) findViewById(R.id.a1clock);

        a2 = (Button) findViewById(R.id.A2);
        //a3 = (Button) findViewById(R.id.A3);
        a3alarm = (ImageView) findViewById(R.id.a3alarm);
        a3 = (LinearLayout) findViewById(R.id.rlayout);

        b1 = (Button) findViewById(R.id.B1);
        b2 = (Button) findViewById(R.id.B2);
        b3 = (Button) findViewById(R.id.B3);
        c1 = (Button) findViewById(R.id.C1);
        c2 = (Button) findViewById(R.id.C2);
        c3 = (Button) findViewById(R.id.C3);

        doNetwork stuff = new doNetwork();
        myNet = new Thread(stuff);
        myNet.start();

    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.d("Server", msg.getData().getString("msg"));
            return true;
        }

    });

    @Override
    public void onClick(View v) {
        //Toast.makeText(MainActivity.this, "OnClick Happened", Toast.LENGTH_SHORT).show();
        doNetwork stuff = new doNetwork();
        myNet = new Thread(stuff);
        myNet.start();
    }

    public void mkmsg(String str) {
        //handler junk, because threads can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }

/*    public void countDown() {
        new CountDownTimer(12000, 1000) {

            public void onTick(long millisUntilFinished) {
                int mins = (int)((millisUntilFinished / 1000) /60);
                int secs = (12000%mins)/1000;
                a3alarm.setText(mins + ":" + secs);
            }

            public void onFinish() {
                a3alarm.setText("ALARM!");
            }
        }.start();
    }*/

    class doNetwork implements Runnable {
        public void run() {

            int p = 3012;

            while(!message.equals("Done"))
            {
                try {
                    mkmsg("Waiting on Connecting...\n");
                    Log.v(TAG,"S: Connecting...");
                    ServerSocket serverSocket = new ServerSocket(p);

                    //socket created, now wait for a coonection via accept.
                    Socket client = serverSocket.accept();
                    Log.v(TAG,"S: Receiving...");

                    //switcher.showNext();

                    try {
                        //setup send/receive streams.
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

                        label:
                        while (!message.equals("Closed")) {
                            //receive the message first.
                            mkmsg("Attempting to receive a message ...\n");
                            //Toast.makeText(MainActivity.this, "I'm TRYING", Toast.LENGTH_SHORT).show();
                            String str = in.readLine();
                            mkmsg("received a message:\n" + str + "\n");
                            String[] strArray = str.split("\\s+");

                            if (str.equals("clear mirror")) {
                                out.println("close");

                                clear = true;

                                in.close();
                                out.close();
                                break;
                            }

                            switch (strArray[0]) {
                                case "A1":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[0] = Color.parseColor("#000000");
                                                text[0] = Color.parseColor("#ffffff");
                                                a1.setText("");
                                            } else {
                                                back[0] = Integer.parseInt(strArray[2]);
                                                text[0] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            a1.setBackgroundColor(back[0]);
                                            a1.setTextColor(text[0]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                            //a1.setText("CLOCK");
                                            //formats[0] = strArray[2];
                                            //timezones[0] = strArray[3];
                                            //a1.setVisibility(View.INVISIBLE);
                                            a1clock.setVisibility(View.VISIBLE);
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            a1.setVisibility(View.VISIBLE);
                                            a1clock.setVisibility(View.INVISIBLE);
                                            a1.setText("WEATHER");
                                            states[0] = strArray[2];
                                            cities[0] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            a1.setVisibility(View.VISIBLE);
                                            a1clock.setVisibility(View.INVISIBLE);
                                            a1.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            a1.setVisibility(View.VISIBLE);
                                            a1clock.setVisibility(View.INVISIBLE);
                                            a1.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            a1.setVisibility(View.VISIBLE);
                                            a1clock.setVisibility(View.INVISIBLE);
                                            out.println("close");
                                            a1.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                case "A2":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[1] = Color.parseColor("#000000");
                                                text[1] = Color.parseColor("#ffffff");
                                                a2.setText("");
                                            } else {
                                                back[1] = Integer.parseInt(strArray[2]);
                                                text[1] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            a2.setBackgroundColor(back[1]);
                                            a2.setTextColor(text[1]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                            a2.setText("CLOCK");
                                            formats[1] = strArray[2];
                                            timezones[1] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            a2.setText("WEATHER");
                                            states[1] = strArray[2];
                                            cities[1] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            a2.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            a2.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            a2.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                case "A3":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[2] = Color.parseColor("#000000");
                                                text[2] = Color.parseColor("#ffffff");
                                                //a3.setText("");
                                            } else {
                                                back[2] = Integer.parseInt(strArray[2]);
                                                text[2] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            a3.setBackgroundColor(back[2]);
                                            //a3.setTextColor(text[2]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                           // a3.setText("CLOCK");
                                            formats[2] = strArray[2];
                                            timezones[2] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            //a3.setText("WEATHER");
                                            states[2] = strArray[2];
                                            cities[2] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            //a3.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            //a3.setText("ALARM");
                                            a3.setVisibility(View.VISIBLE);///////////////////////////////////////////////////
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            //a3.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                case "B1":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[3] = Color.parseColor("#000000");
                                                text[3] = Color.parseColor("#ffffff");
                                                b1.setText("");
                                            } else {
                                                back[3] = Integer.parseInt(strArray[2]);
                                                text[3] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            b1.setBackgroundColor(back[3]);
                                            b1.setTextColor(text[3]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                            b1.setText("CLOCK");
                                            formats[3] = strArray[2];
                                            timezones[3] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            b1.setText("WEATHER");
                                            states[3] = strArray[2];
                                            cities[3] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            b1.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            b1.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            b1.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                case "B2":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[4] = Color.parseColor("#000000");
                                                text[4] = Color.parseColor("#ffffff");
                                                b2.setText("");
                                            } else {
                                                back[4] = Integer.parseInt(strArray[2]);
                                                text[4] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            b2.setBackgroundColor(back[4]);
                                            b2.setTextColor(text[4]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                            b2.setText("CLOCK");
                                            formats[4] = strArray[2];
                                            timezones[4] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            b2.setText("WEATHER");
                                            states[4] = strArray[2];
                                            cities[4] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            b2.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            b2.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            b2.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                case "B3":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[5] = Color.parseColor("#000000");
                                                text[5] = Color.parseColor("#ffffff");
                                                b3.setText("");
                                            } else {
                                                back[5] = Integer.parseInt(strArray[2]);
                                                text[5] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            b3.setBackgroundColor(back[5]);
                                            b3.setTextColor(text[5]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                            b3.setText("CLOCK");
                                            formats[5] = strArray[2];
                                            timezones[5] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            b3.setText("WEATHER");
                                            states[5] = strArray[2];
                                            cities[5] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            b3.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            b3.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            b3.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                case "C1":
                                    switch (strArray[1]) {
                                        case "color":
                                            c1.setVisibility(View.VISIBLE);
                                            in.close();
                                            out.close();
                                            break;
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[6] = Color.parseColor("#000000");
                                                text[6] = Color.parseColor("#ffffff");
                                                c1.setText("");
                                            } else {
                                                back[6] = Integer.parseInt(strArray[2]);
                                                text[6] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            c1.setBackgroundColor(back[6]);
                                            c1.setTextColor(text[6]);
                                            in.close();
                                            out.close();
                                            break;
                                        case "clock":
                                            c1.setText("CLOCK");
                                            formats[6] = strArray[2];
                                            timezones[6] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            c1.setText("WEATHER");
                                            states[6] = strArray[2];
                                            cities[6] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            c1.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            c1.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            c1.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break;
                                    }
                                    break;

                                case "C2":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[7] = Color.parseColor("#000000");
                                                text[7] = Color.parseColor("#ffffff");
                                                c2.setText("");
                                            } else {
                                                back[7] = Integer.parseInt(strArray[2]);
                                                text[7] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            c2.setBackgroundColor(back[7]);
                                            c2.setTextColor(text[7]);
                                            in.close();
                                            out.close();
                                            break;
                                        case "clock":
                                            c2.setText("CLOCK");
                                            formats[7] = strArray[2];
                                            timezones[7] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            c2.setText("WEATHER");
                                            states[7] = strArray[2];
                                            cities[7] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            c2.setText("REMINDER");
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            c2.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            c2.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break;
                                    }
                                    break;

                                case "C3":
                                    switch (strArray[1]) {
                                        case "color":
                                        case "clear":
                                            if (strArray[1].equals("clear")) {
                                                back[8] = Color.parseColor("#000000");
                                                text[8] = Color.parseColor("#ffffff");
                                                c3.setText("");
                                            } else {
                                                back[8] = Integer.parseInt(strArray[2]);
                                                text[8] = Integer.parseInt(strArray[3]);
                                            }
                                            out.println("close");
                                            c3.setBackgroundColor(back[8]);
                                            c3.setTextColor(text[8]);
                                            in.close();
                                            out.close();
                                            break label;
                                        case "clock":
                                            c3.setText("CLOCK");
                                            formats[8] = strArray[2];
                                            timezones[8] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "weather":
                                            c3.setText("WEATHER");
                                            states[8] = strArray[2];
                                            cities[8] = strArray[3];
                                            in.close();
                                            out.close();
                                            break;
                                        case "reminder":
                                            c3.setText("Reminder \n April 30, 2017 \n 9:00 PM");//////////////////////////////////
                                            in.close();
                                            out.close();
                                        case "alarm":
                                            c3.setText("ALARM");
                                            in.close();
                                            out.close();
                                        default:
                                            out.println("close");
                                            c3.setText(strArray[1].toUpperCase());
                                            in.close();
                                            out.close();
                                            break label;
                                    }
                                    break;

                                default:
                                    message = "State of Mirror";
                                    out.println(message);
                                    break;
                            }
                        } // end of while

                        //now close down the send/receive streams.

                        in.close();
                        out.close();


                        //Toast.makeText(MainActivity.this, "I'm TRYING 2", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        mkmsg("Error happened sending/receiving\n");
                        //client.close();
                        //serverSocket.close();

                        //findViewById(R.id.activity_main).performContextClick();

                        //Toast.makeText(MainActivity.this, "I'm TRYING 5", Toast.LENGTH_SHORT).show();
                        //run();

                    } finally {
                        //Toast.makeText(MainActivity.this, "The world ended", Toast.LENGTH_SHORT).show();
                        //mkmsg("We are done, closing connection\n");
                        //message = "Done";
                        if(clear)
                        {
                            a1.setBackgroundColor(Color.parseColor("#000000"));
                            a1.setTextColor(Color.parseColor("#ffffff"));
                            a1.setText("");
                            a2.setBackgroundColor(Color.parseColor("#000000"));
                            a2.setTextColor(Color.parseColor("#ffffff"));
                            a2.setText("");
                            a3.setBackgroundColor(Color.parseColor("#000000"));
                            //a3.setTextColor(Color.parseColor("#ffffff"));
                            //a3.setText("");

                            b1.setBackgroundColor(Color.parseColor("#000000"));
                            b1.setTextColor(Color.parseColor("#ffffff"));
                            b1.setText("");
                            b2.setBackgroundColor(Color.parseColor("#000000"));
                            b2.setTextColor(Color.parseColor("#ffffff"));
                            b2.setText("");
                            b3.setBackgroundColor(Color.parseColor("#000000"));
                            b3.setTextColor(Color.parseColor("#ffffff"));
                            b3.setText("");

                            c1.setBackgroundColor(Color.parseColor("#000000"));
                            c1.setTextColor(Color.parseColor("#ffffff"));
                            c1.setText("");
                            c2.setBackgroundColor(Color.parseColor("#000000"));
                            c2.setTextColor(Color.parseColor("#ffffff"));
                            c2.setText("");
                            c3.setBackgroundColor(Color.parseColor("#000000"));
                            c3.setTextColor(Color.parseColor("#ffffff"));
                            c3.setText("");

                            clear = false;
                        }
                        client.close();  //close the client connection
                        serverSocket.close();  //finally close down the server side as well.
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mkmsg("Unable to connect...\n");
                }
            }
        }
    }
}
