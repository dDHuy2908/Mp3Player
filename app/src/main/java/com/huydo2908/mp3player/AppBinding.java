package com.huydo2908.mp3player;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppBinding {

    @BindingAdapter("time")
    public static void setTime(TextView tv, int time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        tv.setText(format.format(new Date(time)));
    }
}
