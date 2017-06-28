package com.korzh.hometask.backgroundjob.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korzh.hometask.backgroundjob.R;
import com.korzh.hometask.backgroundjob.callback.LastItemVisibleCallBack;
import com.korzh.hometask.backgroundjob.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 6/28/17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Model> mItems = new ArrayList<>();
    private LastItemVisibleCallBack mLastItemVisibleCallBack;

    public Adapter(LastItemVisibleCallBack lastItemVisibleCallBack) {
        mLastItemVisibleCallBack = lastItemVisibleCallBack;
    }

    public void addItems(List<Model> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvUserId;
        TextView mTvId;
        TextView mTvTitle;
        TextView mTvBody;

        ViewHolder(View v) {
            super(v);
            mTvUserId = (TextView)v.findViewById(R.id.tv_user_id);
            mTvId = (TextView)v.findViewById(R.id.tv_id);
            mTvTitle = (TextView)v.findViewById(R.id.tv_title);
            mTvBody = (TextView)v.findViewById(R.id.tv_body);
        }
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvUserId.setText(mItems.get(position).getUserId());
        holder.mTvId.setText(mItems.get(position).getId());
        holder.mTvTitle.setText(mItems.get(position).getTitle());
        holder.mTvBody.setText(mItems.get(position).getBody());

        if (position == mItems.size() - 1) {
            mLastItemVisibleCallBack.loadNextPage();
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}


