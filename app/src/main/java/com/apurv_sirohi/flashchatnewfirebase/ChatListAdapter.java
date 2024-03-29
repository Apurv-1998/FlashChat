package com.apurv_sirohi.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mSnapshotList;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity mActivity, DatabaseReference mDatabaseReference, String mDisplayName) {
        this.mActivity = mActivity;
        this.mDatabaseReference = mDatabaseReference;
        this.mDisplayName = mDisplayName;
        mDatabaseReference.addChildEventListener(mListener);
        mSnapshotList = new ArrayList<>();
    }

    //Creating a helper class to hold all the views in a single chat row
    static class ViewHolder
    {
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot = mSnapshotList.get(position);

        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflator = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.chat_msg_row,parent,false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName = convertView.findViewById(R.id.author);
            holder.body = convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams)holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final InstantMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isme = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isme,holder);

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String body = message.getMessage();
        holder.body.setText(body);


        return convertView;
    }

    private void setChatRowAppearance(boolean itsme,ViewHolder holder)
    {
        if(itsme)
        {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }
        else
        {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
    }

    public void cleanUp()
    {
        mDatabaseReference.removeEventListener(mListener);
    }
}
