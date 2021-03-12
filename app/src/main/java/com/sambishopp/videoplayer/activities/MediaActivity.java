package com.sambishopp.videoplayer.activities;

import android.os.Bundle;

import com.sambishopp.videoplayer.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to play show available media from a specific folder.
 */
public class MediaActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
    }
}
