package com.huydo2908.mp3player.fragments.song;

import com.huydo2908.basemodule.base.BaseListener;
import com.huydo2908.mp3player.models.Song;

public interface SongItemListener extends BaseListener {
    void onSongItemClicked(Song song);
}
