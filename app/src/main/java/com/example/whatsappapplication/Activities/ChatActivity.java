package com.example.whatsappapplication.Activities;

import android.content.Context;
import android.content.Intent;
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
import com.example.whatsappapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String ReceiverID, ReceiverName, ReceiverImage, SenderID;
    private TextView userName, userLastSeen;
    private CircleImageView userImage;
    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
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

    }

    private void SendMessage() {

        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Please type a message.", Toast.LENGTH_SHORT).show();
        } else {
            String messageSenderRef = "Messages/" + SenderID + "/" + ReceiverID;
            String messageReceiverRef = "Messages/" + ReceiverID + "/" + SenderID;

            DatabaseReference userMessageKeyRef = RootRef.child("Messages")
                    .child(SenderID).child(ReceiverID).push();
            String chat_Id = UUID.randomUUID().toString();
            Chats chat = new Chats(chat_Id,ReceiverName,messageText,System.currentTimeMillis(),false);
            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Chats");
            firebaseDatabase.child(SenderID).child(ReceiverID).setValue(chat);

            String messagePushID = userMessageKeyRef.getKey();

            Map<String,Object> messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", SenderID);
            messageTextBody.put("to",ReceiverID);
            messageTextBody.put("timestamp",System.currentTimeMillis());

            Map<String,Object> messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
