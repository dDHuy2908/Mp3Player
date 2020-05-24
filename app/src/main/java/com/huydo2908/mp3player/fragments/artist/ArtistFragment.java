package com.huydo2908.mp3player.fragments.artist;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huydo2908.basemodule.base.BaseFragment;
import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.adapters.AppAdapter;
import com.huydo2908.mp3player.dao.SystemData;
import com.huydo2908.mp3player.databinding.FragmentArtistBinding;
import com.huydo2908.mp3player.models.Artist;

public class ArtistFragment extends BaseFragment<FragmentArtistBinding> implements ArtistItemListener {

    private AppAdapter<Artist> adapter;
    private SystemData systemData;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new AppAdapter<>(getLayoutInflater(), R.layout.item_artist);
        systemData = new SystemData(getContext());
        adapter.setData(systemData.getArtist());
        binding.rvArtist.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artist;
    }

    @Override
    public void executeSearch(String key) {
        adapter.getFilter().filter(key);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onArtistItemClicked(Artist artist) {

    }
}
