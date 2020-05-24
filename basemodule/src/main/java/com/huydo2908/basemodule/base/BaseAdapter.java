package com.huydo2908.basemodule.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.huydo2908.basemodule.models.BaseModel;

import java.util.ArrayList;

public abstract class BaseAdapter<T extends BaseModel> extends RecyclerView.Adapter<BaseAdapter.BaseHolder> implements Filterable {

    private ArrayList<T> data;
    private ArrayList<T> dataAll;
    private LayoutInflater inflater;
    private int resLayout;
    protected BaseListener listener;

    public BaseAdapter(LayoutInflater inflater, @LayoutRes int resLayout) {
        this.inflater = inflater;
        this.resLayout = resLayout;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
        dataAll = data;
        notifyDataSetChanged();
    }

    public void setListener(BaseListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, resLayout, parent, false);
        return new BaseHolder(binding);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class BaseHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding binding;

        public BaseHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public Filter getFilter() {
        return new FilterAdapter();
    }

    public class FilterAdapter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence key) {
            ArrayList<T> result = new ArrayList<>();
            for (T t : dataAll) {
                if (t.checkFilter(key.toString())) {
                    result.add(t);
                }
            }
            FilterResults filter = new FilterResults();
            filter.values = result;
            filter.count = result.size();
            return filter;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (ArrayList<T>) results.values;
            notifyDataSetChanged();
        }
    }
}
