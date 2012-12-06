package me.nyegaard.AAoA;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;


public class StartActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClick(View view)
    {
        Toast.makeText(this,"In onClick", Toast.LENGTH_SHORT);
        displayNotification();

        try
        {
            doCmd();
        }
        catch(IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void doCmd() throws IOException, InterruptedException
    {
        Process process = Runtime.getRuntime().exec("su");

        DataOutputStream os = new DataOutputStream(process.getOutputStream());

        os.writeBytes("/system/bin/screencap /sdcard/test2.png");
        os.flush();
        os.close();

        process.waitFor();
    }

    protected void displayNotification()
    {
        Intent i = new  Intent(this, NotificationView.class);
        i.putExtra("notificationID", 0);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, i, 0);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notif = new  Notification(
                R.drawable. ic_launcher,
                "AAoA started!",
        System.currentTimeMillis());
        CharSequence from =  "AAoA";
        CharSequence message = "Press to start AAoA." ;

        notif.setLatestEventInfo( this, from, message, pendingIntent);
        nm.notify(0, notif);
    }
}
