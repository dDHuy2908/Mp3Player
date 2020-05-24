package com.huydo2908.mp3player.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.SeekBar;

import androidx.lifecycle.Observer;

import com.huydo2908.basemodule.base.BaseActivity;
import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.databinding.ActivityPlayBinding;
import com.huydo2908.mp3player.models.SongInfo;
import com.huydo2908.mp3player.service.Mp3Service;
import com.huydo2908.mp3player.views.ControllerListener;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> implements SeekBar.OnSeekBarChangeListener, ControllerListener {

    private Mp3Service service;

    @Override
    protected void init() {
        Intent intent = new Intent(PlayActivity.this, Mp3Service.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        binding.setListener(this);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder b) {
            Mp3Service.Mp3Binder binder = (Mp3Service.Mp3Binder) b;
            PlayActivity.this.service = binder.getService();
            bindView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindView() {
        service.getInfo().observe(this, new Observer<SongInfo>() {
            @Override
            public void onChanged(SongInfo songInfo) {
                binding.setInfo(songInfo);
            }
        });
        binding.sbTime.setOnSeekBarChangeListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.controller_enter, R.anim.controller_exit);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            service.getController().seek(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onNext() {
        service.getController().change(1);
    }

    @Override
    public void onPrevious() {
        service.getController().change(-1);
    }

    @Override
    public void onPausePlay() {
        if (service.getController().isPlaying()) {
            service.getController().pause();
        } else {
            service.getController().start();
        }
    }
}
