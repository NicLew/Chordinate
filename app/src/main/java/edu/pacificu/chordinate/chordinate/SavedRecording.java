package edu.pacificu.chordinate.chordinate;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by lewe4441 on 11/8/2015.
 */
public class SavedRecording {
    private File mFile;
    private String mFileName;
    private String mRecName;
    private Date mDate;
    private long mLength; // what data type? use System.currentTimeMillis()

    SavedRecording (int fileNum) {
        //String fileName;
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recording_file_"+Integer.toString(fileNum)+".3gp";
        //mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        //mFileName += "/recording_file_"+fileNum+".3gp";
        mFile = new File(mFileName);

        mRecName = "Recording #"+Integer.toString(fileNum);

        mDate = new Date ();
        mLength = 0;
    }

    public String getFileName () {

        //return mFile.getName();
        return mFileName;
    }

    public Date getDate () {

        return mDate;
    }

    public String getDateStr () {
        String dateString = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

        dateString = dateFormat.format(mDate);

        return dateString;
    }

    public long getLength () {

        return mLength;
    }

    public String getRecName () {

        return mRecName;
    }

    public void setRecName (String newName) {

        mRecName = newName;
    }
}
