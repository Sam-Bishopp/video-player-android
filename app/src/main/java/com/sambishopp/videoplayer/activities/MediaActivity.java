package com.sambishopp.videoplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sambishopp.videoplayer.R;
import com.sambishopp.videoplayer.data.VideoFiles;
import com.sambishopp.videoplayer.ui.MediaAdapter;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Activity to play show available media from a specific folder.
 */
public class MediaActivity extends AppCompatActivity {

    private int STORAGE_WRITE_PERMISSION_CODE = 100;

    private String path = "PlayerTest" + "/" + "videos";
    private File directory = new File(Environment.getExternalStorageDirectory(), path);

    TextView noPermissionsText; //This Text view appears when the app does not have storage permissions.
    TextView noDataText; //This Text view appears when the app does not detect any media to display in the recycler view.

    static ArrayList<VideoFiles> videoFiles = new ArrayList<>();
    RecyclerView recyclerView;
    MediaAdapter mediaAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setContentView(R.layout.activity_media);

        noPermissionsText = findViewById(R.id.noPermissionText);
        noDataText = findViewById(R.id.noDataText);

        //If permissions have not been granted, the user can click this TextView to make the app request storage permissions.
        noPermissionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermissions();
            }
        });

        recyclerView = findViewById(R.id.videoRecyclerView);
        loadMedia();

        checkStoragePermissions(); //Check storage access permissions when the app starts.
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMedia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedia();
    }

    //If permission is already granted; create the directory. Otherwise request permission.
    private void checkStoragePermissions()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            noPermissionsText.setVisibility(View.GONE);
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
                noPermissionsText.setVisibility(View.GONE);
                createDirectory();
                videoFiles = getAllVideos(this);
            }
            else //If the user denies permission, show an alert dialog to explain the reason why permission is needed.
            {
                new AlertDialog.Builder(this)
                        .setTitle("Permissions Request")
                        .setMessage("This app requires storage permissions to be able to create a folder for this app to store videos. To use this app, please allow storage permissions.")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkStoragePermissions(); //App will try to ask for permission again.
                            }
                        })
                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //Alert dialog is dismissed.
                            }
                        })
                        .show();
            }
        }
    }

    //Create directory the app will use to store and access video.
    @SuppressWarnings("All")
    private void createDirectory()
    {
        if(!directory.exists()) //If the directory does NOT exist; create it. Otherwise, do nothing.
        {
            directory.mkdirs();

            if(directory.isDirectory()) //Notify the user if the directory could be created or not.
            {
                notifyUserToast("Directory Created");
            }
            else
            {
                notifyUserToast("Failed to create directory");
            }
        }
    }

    //Function to search for all videos to play in the app.
    public ArrayList<VideoFiles> getAllVideos(Context context)
    {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();

        //Variables for cursor arguments. Specifies what the cursor should return + where to look
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION
        };
        String selection = MediaStore.Video.Media.DATA + " LIKE ? ";
        String[] selectionArgs = new String[] {"%" + path + "%"};

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

        if(cursor != null)
        {
            while(cursor.moveToNext())
            {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String fileName = cursor.getString(3);
                String size = cursor.getString(4);
                String dateAdded = cursor.getString(5);
                String duration = cursor.getString(6);

                VideoFiles videoFiles = new VideoFiles(id, path, title, fileName, size, dateAdded, duration);
                Log.e("Path", path);
                tempVideoFiles.add(videoFiles);
            }
            cursor.close();
        }
        return tempVideoFiles;
    }

    //Reloads Media Adapter and checks for new media to list in recycler view.
    private void loadMedia()
    {
        if(videoFiles != null && videoFiles.size() > 0) //If there is media to show, display it.
        {
            noDataText.setVisibility(View.GONE);
            mediaAdapter = new MediaAdapter(getApplicationContext(), videoFiles);
            recyclerView.setAdapter(mediaAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        }
        else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) //Otherwise, set a message to let the user know that no data could be found / how to potentially fix the problem.
        {
            noDataText.setVisibility(View.VISIBLE);
        }
    }

    //Function to simplify the process of showing toast messages.
    private void notifyUserToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}