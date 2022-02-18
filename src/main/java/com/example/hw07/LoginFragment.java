package com.example.hw07;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;


public class LoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button login;
    Button create;
    private FirebaseAuth auth;
    public String uName;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle(getString(R.string.Login));
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        create = view.findViewById(R.id.NewAccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailVal = email.getText().toString();
                String passwordVal = password.getText().toString();
                if(emailVal== null || emailVal.equalsIgnoreCase("")|| passwordVal.equalsIgnoreCase("")||passwordVal == null ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getResources().getString(R.string.dataerror))
                            .setTitle(getResources().getString(R.string.error))
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });

                    builder.create().show();
                }
                else{
                    auth.signInWithEmailAndPassword(emailVal, passwordVal)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mListener.home(emailVal);

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage(task.getException().toString())
                                                .setTitle(getResources().getString(R.string.error))
                                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });

                                        builder.create().show();
                                    }
                                }
                            });
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.register();
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