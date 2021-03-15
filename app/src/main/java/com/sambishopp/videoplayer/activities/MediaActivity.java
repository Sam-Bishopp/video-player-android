package com.sambishopp.videoplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.sambishopp.videoplayer.R;
import com.sambishopp.videoplayer.data.VideoFiles;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Activity to play show available media from a specific folder.
 */
public class MediaActivity extends AppCompatActivity {

    private int STORAGE_WRITE_PERMISSION_CODE = 100;

    private String path = "PlayerTest" + "/" + "videos";
    private Uri videoUri = Uri.parse("PlayerTest" + "/" + "videos" + "/" + "video.mp4");

    static ArrayList<VideoFiles> videoFiles = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        checkStoragePermissions(); //Check storage access permissions when the app starts.
    }

    //If permission is already granted; try to create directory. Otherwise request permission.
    private void checkStoragePermissions()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            createDirectory();
            videoFiles = getAllVideos(this);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_WRITE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == STORAGE_WRITE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                createDirectory();
                videoFiles = getAllVideos(this);
                //notifyUser("Storage Write Permission: Granted");
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_WRITE_PERMISSION_CODE); //Temporary solution for testing purposes. Remove / change where this is used later.
                notifyUser("Storage Write Permission: Denied");
            }
        }
    }

    //Create directory the app will use to store and access video.
    @SuppressWarnings("All")
    private void createDirectory()
    {
        File directory = new File(Environment.getExternalStorageDirectory(), path);

        if(!directory.exists()) //If the directory does NOT exist; create it. Otherwise, do nothing.
        {
            directory.mkdirs();

            if(directory.isDirectory()) //Notify the user if the directory could be created or not.
            {
                notifyUser("Directory Created");
            }
            else
            {
                notifyUser("Failed to create directory");
            }
        }
    }

    //Function to search for all videos to play in the app.
    public ArrayList<VideoFiles> getAllVideos(Context context)
    {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);

                VideoFiles videoFiles = new VideoFiles(id, path, title, size, dateAdded, duration, fileName);
                Log.e("Path", path);
                tempVideoFiles.add(videoFiles);
            }
            cursor.close();
        }
        return tempVideoFiles;
    }

    //Function to simplify the process of showing toast messages.
    private void notifyUser(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
