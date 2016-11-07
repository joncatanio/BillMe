package com.joncatanio.billme;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joncatanio.billme.model.GroupShort;

public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String GROUP_ID = "com.joncatanio.billme.GROUP_ID";
    private GroupShort listItem;
    private TextView groupName;

    public GroupViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        groupName = (TextView) itemView.findViewById(R.id.list_group_name);
    }

    public void bind(final GroupShort listItem) {
        this.listItem = listItem;

        groupName.setText(listItem.getGroupName());
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(view.getContext(), ViewGroupActivity.class);
        i.putExtra(GROUP_ID, listItem.getGroupId());
        view.getContext().startActivity(i);
    }
}
