package com.example.whatsappapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whatsappapplication.Activities.MainActivity;
import com.example.whatsappapplication.Models.Chats;
import com.example.whatsappapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends ArrayAdapter<Chats> {

    public ChatsAdapter(@NonNull Context context, @NonNull List<Chats> objects) {
        super(context, R.layout.chats_layout, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats_layout,parent,false);
        }
        TextView username = convertView.findViewById(R.id.textViewChatUsername);
        TextView lastSeen = convertView.findViewById(R.id.textViewChatLastSeen);
        CircleImageView circleImageView = convertView.findViewById(R.id.circleImageView);
        Chats chat = getItem(position);
        username.setText(chat.getReceiverUsername());
        Date date = new Date(chat.getTimestamp());
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        lastSeen.setText(dateFormatted);
        if(MainActivity.photoUri != null)
        Picasso.get().load(MainActivity.photoUri).placeholder(R.drawable.ic_baseline_person_pin_24).into(circleImageView);
        else
            circleImageView.setImageResource(R.drawable.ic_baseline_person_pin_24);
        return convertView;

    }
}
