package com.huydo2908.mp3player.models;

import com.huydo2908.basemodule.models.BaseModel;

public class Album extends BaseModel {

    private String name;
    private String artist;
    private int numberSong;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getNumberSong() {
        return numberSong;
    }

    public void setNumberSong(int numberSong) {
        this.numberSong = numberSong;
    }

    @Override
    public boolean checkFilter(String key) {
        return name.toLowerCase().replaceAll("[ ]", "")
                .contains(key.toLowerCase().replaceAll("[ ]", ""));
    }
}
