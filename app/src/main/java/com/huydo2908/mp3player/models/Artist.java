package com.huydo2908.mp3player.models;

import com.huydo2908.basemodule.models.BaseModel;

public class Artist extends BaseModel {

    private String name;
    private String numberAlbum;
    private int numberTrack;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberAlbum() {
        return numberAlbum;
    }

    public void setNumberAlbum(String numberAlbum) {
        this.numberAlbum = numberAlbum;
    }

    public int getNumberTrack() {
        return numberTrack;
    }

    public void setNumberTrack(int numberTrack) {
        this.numberTrack = numberTrack;
    }

    @Override
    public boolean checkFilter(String key) {
        return name.toLowerCase().replaceAll("[ ]", "")
                .contains(key.toLowerCase().replaceAll("[ ]", ""));
    }
}
