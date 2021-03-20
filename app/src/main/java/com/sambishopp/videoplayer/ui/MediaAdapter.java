package com.sambishopp.videoplayer.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sambishopp.videoplayer.R;
import com.sambishopp.videoplayer.activities.PlayerActivity;
import com.sambishopp.videoplayer.data.VideoFiles;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter to show media from "videoFiles" in recycler view.
 */
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private Context context;
    static ArrayList<VideoFiles> videoFiles;
    View view;

    public MediaAdapter(Context context, ArrayList<VideoFiles> videoFiles)
    {
        this.context = context;
        MediaAdapter.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) //Create a video item for each video file found.
    {
        view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, final int position)
    {
        holder.fileName.setText(videoFiles.get(position).getFileName());
        holder.duration.setText(videoFiles.get(position).getDuration());
        //TODO: Get duration of each video

        Glide.with(context)
                .load(new File(videoFiles.get(position).getPath()))
                .into(holder.thumbnail);

        //When an item is clicked, start the player activity and play the video.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mediaIntent = new Intent(context, PlayerActivity.class);
                mediaIntent.putExtra("position", position);
                mediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mediaIntent);
            }
        });
    }

    @Override
    public int getItemCount() //Gets the amount of items
    {
        return videoFiles.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder
    {
        ImageView thumbnail;
        TextView fileName, duration;

        public MediaViewHolder(@NonNull View itemView)
        {
            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            fileName = itemView.findViewById(R.id.videoName);
            duration = itemView.findViewById(R.id.videoDuration);
        }
    }
}