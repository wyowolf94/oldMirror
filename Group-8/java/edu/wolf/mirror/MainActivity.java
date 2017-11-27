package edu.wolf.mirror;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
    Button a1, a2, a3, b1, b2, b3, c1, c2, c3;

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

        a1 = (Button) findViewById(R.id.A1);
        a2 = (Button) findViewById(R.id.A2);
        a3 = (Button) findViewById(R.id.A3);
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

                        while(!message.equals("Closed"))
                        {
                            //receive the message first.
                            mkmsg("Attempting to receive a message ...\n");
                            //Toast.makeText(MainActivity.this, "I'm TRYING", Toast.LENGTH_SHORT).show();
                            String str = in.readLine();
                            mkmsg("received a message:\n" + str + "\n");
                            String[] strArray = str.split("\\s+");

                            if(str.equals("clear mirror"))
                            {
                                out.println("close");

                                clear = true;

                                in.close();
                                out.close();
                                break;
                            }

                            if(strArray[0].equals("A1"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        a1.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    a1.setBackgroundColor(back);
                                    a1.setTextColor(text);
                                    //Intent intent = new Intent(this, ClockWidget.class);
                                    //EditText editText = (EditText) findViewById(R.id.A1);
                                    //String message = editText.getText().toString();
                                    //startActivity(intent);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    //Intent intent = new Intent(this, ClockWidget.class);
                                    a1.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("A2"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        a2.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    a2.setBackgroundColor(back);
                                    a2.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    a2.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("A3"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        a3.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    a3.setBackgroundColor(back);
                                    a3.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    a3.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("B1"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        b1.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    b1.setBackgroundColor(back);
                                    b1.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    b1.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("B2"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        b2.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    b2.setBackgroundColor(back);
                                    b2.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    b2.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("B3"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        b3.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    b3.setBackgroundColor(back);
                                    b3.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    b3.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("C1"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        c1.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    c1.setBackgroundColor(back);
                                    c1.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    c1.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("C2"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        c2.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    c2.setBackgroundColor(back);
                                    c2.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    c2.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else if(strArray[0].equals("C3"))
                            {
                                if(strArray[1].equals("color") || strArray[1].equals("clear"))
                                {
                                    int back; int text;
                                    if(strArray[1].equals("clear"))
                                    {
                                        back = Color.parseColor("#000000");
                                        text = Color.parseColor("#ffffff");
                                        c3.setText("");
                                    }
                                    else
                                    {
                                        back = Integer.parseInt(strArray[2]);
                                        text = Integer.parseInt(strArray[3]);
                                    }
                                    out.println("close");
                                    c3.setBackgroundColor(back);
                                    c3.setTextColor(text);
                                    in.close();
                                    out.close();
                                    break;
                                }
                                else
                                {
                                    out.println("close");
                                    c3.setText(strArray[1].toUpperCase());
                                    in.close();
                                    out.close();
                                    break;
                                }
                            }
                            else
                            {
                                message = "State of Mirror";
                                out.println(message);
                            }
                        } // end of while

                        //now close down the send/receive streams.

                        in.close();
                        out.close();
                        //Toast.makeText(MainActivity.this, "I'm TRYING 2", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        mkmsg("Error happened sending/receiving\n");
                        client.close();
                        serverSocket.close();

                        //findViewById(R.id.activity_main).performContextClick();

                        //Toast.makeText(MainActivity.this, "I'm TRYING 5", Toast.LENGTH_SHORT).show();
                        run();

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
                            a3.setTextColor(Color.parseColor("#ffffff"));
                            a3.setText("");

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

                        //Toast.makeText(MainActivity.this, "I'm TRYING 4", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    mkmsg("Unable to connect...\n");
                    //Toast.makeText(MainActivity.this, "I'm TRYING 3", Toast.LENGTH_SHORT).show();
                    //message = "Done";
                    //run();
                }
            }
        }
    }
}
