package com.example.hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    String userId;
    Button track;
    Button history;
    Button button6;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle(getString(R.string.Home));
        track = view.findViewById(R.id.button);
        history = view.findViewById(R.id.button2);
        button6 = view.findViewById(R.id.button6);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.track(userId);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.history(userId);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.login();
            }
        });
        return view;
    }
    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof IListener){
            mListener = (IListener) context;
        }else{
            throw new RuntimeException(context.toString() + getString(R.string.InterfaceMsg));
        }

    }
}