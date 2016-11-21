package com.joncatanio.billme;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joncatanio.billme.model.Payer;

import java.util.ArrayList;


public class PayerListAdapter extends RecyclerView.Adapter<PayerListViewHolder> {
    private ArrayList<Payer> payers;
    private Resources resources;

    public PayerListAdapter(ArrayList<Payer> payers, Resources resources) {
        this.payers = payers;
        this.resources = resources;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.view_bill_payer_list;
    }

    @Override
    public PayerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PayerListViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(PayerListViewHolder holder, int position) {
        holder.bind(payers.get(position), resources);
    }

    @Override
    public int getItemCount() {
        return payers.size();
    }
}
