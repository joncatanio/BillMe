package com.joncatanio.billme;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.joncatanio.billme.model.Login;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit(view);
            }
        });
    }

    public void btnSubmit(View view) {
        EditText email = (EditText) findViewById(R.id.login_email_username);
        EditText pw = (EditText) findViewById(R.id.login_pw);
        String emailUsername = email.getText().toString().trim();
        String password = pw.getText().toString().trim();

        if (emailUsername.isEmpty()) {
            Toast.makeText(this, "Please enter an email/username", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        BillMeApi.get()
                .login(emailUsername, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Login>() {
                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException httpErr = (HttpException) e;
                        Log.e("LoginActivity", "login error - " + e.getMessage());

                        if (httpErr.code() == 403) {
                            // show bad username/password error
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Invalid username/password")
                                    .show();
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Error")
                                    .setMessage(e.getMessage())
                                    .show();
                        }
                    }

                    @Override
                    public void onNext(Login login) {
                        BillMeApi.setAuthToken(login.getToken(), login.getUserId(), getApplicationContext());
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
