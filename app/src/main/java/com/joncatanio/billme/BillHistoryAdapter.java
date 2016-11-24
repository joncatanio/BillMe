package com.joncatanio.billme;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joncatanio.billme.model.Bill;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import static com.joncatanio.billme.BillViewHolder.BILL_ID;

public class BillHistoryAdapter extends RecyclerView.Adapter<BillHistoryAdapter.BillHistoryViewHolder> {
    private ArrayList<Bill> bills;

    public BillHistoryAdapter(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.bill_list_item_history;
    }

    @Override
    public BillHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BillHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(BillHistoryViewHolder holder, int position) {
        holder.bind(bills.get(position));
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public static class BillHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Bill listItem;
        private TextView billName;
        private TextView billDue;
        private TextView billTotal;
        private TextView billPaid;

        public BillHistoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            billName = (TextView) itemView.findViewById(R.id.list_bill_history_name);
            billDue = (TextView) itemView.findViewById(R.id.list_bill_history_due_date);
            billTotal = (TextView) itemView.findViewById(R.id.list_bill_history_total);
            billPaid = (TextView) itemView.findViewById(R.id.list_bill_history_paid);
        }

        public void bind(Bill listItem) {
            this.listItem = listItem;

            billName.setText(listItem.getBillName());

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime dueDate = formatter.parseDateTime(listItem.getDueDate());
            DateTimeFormatter form = DateTimeFormat.forPattern("MMMM d, yyyy");
            billDue.setText(form.print(dueDate));

            billTotal.setText("Total: " + listItem.getTotalAmt());

            float indivCost = Float.parseFloat(listItem.getTotalAmt()) / (float) listItem.getNumPayers();
            billPaid.setText("Paid: " + String.format("%.2f", indivCost));
        }

        @Override
        public void onClick(View view) {
            // Pass the bill id on through to make another API call
            Intent i = new Intent(view.getContext(), ViewBillActivity.class);
            i.putExtra(BILL_ID, listItem.getBillId());
            view.getContext().startActivity(i);
        }
    }
}
