package com.example.yawa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private String[] rDataset;
    private Boolean main;

    RecyclerAdapter(String[] dataset, Boolean m) {
        rDataset = dataset;
        main = m;
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        RecyclerViewHolder (TextView v) {
            super(v);
            textView = v;
        }
    }

    @Override
    @NonNull
    public RecyclerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_view, parent, false);
        if (main) {
            v.setOnClickListener(MainActivity.onClickMain);
        }
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder (RecyclerViewHolder holder, int position) {
        holder.textView.setText(rDataset[position]);
    }

    @Override
    public int getItemCount() {
        return rDataset.length;
    }
}
