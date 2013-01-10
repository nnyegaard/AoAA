package me.nyegaard.AAoA;

import android.os.Environment;
import java.io.File;

public class FolderStructure
{
    public FolderStructure()
    {
    }

    private static String noteBookName;

    public static void createFolder()
    {
        File rootFolder = new File(Environment.getExternalStorageDirectory() + "/AAoA/"); // Should be changed to a const public so I can use it in general

        if (!rootFolder.exists())
        {
            rootFolder.mkdir();
        }

        File noteBookNameFolder = new File(rootFolder + "/" + noteBookName);
        noteBookNameFolder.mkdir();
    }

    public static void setNoteBookName(String pNoteBookName)
    {
        noteBookName = pNoteBookName;
    }

    public static String getNoteBookName()
    {
        return noteBookName;
    }
}