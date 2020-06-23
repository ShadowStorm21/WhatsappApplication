package com.example.whatsappapplication.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsappapplication.Adapters.ContactsAdapter;
import com.example.whatsappapplication.Models.Users;
import com.example.whatsappapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ListView listView;
    private List<Users> usersList;
    private final String TAG = ContactsActivity.this.getClass().getSimpleName();
    private ContactsAdapter contactsAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        listView = findViewById(R.id.contactsListView);
        usersList = (List<Users>) getIntent().getSerializableExtra("contactsList");

        if(usersList.get(0).getUid() == null)
        {
            getUsers();

        }

        contactsAdapter = new ContactsAdapter(this,usersList);
        listView.setAdapter(contactsAdapter);


    }

    private void getUsers()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        usersList.clear();
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String uid = firebaseUser.getUid();
                Log.i(TAG,"user_id :"+uid);
                for(DataSnapshot users : dataSnapshot.getChildren())
                {

                    Users user = users.getValue(Users.class);
                    if(!user.getUid().equals(uid))
                    {
                        usersList.add(user);
                    }

                    Log.i(TAG, "user_id from database :" + user.getUid());

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }




}