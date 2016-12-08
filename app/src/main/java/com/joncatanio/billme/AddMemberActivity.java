package com.joncatanio.billme;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.joncatanio.billme.model.AddMemberRequest;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        final int groupId = intent.getIntExtra(ViewGroupActivity.GROUP_ID, -1);
        if (groupId == -1) {
            Log.e("ViewBillActivity", "No index received from intent");
        }

        final EditText namemail = (EditText) findViewById(R.id.add_group_mem_username);
        Button button = (Button) findViewById(R.id.add_group_mem_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = namemail.getText().toString().trim();

                if (content == null || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Field can't be empty", Toast.LENGTH_SHORT);
                    return;
                }
                requestAddMember(content, groupId);
            }
        });
    }

    private void requestAddMember(String namemail, int groupId) {
        AddMemberRequest req = new AddMemberRequest();

        req.setGroupId(Integer.toString(groupId));
        req.setUsername(namemail);

        final AppCompatActivity self = this;
        BillMeApi.get()
                .addGroupMember(BillMeApi.getAuthToken(this), req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e == null || !(e instanceof HttpException)) {
                            Toast.makeText(self, "An Error Has Occurred", Toast.LENGTH_SHORT);
                            return;
                        }
                        HttpException exp = (HttpException) e;

                        if (exp.code() == 403) {
                            Toast.makeText(self, "You do not have permission to add members", Toast.LENGTH_SHORT);
                            return;
                        }
                        Toast.makeText(self, "Oops, something went wrong", Toast.LENGTH_LONG);
                        return;
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        finish();
                    }
                });
    }
}
