package com.joncatanio.billme;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.joncatanio.billme.model.Payer;

/**
 * Created by JonCatanio on 11/6/16.
 */

public class PayerListViewHolder extends RecyclerView.ViewHolder {
    private Payer payer;
    private TextView payerName;

    public PayerListViewHolder(View itemView) {
        super(itemView);

        payerName = (TextView) itemView.findViewById(R.id.payer_list_name);
    }

    public void bind(final Payer payer) {
        this.payer = payer;

        payerName.setText(payer.getName());
    }
}
