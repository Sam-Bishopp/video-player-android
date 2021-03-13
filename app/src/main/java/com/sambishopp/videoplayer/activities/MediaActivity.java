package com.sambishopp.videoplayer.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.sambishopp.videoplayer.R;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Activity to play show available media from a specific folder.
 */
public class MediaActivity extends AppCompatActivity {

    private int STORAGE_WRITE_PERMISSION_CODE = 100;
    private String path = "PlayerTest" + "/" + "videos";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        checkStoragePermissions();
    }

    private void checkStoragePermissions()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            //notifyUser("You already granted this permission");
            createDirectory();
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
                notifyUser("Storage Write Permission: Granted");
            }
            else
            {
                notifyUser("Storage Write Permission: Denied");
            }
        }
    }

    private void createDirectory()
    {
        File directory = new File(Environment.getExternalStorageDirectory(), path);

        if(!directory.exists())
        {
            directory.mkdirs();

            if(directory.isDirectory())
            {
                notifyUser("Directory Created");
            }
            else
            {
                notifyUser("Failed to create directory");
            }
        }
    }

    //Display toast messages to the user
    private void notifyUser(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
