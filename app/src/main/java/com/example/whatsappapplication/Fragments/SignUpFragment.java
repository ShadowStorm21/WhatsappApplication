package com.example.whatsappapplication.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.whatsappapplication.Activities.MainActivity;
import com.example.whatsappapplication.Models.Users;
import com.example.whatsappapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUpFragment extends Fragment {

    private static final String TAG = "SignUpFragment";
    private TextInputEditText editTextEmail,editTextUsername,editTextPassword,editTextConfirmPassword;
    private CircleImageView circleImageView;
    private FirebaseStorage mStorageRef;
    private Uri selectedImage = null;
    private FirebaseAuth mAuth;
    private Uri uri;
    private FirebaseUser firebaseUser;

    public SignUpFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        editTextEmail = view.findViewById(R.id.EditTextEmailSignup);
        editTextUsername = view.findViewById(R.id.EditTextUser);
        editTextPassword = view.findViewById(R.id.EditTextPasswordSignup);
        editTextConfirmPassword = view.findViewById(R.id.EditTextConfirm);
        circleImageView = view.findViewById(R.id.profile_image);
        Button button = view.findViewById(R.id.buttonSignUp);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkFields())
                {
                    if(checkMatch())
                    {
                        String email = editTextEmail.getText().toString();
                        String password = editTextPassword.getText().toString();
                            registerNewUser(email,password);

                    }
                }
                else
                {
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermissions())
                {
                    getImage();
                }
                else
                {
                    requestPermissions();
                }
            }
        });
        return view;
    }


    private boolean checkFields()
    {
        if(editTextEmail.getText().toString().isEmpty())
        {
            return false;
        }
        if(editTextUsername.getText().toString().isEmpty())
        {
            return false;
        }
        if(editTextPassword.getText().toString().isEmpty())
        {
            return false;
        }
        if(editTextConfirmPassword.getText().toString().isEmpty())
        {
            return false;
        }
        return true;
    }

    private boolean checkMatch()
    {
        String email = editTextEmail.getText().toString();
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);   // check for valid email
        Matcher matcher = pattern.matcher(email);

        if(!matcher.matches())
        {
            Toast.makeText(getContext(), "Invalid email format!", Toast.LENGTH_SHORT).show();
            return false;
        }

        String password = editTextPassword.getText().toString();
        String cPassword = editTextConfirmPassword.getText().toString();

        if(!password.matches(cPassword))
        {
            Toast.makeText(getContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

    private boolean checkPermissions()
    {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        return false;
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getImage();
            }
            else
            {
                requestPermissions();
            }
        }
    }

    private void getImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000);
    }

    private void registerNewUser(String email,String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            firebaseUser = mAuth.getCurrentUser();
                            updateUI(firebaseUser);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        uploadImageToFirebaseStorage();
                    }
                });


            }
        });

    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        getActivity().finish();
    }

    private void uploadImageToFirebaseStorage()
    {

        if(selectedImage != null) {
            String img_id = UUID.randomUUID().toString();
            final StorageReference reference = mStorageRef.getReference("ProfilePics").child(img_id + ".jpg");
            reference.putFile(selectedImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uri = task.getResult();
                       Log.i(TAG,uri.toString());
                       insertData(firebaseUser);

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });

        }
        else
        {
            circleImageView.setImageResource(R.drawable.ic_baseline_person_pin_24);
            String x = "default";
            uri = Uri.parse(x);
            insertData(firebaseUser);

        }


    }


    private void insertData(FirebaseUser firebaseUser) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        Users user = new Users(firebaseUser.getEmail(), firebaseUser.getUid(), editTextUsername.getText().toString(), uri.toString(), System.currentTimeMillis());
        myRef.child(firebaseUser.getUid()).setValue(user);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    circleImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}