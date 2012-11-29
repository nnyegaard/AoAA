package me.nyegaard.AAoA;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;

/**
 * Project: AAoA
 * User: niny
 * Date: 25-10-12
 * Time: 14:39
 */
public class NotificationView extends Activity
{
    //==============================
    // Variables
    //==============================
    Context mContext = null;

    private FrameLayout     mCanvasContainer;
    private SCanvasView		mSCanvas;
    private ImageView       mPenBtn;
    private ImageView		mEraserBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas);

        //------------------------------------
        // Create SCanvasView
        //------------------------------------

        mContext = this;
        mCanvasContainer = (FrameLayout) findViewById(R.id.canvas);
        mSCanvas = new SCanvasView(mContext);
        mCanvasContainer.addView(mSCanvas);

        //------------------------------------
        // Create Button listeners
        //------------------------------------
        mPenBtn = (ImageView) findViewById(R.id.penBtn);
        mPenBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
                Toast.makeText(getBaseContext(), "Pen", Toast.LENGTH_SHORT).show();
            }
        });

        mEraserBtn = (ImageView) findViewById(R.id.eraserBtn);
        mEraserBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
                Toast.makeText(getBaseContext(), "Eraser", Toast.LENGTH_SHORT).show();
            }
        });

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        nm.cancel(getIntent().getExtras().getInt("notificationID"));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        displayNotification();
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
        //---100ms delay, vibrate for 250ms, pause for 100 ms and
        //notif.vibrate =  new  long[] { 100, 250, 100, 500};
        nm.notify(0, notif);
    }
}
