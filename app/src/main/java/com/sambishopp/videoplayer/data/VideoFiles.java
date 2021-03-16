package com.sambishopp.videoplayer.data;

/**
 * Class to format & contain data related to video files.
 */
public class VideoFiles {
    private String id;
    private String path;
    private String title;
    private String fileName;
    private String size;
    private String dateAdded;
    private String duration;

    public VideoFiles(String id, String path, String title, String fileName, String size, String dateAdded, String duration) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.fileName = fileName;
        this.size = size;
        this.dateAdded = dateAdded;
        this.duration = duration;
    }

    //Video ID Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    //Video path Getter and Setter
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //Video title Getter and Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //Video filename Getter and Setter
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //Video size Getter and Setter
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    //Video date added Getter and Setter
    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    //Video duration Getter and Setter
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}