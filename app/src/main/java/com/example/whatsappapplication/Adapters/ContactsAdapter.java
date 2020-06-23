package com.example.whatsappapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.whatsappapplication.Models.Chats;
import com.example.whatsappapplication.Models.Users;
import com.example.whatsappapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends ArrayAdapter<Chats> {

    private List<Users> chatsList;

    public ContactsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public ContactsAdapter(@NonNull Context context, List<Users> chatsList) {
        super(context, R.layout.contacts_view);
        this.chatsList = chatsList;
    }

    @Override
    public int getCount() {
        return chatsList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null)
        {
           convertView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_view,parent,false);
        }

        Users user = chatsList.get(position);
        TextView textView = convertView.findViewById(R.id.textViewName);
        TextView lastSeen = convertView.findViewById(R.id.textViewLastSeen);
        CircleImageView circleImageView = convertView.findViewById(R.id.imageView);
        textView.setText(user.getUsername());
        Date date = new Date(user.getLastSeen());
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateFormatted = formatter.format(date);
        lastSeen.setText("Last Seen at : "+dateFormatted);
        if(user.getPhotoUrl().equals("default"))
        {
            circleImageView.setImageResource(R.drawable.ic_baseline_person_pin_24);

        }
        else {
            Picasso.get().load(user.getPhotoUrl()).into(circleImageView);
        }

        return convertView;

    }
}
