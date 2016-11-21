package com.joncatanio.billme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joncatanio.billme.model.BillFull;
import com.joncatanio.billme.model.Payer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewBillActivity extends AppCompatActivity {
    private int billId;
    private PayerListAdapter adapter;
    private Payer thisUser;
    private BillFull bill;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        Intent intent = getIntent();
        billId = intent.getIntExtra(BillViewHolder.BILL_ID, -1);
        if (billId == -1) {
            Log.e("ViewBillActivity", "No index received from intent");
        }

        fetchBillData();
    }

    private void fetchBillData() {
        String authToken = BillMeApi.getAuthToken(this);

        BillMeApi.get()
                .getBill(authToken, billId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BillFull>() {
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
                    public void onNext(BillFull bill) {
                        setScreenData(bill);
                    }
                });
    }

    private void setScreenData(BillFull bill) {
        // Get this user from the payers list
        this.bill = bill;
        thisUser = null;
        for (Payer p : bill.getPayers()) {
            if (p.getUserId() == BillMeApi.getUserId()) {
                thisUser = p;
            }
        }

        // Get view widgets
        TextView billName = (TextView) findViewById(R.id.view_bill_name);
        TextView billCost = (TextView) findViewById(R.id.view_bill_cost);
        TextView billDue = (TextView) findViewById(R.id.view_bill_due);
        RecyclerView billPayers = (RecyclerView) findViewById(R.id.view_bill_payers);
        Button billBtn = (Button) findViewById(R.id.view_bill_btn);

        billName.setText(bill.getBillName());
        billCost.setText("Total: $" + bill.getTotalAmt());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dueDate = formatter.parseDateTime(bill.getDueDate());
        DateTimeFormatter form = DateTimeFormat.forPattern("MMMM d, yyyy");
        billDue.setText("Due: " + form.print(dueDate));

        float indivCost = Float.parseFloat(bill.getTotalAmt()) / (float) bill.getPayers().size();
        if (thisUser.getPaid() == 1) {
            billBtn.setBackground(getDrawable(R.drawable.custom_button_inactive));
            billBtn.setText("Paid: $" + String.format("%.2f", indivCost));
        } else {
            billBtn.setText("Pay: $" + String.format("%.2f", indivCost));
            billBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payBill();
                }
            });
        }

        billPayers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PayerListAdapter((ArrayList<Payer>) bill.getPayers(), getResources());
        billPayers.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void payBill() {
        BillMeApi.get()
                .payBill(BillMeApi.getAuthToken(this), billId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {

                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Payment didn't go through.
                        Log.e("Pay Bill", "payment failed: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        updatePaymentStatus();
                    }
                });
    }

    private void updatePaymentStatus() {
        Button billBtn = (Button) findViewById(R.id.view_bill_btn);
        int indexOfThisUser = bill.getPayers().indexOf(thisUser);

        float indivCost = Float.parseFloat(bill.getTotalAmt()) / (float) bill.getPayers().size();
        billBtn.setBackground(getDrawable(R.drawable.custom_button_inactive));
        billBtn.setText("Paid: $" + String.format("%.2f", indivCost));
        billBtn.setClickable(false);
        thisUser.setPaid(1);
        adapter.notifyItemChanged(indexOfThisUser);
    }
}
