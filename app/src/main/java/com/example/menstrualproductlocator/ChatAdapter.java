package com.example.menstrualproductlocator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;
    private ParseUser mUser;

    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;


    public ChatAdapter(Context context, String userId, List<Message> messages, ParseUser user) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
        mUser = user;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView body;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) {
            Log.i("TAG", "bindMessage: " + message.getUser().getParseFile("profilePic").getUrl());
            Glide.with(mContext)
                    .load(message.getUser().getParseFile("profilePic").getUrl())
                    .circleCrop() // create an effect of a round profile picture
                    .into(imageOther);
            body.setText(message.getBody());
            name.setText(message.getUsername());

        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageMe;
        TextView body;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
        }

        @Override
        public void bindMessage(Message message) {
            Log.i("TAG", "bindMessage: " + message.getUser().getParseFile("profilePic").getUrl());
            Glide.with(mContext)
                    .load(message.getUser().getParseFile("profilePic").getUrl())
                    .circleCrop() // create an effect of a round profile picture
                    .into(imageMe);
            body.setText(message.getBody());
        }

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }


    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.bindMessage(message);
    }


    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
    }


    private boolean isMe(int position) {
        Message message = mMessages.get(position);
        return message.getUserId() != null && message.getUserId().equals(mUserId);
    }

}