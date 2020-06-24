package com.example.whatsappapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.whatsappapplication.Activities.ChatActivity;
import com.example.whatsappapplication.Activities.MainActivity;
import com.example.whatsappapplication.Adapters.ChatsAdapter;
import com.example.whatsappapplication.Models.Chats;
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


public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";
    private ChatsAdapter chatsAdapter;
    private List<Chats> chatsList;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ListView listView = view.findViewById(R.id.chatListView);
        chatsList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(getContext(),chatsList);
        listView.setAdapter(chatsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("ReceiverUsername",chatsList.get(position).getReceiverUsername());
                intent.putExtra("ReceiverPhotoUri", MainActivity.photoUri);
                intent.putExtra("ReceiverUserId",MainActivity.rUid);
                startActivity(intent);
            }
        });
        getUserChats();
        return view;
    }

    private void getUserChats()
    {
        try {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Chats");
            myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatsList.clear();
                    for (DataSnapshot userChats : snapshot.getChildren()) {
                        chatsList.add(userChats.getValue(Chats.class));
                    }
                    chatsAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}