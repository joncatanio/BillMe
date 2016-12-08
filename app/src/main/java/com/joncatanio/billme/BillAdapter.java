package com.joncatanio.billme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.joncatanio.billme.model.Bill;

import java.util.ArrayList;

public class BillAdapter extends RecyclerView.Adapter<BillViewHolder> {
    public ArrayList<Bill> bills;

    public BillAdapter(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.bill_list_item;
    }

    @Override
    public BillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), this);
    }

    @Override
    public void onBindViewHolder(BillViewHolder holder, int position) {
        holder.bind(bills.get(position));
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }
}
