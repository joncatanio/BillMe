package com.joncatanio.billme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joncatanio.billme.model.GroupFull;
import com.joncatanio.billme.model.GroupShort;
import com.joncatanio.billme.model.Member;
import com.joncatanio.billme.model.NewBill;
import com.joncatanio.billme.model.NewBillResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewBillFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewBillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewBillFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static Button datePickerBtn;
    private static DateTime dueDate;

    public NewBillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewBillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewBillFragment newInstance(String param1, String param2) {
        NewBillFragment fragment = new NewBillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_bill, container, false);

        fetchContent(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void fetchContent(final View rootView) {
        datePickerBtn = (Button) rootView.findViewById(R.id.new_bill_date_picker_btn);

        DateTime curDate = new DateTime();
        DateTimeFormatter form = DateTimeFormat.forPattern("MMMM d, yyyy");
        datePickerBtn.setText(form.print(curDate));
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        BillMeApi.get()
                .getGroups(BillMeApi.getAuthToken(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GroupShort>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NewBillFragment", e.getMessage());
                    }

                    @Override
                    public void onNext(List<GroupShort> groupShorts) {
                        updateDropdown(rootView, (ArrayList<GroupShort>) groupShorts);
                    }
                });
    }

    private void updateDropdown(final View rootView, final ArrayList<GroupShort> groupShorts) {
        Spinner dropdown = (Spinner) rootView.findViewById(R.id.new_bill_group_dropdown);
        ArrayList<String> items = new ArrayList<String>();
        // Custom adapter would be awesome, too bad it is apparently a b*tch to implement.
        for (GroupShort g : groupShorts) {
            items.add(g.getGroupName());
        }
        String[] spinnerItems = items.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GroupShort group = groupShorts.get(i);
                Log.i("Group Selected", group.getGroupName());
                fetchUserList(rootView, group);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void fetchUserList(final View rootView, GroupShort group) {
        BillMeApi.get()
                .getGroup(BillMeApi.getAuthToken(getActivity()), group.getGroupId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GroupFull>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NewBillFragment", "Fetching users of group: " + e.getMessage());
                    }

                    @Override
                    public void onNext(GroupFull groupFull) {
                        updateIncludeList(rootView, groupFull);
                    }
                });
    }

    private void updateIncludeList(final View rootView, final GroupFull group) {
        RecyclerView groupMembers = (RecyclerView) rootView.findViewById(R.id.new_bill_included_members);

        groupMembers.setLayoutManager(new LinearLayoutManager(getActivity()));
        GroupMemberAdapter adapter = new GroupMemberAdapter(group.getMembers(), getResources());
        groupMembers.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Setup button now that we have a list generated
        Button btn = (Button) rootView.findViewById(R.id.new_bill_add_btn);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNewBill(rootView, group);
            }
        });
    }

    private void sendNewBill(View rootView, GroupFull group) {
        NewBill requestBody = new NewBill();
        EditText billName = (EditText) rootView.findViewById(R.id.new_bill_name);
        EditText billAmt = (EditText) rootView.findViewById(R.id.new_bill_amt);

        if (billName.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Empty Bill Name", Toast.LENGTH_SHORT).show();
            Log.e("NewBill", "Bill name was empty");
            return;
        }
        requestBody.setBillName(billName.getText().toString());

        if (billAmt.getText().toString().equals("")) {
            Toast.makeText(getActivity(), "Empty Bill Amount", Toast.LENGTH_SHORT).show();
            Log.e("NewBill", "Bill amount was empty");
            return;
        }
        Float totalCost = Float.parseFloat(billAmt.getText().toString());
        requestBody.setTotalAmt(String.format("%.2f", totalCost));

        requestBody.setGroupId(group.getGroupId().toString());
        ArrayList<String> memberIds = new ArrayList<>();
        for(Member m : group.getMembers()) {
            memberIds.add(m.getUserId().toString());
        }
        requestBody.setIncludedMembers((List) memberIds);

        DateTimeFormatter form = DateTimeFormat.forPattern("yyyy-MM-dd");
        requestBody.setDueDate(form.print(dueDate));

        BillMeApi.get()
                .addBill(BillMeApi.getAuthToken(getActivity()), requestBody)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<NewBillResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NewBill", "Request failed: " + e.getMessage());
                        Toast.makeText(getActivity(), "Error adding bill", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(NewBillResponse newBillResponse) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
    }

    private static void updateDueDateBtn(int year, int month, int day) {
        dueDate = new DateTime(year, month + 1, day, 0 ,0);
        DateTimeFormatter form = DateTimeFormat.forPattern("MMMM d, yyyy");
        datePickerBtn.setText(form.print(dueDate));
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year, month, day;
            if (dueDate == null) {
                dueDate = new DateTime();
            }
            year = dueDate.getYear();
            month = dueDate.getMonthOfYear();
            day = dueDate.getDayOfMonth();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month - 1, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            updateDueDateBtn(year, month, day);
        }
    }
}
