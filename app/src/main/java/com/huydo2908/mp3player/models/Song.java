package com.huydo2908.mp3player.models;

import com.huydo2908.basemodule.models.BaseModel;

public class Song extends BaseModel {

    private String data;
    private String title;
    private int duration;
    private int size;
    private String artist;
    private String album;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public boolean checkFilter(String key) {
        return title.toLowerCase().replaceAll("[ ]", "")
                .contains(key.toLowerCase().replaceAll("[ ]", ""));
    }
}
