package me.nyegaard.AAoA;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(getWindowManager().getDefaultDisplay().getRotation() == 0)
        {
            // Landscape mode
            LandscapeUI landscapeUI = new LandscapeUI();
            fragmentTransaction.replace(android.R.id.content, landscapeUI);
        }
        else
        {
            // Portrait mode
            PortraitUI portraitUI = new PortraitUI();
            fragmentTransaction.replace(android.R.id.content, portraitUI);
        }

        fragmentTransaction.commit();
    }

    public void onClick(View view)
    {
        createNotification();
        // Logic about edit text field
        finish();
    }

    public void displayNotification(View view)
    {
        createNotification();
    }

    private void createNotification()
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

    // Code to show dialog:
    // Class should implements CreateNewNotebookDialog.CreateNewNoteBookDialogListener
    // Then we can receive events from the dialog when positive and negative button is pressed.
    {
    /* Show Dialog code:

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog)
    {
        EditText noteBookName = (EditText) findViewById(R.id.CreateNotebookDialogEditText);
        if(noteBookName != null)
        {
            String test = noteBookName.getText().toString();
            Toast.makeText(getBaseContext(), "You wrote: " + test, Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getBaseContext(), "Something is wrong!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
        Toast.makeText(getBaseContext(), "Negative clicked!", Toast.LENGTH_SHORT).show();
    }



    private void showCreateNewNotebookDialog()
    {
        CreateNewNotebookDialog createNewNotebookDialog = new CreateNewNotebookDialog();
        createNewNotebookDialog.show(getFragmentManager(), "test");
    }

    */
    }
}