package com.joncatanio.billme;

import com.joncatanio.billme.model.Bill;
import com.joncatanio.billme.model.BillFull;
import com.joncatanio.billme.model.GroupFull;
import com.joncatanio.billme.model.GroupShort;
import com.joncatanio.billme.model.Login;
import com.joncatanio.billme.model.NewBill;
import com.joncatanio.billme.model.NewBillResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface BillMeService {
    @FormUrlEncoded
    @POST("login")
    Observable<Login> login(@Field("username") String username, @Field("password") String password);

    @GET("bills/")
    Observable<List<Bill>> getBills(@Header("Authorization") String authToken);

    @GET("bill/{billId}/")
    Observable<BillFull> getBill(@Header("Authorization") String authToken, @Path("billId") int billId);

    @GET("bills/pay/{billId}/")
    Observable<Void> payBill(@Header("Authorization") String authToken, @Path("billId") int billId);

    @POST("bills/add/")
    Observable<NewBillResponse> addBill(@Header("Authorization") String authToken, @Body NewBill body);

    @GET("groups/")
    Observable<List<GroupShort>> getGroups(@Header("Authorization") String authToken);

    @GET("group/{groupId}/")
    Observable<GroupFull> getGroup(@Header("Authorization") String authToken, @Path("groupId") int groupId);
}
