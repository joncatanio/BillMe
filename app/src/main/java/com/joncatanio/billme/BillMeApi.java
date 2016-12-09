package com.joncatanio.billme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BillMeApi {
    private static final String BASE_URL = "http://ec2-54-153-9-233.us-west-1.compute.amazonaws.com/";//10.0.2.2:5000/";
    private static final String TOKEN_FILE = "token";
    private static String authToken = null;
    private static int userId;

    private static BillMeService sService;

    public static BillMeService get() {
        if (sService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            sService = retrofit.create(BillMeService.class);
        }

        return sService;
    }

    public static void resetAuthToken(Context context) {
        authToken = null;
        new File(context.getFilesDir(), TOKEN_FILE).delete();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    // If null is returned the user must login and set the auth token.
    public static String getAuthToken(Activity context) {
        if (authToken == null) {
            try {
                File file = new File(context.getFilesDir(), TOKEN_FILE);
                if (file.exists()) {
                    FileInputStream fis = context.openFileInput(TOKEN_FILE);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buf = new BufferedReader(isr);
                    authToken = buf.readLine();
                    userId = Integer.parseInt(buf.readLine());
                } else {
                    // Show login screen and get new token.
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    context.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return authToken;
    }

    public static void setAuthToken(String token, Integer uid, Context context) {
        FileOutputStream outstream;
        authToken = token;
        userId = uid;

        try {
            outstream = context.openFileOutput(TOKEN_FILE, Context.MODE_PRIVATE);
            outstream.write(token.getBytes());
            outstream.write("\n".getBytes());
            outstream.write(uid.toString().getBytes());
            outstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getUserId() {
        return userId;
    }
}