package com.huydo2908.mp3player.fragments.artist;

import com.huydo2908.basemodule.base.BaseListener;
import com.huydo2908.mp3player.models.Artist;

public interface ArtistItemListener extends BaseListener {
    void onArtistItemClicked(Artist artist);
}
