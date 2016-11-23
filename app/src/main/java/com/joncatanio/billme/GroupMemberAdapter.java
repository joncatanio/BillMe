package com.joncatanio.billme;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joncatanio.billme.model.Member;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder> {
    private ArrayList<Member> members;
    private static Resources resources;

    public GroupMemberAdapter(List<Member> members, Resources resources) {
        this.members = (ArrayList<Member>) members;
        this.resources = resources;
    }

    @Override
    public GroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupMemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupMemberViewHolder holder, int position) {
        holder.bind(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.group_member_list_item;
    }

    public static class GroupMemberViewHolder extends RecyclerView.ViewHolder {
        private ImageView memberProfilePic;
        private TextView memberName;
        private TextView memberUsername;

        public GroupMemberViewHolder(View itemView) {
            super(itemView);

            memberProfilePic = (ImageView) itemView.findViewById(R.id.group_member_profile_pic);
            memberName = (TextView) itemView.findViewById(R.id.group_member_name);
            memberUsername = (TextView) itemView.findViewById(R.id.group_member_username);
        }

        public void bind(Member member) {
            byte[] img = Base64.decode(member.getProfilePic(), Base64.DEFAULT);
            Bitmap src = BitmapFactory.decodeByteArray(img, 0, img.length);
            RoundedBitmapDrawable roundDr = RoundedBitmapDrawableFactory.create(resources, src);
            roundDr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
            memberProfilePic.setImageDrawable(roundDr);

            memberName.setText(member.getName());
            memberUsername.setText("Username: " + member.getUsername());
        }
    }
}
