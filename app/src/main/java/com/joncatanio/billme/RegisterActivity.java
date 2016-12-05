package com.joncatanio.billme;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        String name = ((EditText) findViewById(R.id.register_name)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.register_email)).getText().toString().trim();
        String username = ((EditText) findViewById(R.id.register_username)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.register_password)).getText().toString();
        String repeatPassword = ((EditText) findViewById(R.id.register_repeatPassword)).getText().toString();

        if (!password.equals(repeatPassword)) {
            Toast.makeText(this, R.string.password_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }

        final AppCompatActivity self = this;
        BillMeApi.get()
                .register(username, password, email, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        // do nothin'
                    }

                    @Override
                    public void onError(Throwable e) {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("Error")
                                .setMessage(e.getMessage())
                                .show();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        // register with the server and go back to login
                        Intent intent = new Intent(self, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
