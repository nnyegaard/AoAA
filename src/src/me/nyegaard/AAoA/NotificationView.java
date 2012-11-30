package me.nyegaard.AAoA;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
    private ImageView       mSaveBtn;
    private ImageView		mColorPickerBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas);
        mContext = this;

        //------------------------------------
        // UI Settings
        //------------------------------------
        mPenBtn = (ImageView) findViewById(R.id.penBtn);


        //------------------------------------
        // Create SCanvasView
        //------------------------------------
        mCanvasContainer = (FrameLayout) findViewById(R.id.canvas);
        mSCanvas = new SCanvasView(mContext);
        mCanvasContainer.addView(mSCanvas);

        //------------------------------------
        // Create Button listeners
        //------------------------------------

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

        mSaveBtn = (ImageView) findViewById(R.id.saveBtn);
        mSaveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveCanvas();
                Toast.makeText(getBaseContext(), "Saving", Toast.LENGTH_SHORT).show();
            }
        });

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        nm.cancel(getIntent().getExtras().getInt("notificationID"));
    }

    public void saveCanvas()
    {
        mSCanvas.setDrawingCacheEnabled(true);

        Bitmap bitmap = mSCanvas.getBitmap(true);

        String extr = Environment.getExternalStorageDirectory().toString();
        File myPath = new File(extr, "test.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finish();
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
