package com.joncatanio.billme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joncatanio.billme.model.GroupShort;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {
    private ArrayList<GroupShort> groups;

    public GroupAdapter(ArrayList<GroupShort> groups) {
        this.groups = groups;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.group_list_item;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
