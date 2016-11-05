package com.joncatanio.billme;

import com.joncatanio.billme.model.Bill;
import com.joncatanio.billme.model.Login;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface BillMeService {
    @FormUrlEncoded
    @POST("login")
    Observable<Login> login(@Field("username") String username, @Field("password") String password);

    @GET("bills/")
    Observable<List<Bill>> getBills(@Header("Authorization") String authToken);
}
