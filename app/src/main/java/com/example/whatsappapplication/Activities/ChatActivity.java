package com.example.whatsappapplication.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappapplication.Adapters.MessagesAdapter;
import com.example.whatsappapplication.Models.Chats;
import com.example.whatsappapplication.Models.Messages;
import com.example.whatsappapplication.Models.Users;
import com.example.whatsappapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private final String TAG = ChatActivity.this.getClass().getSimpleName();
    private String ReceiverID, ReceiverName, ReceiverImage, SenderID,SenderUsername;
    private TextView userName, userLastSeen;
    private CircleImageView userImage;
    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private DatabaseReference myRef;
    private ImageButton SendMessageButton;
    private EditText MessageInputText;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private MessagesAdapter mMessageAdapter;
    private RecyclerView userMessagesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        SenderID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        ReceiverID = getIntent().getExtras().get("ReceiverUserId").toString();
        ReceiverName = getIntent().getExtras().get("ReceiverUsername").toString();
        ReceiverImage = getIntent().getExtras().get("ReceiverPhotoUri").toString();

        initializeControllers();

        userName.setText(ReceiverName);
        Picasso.get().load(ReceiverImage).placeholder(R.drawable.ic_baseline_person_pin_24).into(userImage);

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage();

            }
        });


    }



    private void initializeControllers() {
        ChatToolBar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatToolBar);
        setTitle("");
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
       actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater =  (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.app_bar_layout, null);
        actionBar.setCustomView(actionBarView);

        userImage = findViewById(R.id.custom_profile_image);
        userName = findViewById(R.id.custom_profile_name);
        userLastSeen = findViewById(R.id.custom_user_last_seen);

        SendMessageButton = findViewById(R.id.send_message_btn);
        MessageInputText = findViewById(R.id.input_message);

        mMessageAdapter = new MessagesAdapter(messagesList);
        userMessagesList = findViewById(R.id.private_messages_list_of_users);

        mLinearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(mLinearLayoutManager);
        userMessagesList.setAdapter(mMessageAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            myRef = firebaseDatabase.getReference("Messages");
            myRef.child(SenderID).child(ReceiverID).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Messages messages = dataSnapshot.getValue(Messages.class);
                            messagesList.add(messages);

                            mMessageAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }catch ( Exception e)
        {
            e.printStackTrace();
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("Users");
        reference.child(SenderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                SenderUsername = currentUser.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SendMessage() {

        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Please type a message.", Toast.LENGTH_SHORT).show();
        } else {

            String messageSenderRef = "Messages/" + SenderID + "/" + ReceiverID;
            String messageReceiverRef = "Messages/" + ReceiverID + "/" + SenderID;

            DatabaseReference senderMessageKeyRef = RootRef.child("Messages").child(SenderID).child(ReceiverID).push();
            DatabaseReference receiverMessageKeyRef = RootRef.child("Messages").child(ReceiverID).child(SenderID).push();

            String chatSenderRef = "Chats/" + SenderID + "/" + ReceiverID;
            String chatReceiverRef = "Chats/" + ReceiverID + "/" + SenderID;

            String chat_Id = UUID.randomUUID().toString();
            Chats senderChat = new Chats(chat_Id,ReceiverName,messageText,System.currentTimeMillis(),false);
            Chats receiverChat = new Chats(chat_Id,SenderUsername,messageText,System.currentTimeMillis(),false);

            DatabaseReference senderChatKeyRef = RootRef.child("Chats").child(SenderID).child(ReceiverID).push();
            DatabaseReference receiverChatKeyRef = RootRef.child("Chats").child(SenderID).child(ReceiverID).push();

            String senderMessagePushID = senderMessageKeyRef.getKey();
            String receiverMessagePushID = receiverMessageKeyRef.getKey();
            String senderChatPushID = senderChatKeyRef.getKey();
            String receiverChatPushID = receiverChatKeyRef.getKey();

            Messages senderMessage = new Messages(senderMessagePushID,SenderID,messageText,"text",ReceiverID,System.currentTimeMillis());
            Messages receiverMessage = new Messages(receiverMessagePushID,ReceiverID,messageText,"text",SenderID,System.currentTimeMillis());


            Map<String,Object> detailsMap = new HashMap<>();
            detailsMap.put(messageSenderRef + "/" + senderMessagePushID, senderMessage);
            detailsMap.put(messageReceiverRef + "/" + receiverMessagePushID, receiverMessage);
            detailsMap.put(chatSenderRef + "/" + senderChatPushID, senderChat);
            detailsMap.put(chatReceiverRef + "/" + receiverChatPushID, receiverChat);


            RootRef.updateChildren(detailsMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatActivity.this, "Message Sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(ChatActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");

                }
            });
        }

    }




}
