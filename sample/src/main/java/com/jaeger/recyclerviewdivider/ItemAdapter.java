package com.jaeger.recyclerviewdivider;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Jaeger on 2016/12/30.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */

public class ItemAdapter extends Adapter<ItemAdapter.ItemViewHolder> {

    private final int mCount;

    public ItemAdapter(int count) {
        mCount = count;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    public static class ItemViewHolder extends ViewHolder {

        public ItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
        }
    }
}
