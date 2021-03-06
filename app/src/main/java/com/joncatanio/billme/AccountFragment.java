package com.joncatanio.billme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joncatanio.billme.model.Account;
import com.joncatanio.billme.model.AccountUpdateRequest;
import com.joncatanio.billme.model.Bill;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int FETCH_IMAGE = 21;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // View variables
    private ImageView userImg;
    private Bitmap bmpImg;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        fetchContent(rootView);
        return rootView;
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

    private void fetchContent(final View rootView) {
        BillMeApi.get()
                .getAccount(BillMeApi.getAuthToken(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Account>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("AccountFragment", "error getting account info");
                    }

                    @Override
                    public void onNext(Account account) {
                        populateAccountInfo(rootView, account);
                    }
                });
    }

    private void populateAccountInfo(final View rootView, final Account account) {
        userImg = (ImageView) rootView.findViewById(R.id.account_profile_pic);
        final EditText userFullname = (EditText) rootView.findViewById(R.id.account_fullname);
        final TextView userEmail = (TextView) rootView.findViewById(R.id.account_email);
        final EditText username = (EditText) rootView.findViewById(R.id.account_username);
        Button saveBtn = (Button) rootView.findViewById(R.id.account_save_btn);

        byte[] img = Base64.decode(account.getProfilePic(), Base64.DEFAULT);
        bmpImg = BitmapFactory.decodeByteArray(img, 0, img.length);
        userImg.setImageBitmap(bmpImg);
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, FETCH_IMAGE);
            }
        });

        userFullname.setText(account.getName());
        userEmail.setText(account.getEmail());
        username.setText(account.getUsername());

        BillMeApi.get()
                .getBillHistory(BillMeApi.getAuthToken(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Bill>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Account", "bill history failed");
                    }

                    @Override
                    public void onNext(List<Bill> bills) {
                        populatePaymentHistory(rootView, account, (ArrayList<Bill>) bills);
                    }
                });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountUpdateRequest body = new AccountUpdateRequest();

                body.setUsername(username.getText().toString().trim());
                body.setEmail(userEmail.getText().toString().trim());
                body.setName(userFullname.getText().toString().trim());

                if (bmpImg == null) {
                    body.setUserImg("");
                } else {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bmpImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    body.setUserImg(Base64.encodeToString(byteArray, Base64.DEFAULT));
                }

                BillMeApi.get()
                        .updateAccount(BillMeApi.getAuthToken(getActivity()), body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e == null || !(e instanceof HttpException)) {
                                    Toast.makeText(getContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                HttpException exp = (HttpException) e;
                                if (exp.code() == 403) {
                                    Toast.makeText(getContext(), "Session expired please log back in", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Toast.makeText(getContext(), "Account Saved", Toast.LENGTH_SHORT).show();
                                mListener.onFragmentInteraction(new Uri.Builder().fragment(MainActivity.UPDATE_PROFILE).build());
                            }
                        });
            }
        });
    }

    private void populatePaymentHistory(View rootView, Account account, ArrayList<Bill> bills) {
        RecyclerView billHistory = (RecyclerView) rootView.findViewById(R.id.account_bill_history);

        billHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        BillHistoryAdapter adapter = new BillHistoryAdapter(bills);
        billHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FETCH_IMAGE) {
                Uri uri = data.getData();

                try {
                    bmpImg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    userImg.setImageBitmap(bmpImg);
                } catch (IOException e) {
                    Log.e("EditTodoFragment", "onActivityResult: " + e.getLocalizedMessage());
                }
            }
        }
    }
}
