package com.example.whatsappapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
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


        usersList = new ArrayList<>();

        contactsAdapter = new ContactsAdapter(this,usersList);
        listView.setAdapter(contactsAdapter);
        getUsers();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ContactsActivity.this,ChatActivity.class);
                intent.putExtra("ReceiverUsername",usersList.get(position).getUsername());
                intent.putExtra("ReceiverLastSeen",usersList.get(position).getLastSeen());
                intent.putExtra("ReceiverUserId",usersList.get(position).getUid());
                intent.putExtra("ReceiverPhotoUri",usersList.get(position).getPhotoUrl());
                startActivity(intent);
                finish();
            }
        });



    }

    private void getUsers()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        usersList.clear();
       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String uid = firebaseUser.getUid();
               Log.i(TAG,"user_id :"+uid);
               for(DataSnapshot users : snapshot.getChildren())
               {

                   Users user = users.getValue(Users.class);
                   if(!user.getUid().equals(uid))
                   {
                       usersList.add(user);
                   }
                   contactsAdapter.notifyDataSetChanged();

                   Log.i(TAG, "user_id from database :" + user.getUid());

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }




}