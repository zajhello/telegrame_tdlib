package com.example.tdlib_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements KulaClient.Callback {

    Client client;
    ArrayList<TdApi.Message> messages = new ArrayList<>();
    RecyclerView recyclerViewChat;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        long id = getIntent().getLongExtra("KulaID",192);
        client = KulaClient.getClient(this);
        client.send(new TdApi.GetChatHistory(id,0,0,20,false),this,null);
        recyclerViewChat = findViewById(R.id.recyclerview_chat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messages);
        recyclerViewChat.setAdapter(chatAdapter);
    }

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()){
            case TdApi.Messages.CONSTRUCTOR:
                TdApi.Messages m = (TdApi.Messages) object;
                Collections.addAll(messages,m.messages);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.refresh();
                    }
                });
        }
    }
}
class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{

    private ArrayList<TdApi.Message> messageList;

    ChatAdapter(ArrayList<TdApi.Message> messageList){
        this.messageList = messageList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_chat,parent,false);
        return new ChatViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
        if (messageList.get(position).content instanceof TdApi.MessageText){
            try{
                holder.message.setText(((TdApi.MessageText) messageList.get(position).content).text.text);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

}
class ChatViewHolder extends RecyclerView.ViewHolder{

    LinearLayout layout;
    TextView message;

    public ChatViewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout) itemView;
        message = layout.findViewById(R.id.message);
    }
}
