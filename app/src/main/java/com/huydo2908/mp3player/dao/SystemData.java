package com.huydo2908.mp3player.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.huydo2908.mp3player.models.Album;
import com.huydo2908.mp3player.models.Artist;
import com.huydo2908.mp3player.models.Song;

import java.util.ArrayList;

public class SystemData {

    private ContentResolver resolver;

    public SystemData(Context context) {
        this.resolver = context.getContentResolver();
    }

    public ArrayList<Song> getSong() {
        ArrayList<Song> songArrayList = new ArrayList<>();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        int indexData = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        int indexTitle = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        int indexSize = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE);
        int indexDuration = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
        int indexArtist = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
        int indexAlbum = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);

        while (cursor.isAfterLast() == false) {
            String data = cursor.getString(indexData);
            String title = cursor.getString(indexTitle);
            int size = cursor.getInt(indexSize);
            int duration = cursor.getInt(indexDuration);
            String artist = cursor.getString(indexArtist);
            String album = cursor.getString(indexAlbum);

            Song song = new Song();
            song.setData(data);
            song.setTitle(title);
            song.setSize(size);
            song.setDuration(duration);
            song.setArtist(artist);
            song.setAlbum(album);
            songArrayList.add(song);

            cursor.moveToNext();
        }
        return songArrayList;
    }

    public ArrayList<Album> getAlbum() {
        ArrayList<Album> albumArrayList = new ArrayList<>();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        int indexName = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
        int indexArtist = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
        int indexNumberSong = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);

        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(indexName);
            String artist = cursor.getString(indexArtist);
            int numberSong = cursor.getInt(indexNumberSong);

            Album album = new Album();
            album.setName(name);
            album.setArtist(artist);
            album.setNumberSong(numberSong);
            albumArrayList.add(album);

            cursor.moveToNext();
        }
        return albumArrayList;
    }

    public ArrayList<Artist> getArtist() {
        ArrayList<Artist> artistArrayList = new ArrayList<>();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        int indexName = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
        int indexNumberAlbum = cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
        int indexNumberTrack = cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(indexName);
            String numberAlbum = cursor.getString(indexNumberAlbum);
            int numberTrack = cursor.getInt(indexNumberTrack);

            Artist artist = new Artist();
            artist.setName(name);
            artist.setNumberAlbum(numberAlbum);
            artist.setNumberTrack(numberTrack);
            artistArrayList.add(artist);

            cursor.moveToNext();
        }
        return artistArrayList;
    }
}
