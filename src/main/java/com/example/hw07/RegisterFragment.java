package com.example.hw07;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterFragment extends Fragment {
    TextView name;
    TextView email;
    TextView password;
    Button register;
    Button back;
    private FirebaseAuth auth;

    public RegisterFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth  = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle(getString(R.string.Register));
        email = view.findViewById(R.id.emailRegister);
        password = view.findViewById(R.id.pwdRegister);
        register =view.findViewById(R.id.register);
        back = view.findViewById(R.id.back);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailVal = email.getText().toString();
                String pass = password.getText().toString();
                if( emailVal == null || pass == null || emailVal.equalsIgnoreCase("")
                        || pass.equalsIgnoreCase("")){
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
                    auth.createUserWithEmailAndPassword(emailVal, pass)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });


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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                
                mListener.login();
            }
        });
        return  view;
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