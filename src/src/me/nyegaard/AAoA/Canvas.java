package me.nyegaard.AAoA;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.ColorPickerColorChangeListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.samsung.spensdk.applistener.SCanvasModeChangedListener;

import java.io.*;

public class Canvas extends Activity
{
    //==============================
    // Variables
    //==============================
    Context mContext = null;

    // Drawing
    private FrameLayout     mCanvasContainer;
    private SCanvasView		mSCanvas;
    private ImageView       mPenBtn;
    private ImageView		mEraserBtn;
    private ImageView       mSaveBtn;

    // Storage
    final private String    externalStorage = Environment.getExternalStorageDirectory().toString();
    final FolderStructure   folderStructure = new FolderStructure();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas);
        mContext = this;

        //------------------------------------
        // UI Settings
        //------------------------------------

        // Canvas
        mPenBtn = (ImageView) findViewById(R.id.penBtn);
        mPenBtn.setOnClickListener(mBtnClickListener);
        mEraserBtn = (ImageView) findViewById(R.id.eraserBtn);
        mEraserBtn.setOnClickListener(mBtnClickListener);
        mSaveBtn = (ImageView) findViewById(R.id.saveBtn);
        mSaveBtn.setOnClickListener(mBtnClickListener);


        //------------------------------------
        // Create SCanvasView
        //------------------------------------
        mCanvasContainer = (FrameLayout) findViewById(R.id.canvas);
        mSCanvas = new SCanvasView(mContext);

        mCanvasContainer.addView(mSCanvas);




        //------------------------------------
        // SettingView Setting
        //------------------------------------
        mSCanvas.createSettingView(mCanvasContainer, null, null);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(getIntent().getExtras().getInt("notificationID"));

        //====================================================================================
        //
        // Set Callback Listener(Interface)
        //
        //====================================================================================
        //------------------------------------------------
        // SCanvas Listener
        //------------------------------------------------
        mSCanvas.setSCanvasInitializeListener(new SCanvasInitializeListener()
        {
            @Override
            public void onInitialized()
            {
                updateButtonState();
            }
        });

        //------------------------------------------------
        // SCanvas Mode Changed Listener
        //------------------------------------------------
        mSCanvas.setSCanvasModeChangedListener(new SCanvasModeChangedListener()
        {
            @Override
            public void onModeChanged(int mode)
            {
                updateButtonState();
            }
        });

        //------------------------------------------------
        // Color Picker Listener
        //------------------------------------------------
        mSCanvas.setColorPickerColorChangeListener(new ColorPickerColorChangeListener()
        {
            @Override
            public void onColorPickerColorChanged(int nColor)
            {
                int nCurMode = mSCanvas.getCanvasMode();
                if(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_PEN)
                {
                    SettingStrokeInfo strokeInfo = mSCanvas.getSettingViewStrokeInfo();
                    if(strokeInfo != null)
                    {
                        strokeInfo.setStrokeColor(nColor);
                        mSCanvas.setSettingViewStrokeInfo(strokeInfo);
                    }
                }
            }
        });

        mPenBtn.setSelected(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        //Toast.makeText(getBaseContext(), "Window has focus", Toast.LENGTH_SHORT).show();
        if(hasFocus)
        {
            //shellCmd("test2"); // Take a picture of the background
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        displayNotification();
    }

    public int getServiceProcessId()
    {
        return android.os.Process.myPid();
    }


    View.OnClickListener mBtnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int nBtnID = v.getId();
            // If the mode is not changed, open the setting view. If the mode is same, close the setting view.
            if(nBtnID == mPenBtn.getId())
            {
                if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_PEN)
                {
                    mSCanvas.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
                    mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
                }
                else
                {
                    mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
                    mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
                    updateButtonState();
                }
            }
            else if(nBtnID == mEraserBtn.getId())
            {
                if(mSCanvas.getCanvasMode()==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER)
                {
                    mSCanvas.setSettingViewSizeOption(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
                    mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
                }
                else
                {
                    mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
                    mSCanvas.showSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
                    updateButtonState();
                }
            }
            else if(nBtnID == mSaveBtn.getId())
            {
                saveCanvas();
            }
        }
    };

    private void updateButtonState()
    {
        int nCurMode = mSCanvas.getCanvasMode();
        mPenBtn.setSelected(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
        mEraserBtn.setSelected(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);

        // Reset color picker tool when Eraser Mode
        if(nCurMode==SCanvasConstants.SCANVAS_MODE_INPUT_ERASER)
            mSCanvas.setColorPickerMode(false);
    }

    private void saveCanvas()
    {
        //   TODO: Implement a feature where you can pull the canvas over the background
        /*
         * Implementation of saving the canvas
         *
        mSCanvas.setDrawingCacheEnabled(true);

        Bitmap bitmap = mSCanvas.getBitmap(true);

        File myPath = new File(externalStorage, "test.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */

        SharedPreferences settings = getSharedPreferences(folderStructure.getNoteBookName(), 0);

        int cnt = settings.getInt(folderStructure.getNoteBookName(), 0);
        String fileName = cnt  + "";

        shellCmd(fileName);

        SharedPreferences.Editor editor = settings.edit().putInt(folderStructure.getNoteBookName(), ++cnt);
        editor.commit();
        finish();
    }

    private void shellCmd(String fileName)
    {

        try
        {
            Process process = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("/system/bin/screencap " + externalStorage + "/AAoA/" + folderStructure.getNoteBookName() + "/" + fileName + ".png");
            os.flush();
            os.close();

            process.waitFor();
        }
        catch(IOException e)
        {
            e.printStackTrace(); //Auto gen. statement
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();  //Auto gen. statement
        }
    }

    private void displayNotification()
    {
        Intent i = new  Intent(this, Canvas.class);
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
