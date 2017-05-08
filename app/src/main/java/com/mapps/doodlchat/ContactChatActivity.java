package com.mapps.doodlchat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;

public class ContactChatActivity extends AppCompatActivity implements View.OnClickListener, MessageDataSource.MessagesCallbacks{

    private String mConvoId, dRecipient;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ListView mListView;
    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    MessageDataSource.MessagesListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_chat);

        Firebase.setAndroidContext(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        TextView Name = (TextView) findViewById(R.id.Name);
        TextView Status = (TextView) findViewById(R.id.Status);
        Name.setText(name);
        Status.setText(status);

        Button send = (Button)findViewById(R.id.sendText);
        send.setOnClickListener(this);
        fab.setOnClickListener(this);

        dRecipient = firebaseAuth.getCurrentUser().getDisplayName();

        mListView = (ListView)findViewById(R.id.message_list);
        mMessages = new ArrayList<>();
        mAdapter = new ContactChatActivity.MessagesAdapter(mMessages);
        mListView.setAdapter(mAdapter);

        String[] ids = {dRecipient,"TestUser","-"};
        mConvoId = ids[0]+ids[1]+ids[2];

        mListener = MessageDataSource.addMessagesListener(mConvoId, this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.sendText) {
            EditText newMessageView = (EditText) findViewById(R.id.new_message);
            String newMessage = newMessageView.getText().toString();

            if (!newMessage.equals("")) {
                newMessageView.setText("");
                Message msg = new Message();
                msg.setDate(new Date());
                msg.setText(newMessage);
                msg.setSender(dRecipient);

                MessageDataSource.saveMessage(msg, mConvoId);
            }
        }
        else if(v.getId() == R.id.fab){
            startActivity(new Intent(ContactChatActivity.this, HomeActivity.class));
        }
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages){
            super(ContactChatActivity.this, R.layout.message_item, R.id.message, messages);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView)convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)nameView.getLayoutParams();

            if (message.getSender().equals(dRecipient)){

                nameView.setBackground(getDrawable(R.drawable.chatright));
                nameView.setGravity(Gravity.CENTER);
                layoutParams.gravity = Gravity.RIGHT;
            }
            else{
                nameView.setBackground(getDrawable(R.drawable.chatleft));
                layoutParams.gravity = Gravity.LEFT;
            }

            nameView.setLayoutParams(layoutParams);


            return convertView;
        }
    }
}
