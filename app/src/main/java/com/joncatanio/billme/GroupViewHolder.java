package com.joncatanio.billme;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joncatanio.billme.model.GroupShort;

public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String GROUP_ID = "com.joncatanio.billme.GROUP_ID";
    private GroupShort listItem;
    private TextView groupName;
    private TextView groupBalance;
    private ImageView groupImg;

    public GroupViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        groupName = (TextView) itemView.findViewById(R.id.list_group_name);
        groupBalance = (TextView) itemView.findViewById(R.id.list_group_balance);
        groupImg = (ImageView) itemView.findViewById(R.id.list_group_img);
    }

    public void bind(final GroupShort listItem) {
        this.listItem = listItem;

        groupName.setText(listItem.getGroupName());
        if (listItem.getAmtOwedAsGroup().equals("None")) {
            groupBalance.setText("Group Balance: $0.00");
        } else {
            groupBalance.setText("Group Balance: $" + listItem.getAmtOwedAsGroup());
        }
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(view.getContext(), ViewGroupActivity.class);
        i.putExtra(GROUP_ID, listItem.getGroupId());
        view.getContext().startActivity(i);
    }
}
