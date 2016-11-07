package com.joncatanio.billme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.joncatanio.billme.model.GroupFull;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
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
                .subscribe(new Subscriber<GroupFull>() {
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
        TextView groupName = (TextView) findViewById(R.id.view_group_name);

        groupName.setText(group.getGroupName());
    }
}
