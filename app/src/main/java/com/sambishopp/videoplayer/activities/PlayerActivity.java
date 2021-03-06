package com.sambishopp.videoplayer.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sambishopp.videoplayer.R;

import androidx.appcompat.app.AppCompatActivity;

import static com.sambishopp.videoplayer.activities.MediaActivity.videoFiles;

/**
 * Fullscreen activity to play media.
 */
public class PlayerActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView playerView;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    int position = -1;
    MediaItem mediaItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.videoPlayer);
        position = getIntent().getIntExtra("position", -1);
        String path = videoFiles.get(position).getPath();

        //If path variable is initialised, parse the Uri and place it into the media item.
        if(path != null)
        {
            Uri selectedMedia = Uri.parse(path);
            mediaItem = MediaItem.fromUri(selectedMedia);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();

        if(player == null) //Check if the player has been initialised yet. If not; initialise it.
        {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer(); //Release the player when the activity pauses
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer(); //Release the player when the activity stops
    }

    //Prepare the player to play media and set the file to be played back.
    private void initializePlayer()
    {
        player = new SimpleExoPlayer.Builder(this).build();

        playerView.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
    }

    //Hide the system UI when the player is visible.
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    //Release the player and record data about the player's current state at the time of release.
    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
}