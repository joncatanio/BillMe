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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.joncatanio.billme.model.NewGroup;
import com.joncatanio.billme.model.NewGroupResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGroupFragment extends Fragment {
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
    private ImageView groupImg;
    private Bitmap bmpImg;

    public NewGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewGroupFragment newInstance(String param1, String param2) {
        NewGroupFragment fragment = new NewGroupFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_new_group, container, false);

        setupContent(rootView);

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

    private void setupContent(View rootView) {
        groupImg = (ImageView) rootView.findViewById(R.id.new_group_img);
        groupImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_insert_photo_grey_24dp, null));
        final EditText groupName = (EditText) rootView.findViewById(R.id.new_group_name);
        Button groupAddBtn = (Button) rootView.findViewById(R.id.new_group_add_btn);

        groupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, FETCH_IMAGE);
            }
        });

        groupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Need Group Name", Toast.LENGTH_SHORT).show();
                    Log.e("Add Group", "No group name specified");
                    return;
                }

                NewGroup requestBody = new NewGroup();
                requestBody.setGroupName(groupName.getText().toString());

                if (bmpImg == null) {
                    requestBody.setGroupImg("");
                } else {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bmpImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    requestBody.setGroupImg(Base64.encodeToString(byteArray, Base64.DEFAULT));
                }

                BillMeApi.get()
                        .addGroup(BillMeApi.getAuthToken(getActivity()), requestBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NewGroupResponse>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("New Group", "Error adding group");
                                Toast.makeText(getContext(), "Error Adding Group", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(NewGroupResponse newGroupResponse) {
                                getActivity().getSupportFragmentManager().beginTransaction().remove(NewGroupFragment.this).commit();
                                getActivity().getSupportFragmentManager().popBackStack();
                                Intent intent = new Intent(getContext(), ViewGroupActivity.class);
                                intent.putExtra(GroupViewHolder.GROUP_ID, newGroupResponse.getGroupId());
                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FETCH_IMAGE) {
                Uri uri = data.getData();

                try {
                    bmpImg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    groupImg.setImageDrawable(null);
                    groupImg.setImageBitmap(bmpImg);
                } catch (IOException e) {
                    Log.e("EditTodoFragment", "onActivityResult: " + e.getLocalizedMessage());
                }
            }
        }
    }
}
