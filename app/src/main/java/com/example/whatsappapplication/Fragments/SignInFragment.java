package com.example.whatsappapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.whatsappapplication.Activities.MainActivity;
import com.example.whatsappapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class SignInFragment extends Fragment {


    private TextInputEditText editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignInFragment";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Users");
    public SignInFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        editTextEmail = view.findViewById(R.id.EditTextEmailLogin);
        editTextPassword = view.findViewById(R.id.EditTextPasswordLogin);
        Button button = view.findViewById(R.id.buttonSignIn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkFields())
                {
                    String email = editTextEmail.getText().toString();

                    String password = editTextPassword.getText().toString();
                    signIn(email,password);
                }
                else
                {
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateLastSeen(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Email / Password Incorrect!",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        getActivity().finish();
    }


    private boolean checkFields()
    {
        if(editTextEmail.getText().toString().isEmpty())
        {
            return false;
        }

        if(editTextPassword.getText().toString().isEmpty())
        {
            return false;
        }

        return true;
    }

    private void updateLastSeen(final FirebaseUser user)
    {

        myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> UserValues = new HashMap();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserValues.put(snapshot.getKey(),snapshot.getValue());
                }

                UserValues.put("lastSeen",System.currentTimeMillis());

                myRef.child(user.getUid()).updateChildren(UserValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}