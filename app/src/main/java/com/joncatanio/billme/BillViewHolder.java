package com.joncatanio.billme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.joncatanio.billme.model.Bill;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final static String BILL_ID = "com.joncatanio.billme.BILL_ID";
    public Bill listItem;
    private TextView billName;
    private TextView billCost;
    private TextView billGroup;
    private TextView remainingDays;
    private TextView daysLeftText;

    public BillViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        billName = (TextView) itemView.findViewById(R.id.list_bill_name);
        billCost = (TextView) itemView.findViewById(R.id.list_bill_cost);
        billGroup = (TextView) itemView.findViewById(R.id.list_bill_group);
        remainingDays = (TextView) itemView.findViewById(R.id.list_bill_remaining);
        daysLeftText = (TextView) itemView.findViewById(R.id.list_bill_days_left);
    }

    public void bind(final Bill listItem) {
        this.listItem = listItem;

        // Set name, costs and group information.
        billName.setText(listItem.getBillName());
        billName.setTextColor(Color.parseColor("#DA000000"));

        double totalCost = Double.parseDouble(listItem.getTotalAmt());
        double userCost = totalCost / (double) listItem.getNumPayers();
        billCost.setText("$" + String.format("%.2f", userCost));
        billCost.setTextColor(Color.parseColor("#DA000000"));
        billGroup.setText(listItem.getGroupName());
        billGroup.setTextColor(Color.parseColor("#8D000000"));

        // Calculate days left on the bill
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dueDate = formatter.parseDateTime(listItem.getDueDate());
        DateTime curDate = new DateTime();
        int daysLeft = Days.daysBetween(curDate.withTimeAtStartOfDay(),
                dueDate.withTimeAtStartOfDay()).getDays();
        remainingDays.setText(Integer.toString(daysLeft));
        // Set color and text size from various days left on bill
        if (daysLeft < 4) {
            remainingDays.setTextColor(Color.parseColor("#E57373"));
            daysLeftText.setTextColor(Color.parseColor("#E57373"));
        } else if (daysLeft >= 5 && daysLeft <= 14) {
            remainingDays.setTextColor(Color.parseColor("#FFF176"));
            daysLeftText.setTextColor(Color.parseColor("#FFF176"));
        } else {
            remainingDays.setTextColor(Color.parseColor("#81C784"));
            daysLeftText.setTextColor(Color.parseColor("#81C784"));
        }
    }

    @Override
    public void onClick(View view) {
        // Pass the bill id on through to make another API call
        Intent i = new Intent(view.getContext(), ViewBillActivity.class);
        i.putExtra(BILL_ID, listItem.getBillId());
        view.getContext().startActivity(i);
    }
}
