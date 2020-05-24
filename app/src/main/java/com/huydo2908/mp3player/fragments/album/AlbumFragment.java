package com.huydo2908.mp3player.fragments.album;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huydo2908.basemodule.base.BaseFragment;
import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.adapters.AppAdapter;
import com.huydo2908.mp3player.dao.SystemData;
import com.huydo2908.mp3player.databinding.FragmentAlbumBinding;
import com.huydo2908.mp3player.models.Album;

public class AlbumFragment extends BaseFragment<FragmentAlbumBinding> implements AlbumItemListener {

    private AppAdapter<Album> adapter;
    private SystemData systemData;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new AppAdapter<>(getLayoutInflater(), R.layout.item_album);
        systemData = new SystemData(getContext());
        adapter.setData(systemData.getAlbum());
        binding.rvSong.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void executeSearch(String key) {
        adapter.getFilter().filter(key);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAlbumItemListener(Album album) {

    }
}
