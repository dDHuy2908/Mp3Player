package com.huydo2908.mp3player.views;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.huydo2908.mp3player.R;
import com.huydo2908.mp3player.activities.MainActivity;
import com.huydo2908.mp3player.activities.PlayActivity;
import com.huydo2908.mp3player.databinding.BottomControllerBinding;
import com.huydo2908.mp3player.models.SongInfo;
import com.huydo2908.mp3player.service.Mp3Service;

public class ControllerView extends FrameLayout implements View.OnClickListener, ControllerListener, SeekBar.OnSeekBarChangeListener {

    private BottomControllerBinding binding;
    private Mp3Service service;

    public ControllerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        binding = BottomControllerBinding.inflate(
                LayoutInflater.from(getContext()),
                this,
                true
        );
        Intent intent = new Intent(getContext(), Mp3Service.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

        binding.setListener(this);

        setOnClickListener(this);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder b) {
            Mp3Service.Mp3Binder binder = (Mp3Service.Mp3Binder) b;
            service = binder.getService();
            bindView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void bindView() {
        MainActivity act = (MainActivity) getContext();
        service.getInfo().observe(act, new Observer<SongInfo>() {
            @Override
            public void onChanged(SongInfo songInfo) {
                binding.setInfo(songInfo);
                setVisibility(songInfo.isStarting() ? VISIBLE : GONE);
            }
        });
        binding.sbTime.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), PlayActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.controller_enter, R.anim.controller_exit);
        getContext().startActivity(intent, options.toBundle());
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unbindService(connection);
    }
}
