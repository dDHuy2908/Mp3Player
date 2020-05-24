package com.huydo2908.mp3player.fragments.song;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huydo2908.basemodule.base.BaseFragment;
import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.activities.MainActivity;
import com.huydo2908.mp3player.adapters.AppAdapter;
import com.huydo2908.mp3player.dao.SystemData;
import com.huydo2908.mp3player.databinding.FragmentSongBinding;
import com.huydo2908.mp3player.models.Song;
import com.huydo2908.mp3player.service.Mp3Service;

public class SongFragment extends BaseFragment<FragmentSongBinding> implements SongItemListener{

    private AppAdapter<Song> adapter;
    private SystemData systemData;
    private Mp3Service service;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new AppAdapter<>(getLayoutInflater(), R.layout.item_song);
        systemData = new SystemData(getContext());
        adapter.setData(systemData.getSong());
        binding.rvSong.setAdapter(adapter);
        adapter.setListener(this);

        MainActivity activity = (MainActivity) getActivity();
        service = activity.getService();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song;
    }

    @Override
    public void executeSearch(String key) {
        adapter.getFilter().filter(key);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSongItemClicked(Song song) {
        int index = adapter.getData().indexOf(song);
        service.setSongData(adapter.getData());
        service.getController().create(index);
    }
}
