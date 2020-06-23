package com.example.whatsappapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappapplication.Models.Messages;
import com.example.whatsappapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<Messages> messagesList;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    public MessagesAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageSender,messageReceiver;
        public CircleImageView profileImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageReceiver = itemView.findViewById(R.id.receiver_message);
            messageSender = itemView.findViewById(R.id.sender_message);
            profileImg = itemView.findViewById(R.id.message_profile_image);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Messages messages = messagesList.get(position);
        String sender_Id = mAuth.getUid();
        String from_User_Id = messages.getFrom();

        myRef = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Users").child(from_User_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("photoUrl").equals("default"))
                {
                    holder.profileImg.setImageResource(R.drawable.ic_baseline_person_pin_24);
                }
                else
                {
                    String uri = snapshot.child("photoUrl").getValue().toString();
                    Picasso.get().load(uri).placeholder(R.drawable.ic_baseline_person_pin_24).into(holder.profileImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (from_User_Id.equals(sender_Id)) {

            holder.messageSender.setText(messages.getMessage());
        } else {
            holder.messageSender.setVisibility(View.INVISIBLE);

            holder.messageReceiver.setVisibility(View.VISIBLE);
            holder.profileImg.setVisibility(View.VISIBLE);
            holder.messageReceiver.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }
}
