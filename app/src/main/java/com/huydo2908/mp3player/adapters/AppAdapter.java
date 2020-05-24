package com.huydo2908.mp3player.adapters;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.huydo2908.basemodule.base.BaseAdapter;
import com.huydo2908.basemodule.models.BaseModel;
import com.huydo2908.mp3player.BR;

public class AppAdapter<T extends BaseModel> extends BaseAdapter<T> {

    public AppAdapter(LayoutInflater inflater, int resLayout) {
        super(inflater, resLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.BaseHolder holder, int position) {
        T item = getData().get(position);
        holder.binding.setVariable(BR.item, item);
        if (listener != null) {
            holder.binding.setVariable(BR.listener, listener);
        }
        holder.binding.executePendingBindings();
    }
}
