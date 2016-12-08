package com.joncatanio.billme;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joncatanio.billme.model.AcceptPaymentRequest;
import com.joncatanio.billme.model.PendingPayment;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JonCatanio on 12/7/16.
 */

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.PendingViewHolder> {
    List<PendingPayment> pendingPayments;
    private Activity activity;

    public PendingAdapter(List<PendingPayment> pendingPayments, Activity activity) {
        this.pendingPayments = pendingPayments;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.pending_payment_item;
    }

    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PendingViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), this);
    }

    @Override
    public void onBindViewHolder(PendingAdapter.PendingViewHolder holder, int position) {
        holder.bind(pendingPayments.get(position), activity);
    }

    @Override
    public int getItemCount() {
        return pendingPayments.size();
    }

    public static class PendingViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImg;
        private TextView billName;
        private TextView username;
        private TextView groupName;
        private Button acceptBtn;
        private PendingAdapter adapter;

        public PendingViewHolder(View itemView, PendingAdapter adapter) {
            super(itemView);

            this.adapter = adapter;
            userImg = (ImageView) itemView.findViewById(R.id.pending_user_img);
            billName = (TextView) itemView.findViewById(R.id.pending_bill_name);
            username = (TextView) itemView.findViewById(R.id.pending_username);
            groupName = (TextView) itemView.findViewById(R.id.pending_group_name);
            acceptBtn = (Button) itemView.findViewById(R.id.pending_accept_btn);
        }

        private void notifyAdapter() {
            int index = getAdapterPosition();
            adapter.pendingPayments.remove(index);
            adapter.notifyItemChanged(index);
        }

        public void bind(final PendingPayment pendingPayment, final Activity activity) {
            byte[] img = Base64.decode(pendingPayment.getProfilePic(), Base64.DEFAULT);
            Bitmap src = BitmapFactory.decodeByteArray(img, 0, img.length);
            RoundedBitmapDrawable roundDr = RoundedBitmapDrawableFactory.create(activity.getResources(), src);
            roundDr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
            userImg.setImageDrawable(roundDr);

            billName.setText(pendingPayment.getBillName());
            username.setText(pendingPayment.getUsername());
            groupName.setText(pendingPayment.getGroupName());

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AcceptPaymentRequest body = new AcceptPaymentRequest();

                    body.setBillId(pendingPayment.getBillId());
                    body.setUserId(pendingPayment.getUserId());

                    // Context is null because we should have an auth token at this point.
                    BillMeApi.get()
                            .acceptPayment(BillMeApi.getAuthToken(null), body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Void>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e == null || !(e instanceof HttpException)) {
                                        Toast.makeText(activity, "An Error Occurred", Toast.LENGTH_SHORT);
                                        return;
                                    }

                                    HttpException exp = (HttpException) e;
                                    if (exp.code() == 403) {
                                        Toast.makeText(activity, "Unauthorized access", Toast.LENGTH_SHORT);
                                        return;
                                    }
                                    Toast.makeText(activity, "Oops, something went wrong", Toast.LENGTH_SHORT);
                                    return;
                                }

                                @Override
                                public void onNext(Void aVoid) {
                                    notifyAdapter();
                                }
                            });
                }
            });
        }
    }
}
