package com.example.fairshare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Context;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;




import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GroupChat extends AppCompatActivity {

    ListView chatListView;
    TextView tv;
    ArrayList<Message> messages;
    EditText et;
    Button bt;
    FloatingActionButton fb, fb2;
    ChatAdapter chatAdapter;
    MyDatabase db;
    int groupId;
    int senderId;
    String userName;
    String groupName;

    Handler handler = new Handler();
    Runnable refreshMessages = new Runnable() {
        @Override
        public void run() {
            messages.clear();
            messages.addAll(db.getMessagesByGroup(groupId));
            chatAdapter.notifyDataSetChanged();
            handler.postDelayed(this, 3000); // refresh every 3 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_chat);
        bt = findViewById(R.id.bt);
        tv = findViewById(R.id.tv);
        fb = findViewById(R.id.fb);
        fb2 = findViewById(R.id.AddExp);
        et = findViewById(R.id.et);
        chatListView = findViewById(R.id.lv);
        chatListView.setStackFromBottom(true);

        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        userName = i.getStringExtra("userName");
        groupId = i.getIntExtra("groupId",0);
        tv.setText(groupName);


        db = new MyDatabase(this);
        senderId = db.getUserIdByUsername(userName);


        messages = db.getMessagesByGroup(groupId);
        chatAdapter = new ChatAdapter((Context) this, messages,userName);
        chatListView.setAdapter(chatAdapter);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupChat.this,ShowMembers.class);
                i.putExtra("groupId",groupId);
                i.putExtra("groupName",groupName);
                i.putExtra("userName",userName);
                startActivity(i);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mes = et.getText().toString().trim();
                if(!mes.isEmpty()){
                    db.addMessage(groupId,senderId, mes);
                    et.setText("");
                    messages.clear();
                    messages.addAll(db.getMessagesByGroup(groupId));
                    chatAdapter.notifyDataSetChanged();

                    chatListView.setSelection(chatAdapter.getCount() - 1);
                }

            }
        });
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupChat.this, ExpenseSplitActivity.class);
                i.putExtra("group_id", groupId);
                i.putExtra("group_name", groupName);
                i.putExtra("user_name", userName);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(refreshMessages); // start periodic updates
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshMessages); // stop refreshing when activity not visible
    }
}