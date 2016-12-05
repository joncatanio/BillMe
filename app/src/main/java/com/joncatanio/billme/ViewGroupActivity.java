package com.joncatanio.billme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.joncatanio.billme.model.GroupFull;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewGroupActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        Intent intent = getIntent();
        int groupId = intent.getIntExtra(GroupViewHolder.GROUP_ID, -1);
        if (groupId == -1) {
            Log.e("ViewGroupActivity", "No index received from intent");
        }

        fetchGroupData(groupId);
    }

    private void fetchGroupData(int groupId) {
        String authToken = BillMeApi.getAuthToken(this);

        BillMeApi.get()
                .getGroup(authToken, groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GroupFull>() {
                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException httpErr = (HttpException) e;

                        if (httpErr.code() == 403) {
                            // The user has an invalid/expired token, make them log in.
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e("ViewBillActivity", e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(GroupFull group) {
                        setScreenData(group);
                    }
                });
    }

    private void setScreenData(GroupFull group) {
        ImageView groupImg = (ImageView) findViewById(R.id.view_group_img);
        TextView groupName = (TextView) findViewById(R.id.view_group_name);
        Button addMemberBtn = (Button) findViewById(R.id.view_group_btn);
        RecyclerView groupMembers = (RecyclerView) findViewById(R.id.view_group_member_list);

        groupName.setText(group.getGroupName());

        // Decode string and set group image
        byte[] img = Base64.decode(group.getGroupImg(), Base64.DEFAULT);
        Bitmap src = BitmapFactory.decodeByteArray(img, 0, img.length);
        groupImg.setImageBitmap(src);

        // Setup recycler view for group members
        groupMembers.setLayoutManager(new LinearLayoutManager(this));
        GroupMemberAdapter adapter = new GroupMemberAdapter(group.getMembers(), getResources());
        groupMembers.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ViewGroupActivity", "add member pressed");
                Intent intent = new Intent(ViewGroupActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });
    }
}
