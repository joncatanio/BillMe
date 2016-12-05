package com.joncatanio.billme;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joncatanio.billme.model.Payer;

public class PayerListViewHolder extends RecyclerView.ViewHolder {
    private Payer payer;
    private TextView payerName;
    private ImageView payerProfilePic;
    private ImageView payerOwnerStatus;
    private ImageView payerStatus;

    public PayerListViewHolder(View itemView) {
        super(itemView);

        payerName = (TextView) itemView.findViewById(R.id.payer_list_name);
        payerProfilePic = (ImageView) itemView.findViewById(R.id.payer_profile_pic);
        payerOwnerStatus = (ImageView) itemView.findViewById(R.id.payer_owner_status);
        payerStatus = (ImageView) itemView.findViewById(R.id.payer_status);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bind(final Payer payer, Resources resources) {
        this.payer = payer;

        payerName.setText(payer.getName());
        if (payer.getPaid() == 1) {
            payerStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_check_green_24dp, null));
        } else if (payer.getPending() == 1) {
            payerStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_access_time_yellow_24dp, null));
        } else {
            payerStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_clear_red_24dp, null));
        }
        if (payer.getOwner() == 1) {
            payerOwnerStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_cash_grey_24dp, null));
        }
        byte[] img = Base64.decode(payer.getProfilePic(), Base64.DEFAULT);
        Bitmap src = BitmapFactory.decodeByteArray(img, 0, img.length);
        RoundedBitmapDrawable roundDr = RoundedBitmapDrawableFactory.create(resources, src);
        roundDr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        payerProfilePic.setImageDrawable(roundDr);
    }
}