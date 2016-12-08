package com.joncatanio.billme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.joncatanio.billme.model.PendingPayment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PendingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PendingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingFragment newInstance(String param1, String param2) {
        PendingFragment fragment = new PendingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_pending, container, false);

        fetchContent(rootView);

        return rootView;
    }

    private void fetchContent(final View rootView) {
        BillMeApi.get()
                .getPendingPayments(BillMeApi.getAuthToken(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PendingPayment>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e == null || !(e instanceof HttpException)) {
                            Toast.makeText(getContext(), "An Error Has Occurred", Toast.LENGTH_LONG);
                            return;
                        }

                        HttpException exp = (HttpException) e;
                        if (exp.code() == 403) {
                            Toast.makeText(getContext(), "Not permitted", Toast.LENGTH_SHORT);
                            return;
                        }
                        Toast.makeText(getContext(), "Oops, something went wrong", Toast.LENGTH_SHORT);
                        return;
                    }

                    @Override
                    public void onNext(List<PendingPayment> pendingPayments) {
                        setupView(rootView, pendingPayments);
                    }
                });
    }

    private void setupView(View rootView, List<PendingPayment> pendingPayments) {
        RecyclerView rc = (RecyclerView) rootView.findViewById(R.id.pending_recycler_view);
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (pendingPayments == null) {
            rc.setAdapter(new PendingAdapter(new ArrayList<PendingPayment>(), getActivity()));
        } else {
            rc.setAdapter(new PendingAdapter(pendingPayments, getActivity()));
        }
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
}
