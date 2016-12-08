package com.joncatanio.billme;

import com.joncatanio.billme.model.AcceptPaymentRequest;
import com.joncatanio.billme.model.Account;
import com.joncatanio.billme.model.AccountUpdateRequest;
import com.joncatanio.billme.model.AddMemberRequest;
import com.joncatanio.billme.model.Bill;
import com.joncatanio.billme.model.BillFull;
import com.joncatanio.billme.model.GroupFull;
import com.joncatanio.billme.model.GroupShort;
import com.joncatanio.billme.model.Login;
import com.joncatanio.billme.model.NewBill;
import com.joncatanio.billme.model.NewBillResponse;
import com.joncatanio.billme.model.NewGroup;
import com.joncatanio.billme.model.NewGroupResponse;
import com.joncatanio.billme.model.PayBillResponse;
import com.joncatanio.billme.model.PendingPayment;

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

    @FormUrlEncoded
    @POST("register")
    Observable<Void> register(@Field("username") String username, @Field("password") String password,
                              @Field("email") String email, @Field("name") String name);

    @GET("account/")
    Observable<Account> getAccount(@Header("Authorization") String authToken);

    @POST("account/update/")
    Observable<Void> updateAccount(@Header("Authorization") String authToken, @Body AccountUpdateRequest body);

    @GET("bills/")
    Observable<List<Bill>> getBills(@Header("Authorization") String authToken);

    @GET("bills/history/")
    Observable<List<Bill>> getBillHistory(@Header("Authorization") String authToken);

    @GET("bill/{billId}/")
    Observable<BillFull> getBill(@Header("Authorization") String authToken, @Path("billId") int billId);

    @GET("bills/pending/")
    Observable<List<PendingPayment>> getPendingPayments(@Header("Authorization") String authToken);

    @GET("bills/pay/{billId}/")
    Observable<PayBillResponse> payBill(@Header("Authorization") String authToken, @Path("billId") int billId);

    @POST("bills/accept/")
    Observable<Void> acceptPayment(@Header("Authorization") String authToken, @Body AcceptPaymentRequest body);

    @POST("bills/add/")
    Observable<NewBillResponse> addBill(@Header("Authorization") String authToken, @Body NewBill body);

    @GET("bills/delete/{billId}/")
    Observable<Void> deleteBill(@Header("Authorization") String authToken, @Path("billId") int billId);

    @GET("groups/")
    Observable<List<GroupShort>> getGroups(@Header("Authorization") String authToken);

    @POST("groups/add/")
    Observable<NewGroupResponse> addGroup(@Header("Authorization") String authToken, @Body NewGroup body);

    @GET("group/{groupId}/")
    Observable<GroupFull> getGroup(@Header("Authorization") String authToken, @Path("groupId") int groupId);

    @POST("groups/addMember/")
    Observable<Void> addGroupMember(@Header("Authorization") String authToken, @Body AddMemberRequest body);
}
