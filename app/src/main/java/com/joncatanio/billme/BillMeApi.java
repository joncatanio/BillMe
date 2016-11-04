package com.joncatanio.billme;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BillMeApi {

    private static final String BASE_URL = "https://api.github.com/";
    private static String authToken = "514dcffb68c06aa9a5e8f7d88c89b24d153548d815f4e418c9148dfe402c1e1a";

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

    // If null is returned the user must login and set the auth token.
    public static String getAuthToken() {
        if (authToken == null) {
            // Load from internal storage if possible
        }

        return authToken;
    }
}