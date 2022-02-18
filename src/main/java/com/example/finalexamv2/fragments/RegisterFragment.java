package com.example.finalexamv2.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalexamv2.R;
import com.example.finalexamv2.models.Services;
import com.example.finalexamv2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    RegInterface am;
    Button sub_btn;
    TextView reg_cancel;
    EditText txt_name, txt_email, txt_pass;
    private FirebaseAuth mAuth;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof RegInterface){
            am = (RegInterface) context;
        }else{
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        am.setTitle(R.string.register_account);
        txt_name = view.findViewById(R.id.reg_name);
        txt_email = view.findViewById(R.id.reg_email);
        txt_pass = view.findViewById(R.id.reg_password);
        sub_btn = view.findViewById(R.id.button_submit);
        reg_cancel = view.findViewById(R.id.reg_cancel);

        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_name.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || txt_pass.getText().toString().isEmpty()){
                    am.alert(getString(R.string.empty_fields));
                    return;
                }
                am.toggleDialog(true);
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(txt_email.getText().toString(), txt_pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                am.toggleDialog(false);
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest.Builder upcr = new UserProfileChangeRequest.Builder();
                                    upcr.setDisplayName(txt_name.getText().toString());
                                    user.updateProfile(upcr.build());

                                    am.toggleDialog(true);

                                    User user1 = new User();
                                    user1.setId(user.getUid());
                                    user1.setDisplay_name(txt_name.getText().toString());
                                    user1.setEmail(txt_email.getText().toString());

                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection(Services.DB_USERS).document(user.getUid()).set(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            am.toggleDialog(false);
                                            am.setUser(user1);
                                            am.startMainActivity();
                                        }
                                    });
                                } else {
                                    am.alert(task.getException().getMessage());
                                }
                            }
                        });
            }
        });
        reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.goBack();
            }
        });
        return view;
    }

    public interface RegInterface{

        void alert(String msg);

        void goBack();

        void setUser(User user);

        void toggleDialog(boolean show);

        void setTitle(int title_id);

        void sendLoginFragment();

        void startMainActivity();

    }

}