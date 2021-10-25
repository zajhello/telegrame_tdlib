package com.example.tdlib_test;

import android.content.Intent;
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

public class ConversationActivity extends AppCompatActivity implements KulaClient.Callback{

    public Client client;
    RecyclerView recyclerView_conversation;
    public ArrayList<TdApi.Chat> chatList = new ArrayList<>();
    ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        recyclerView_conversation = (RecyclerView) findViewById(R.id.recyclerview_conversation);
        recyclerView_conversation.setLayoutManager(new LinearLayoutManager(this));
        conversationAdapter = new ConversationAdapter(chatList);
        recyclerView_conversation.setAdapter(conversationAdapter);
        client = KulaClient.getClient(this);
        client.send(new TdApi.GetChats(),this,null);
        //TdApi.Object object = Client.execute(new TdApi.GetChats(Long.MAX_VALUE,0,10));
        recyclerView_conversation.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView_conversation, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent chatIntent = new Intent(ConversationActivity.this,ChatActivity.class);
                Log.d("lol", ""+chatList.get(position).id);
                chatIntent.putExtra("KulaID",chatList.get(position).id);
                startActivity(chatIntent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()){
            case TdApi.Chats.CONSTRUCTOR:
                long chatIDs[] = ((TdApi.Chats)object).chatIds;
                for (long chatID : chatIDs){
                    client.send(new TdApi.GetChat(chatID),ConversationActivity.this,new ExceptionHandler());
                }
                //Log.d("lol", "onResult: "+chatIDs.toString());
                break;
            case TdApi.Chat.CONSTRUCTOR:
                TdApi.Chat myChat = ((TdApi.Chat)object);
                chatList.add(myChat);
                client.send(new TdApi.DownloadFile(myChat.photo.small.id, 1,0,1,true),ConversationActivity.this,null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        conversationAdapter.refresh();
                    }
                });
                break;
            case TdApi.UpdateUser.CONSTRUCTOR:
                TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
                TdApi.User user = updateUser.user;

        }
    }

    public class ExceptionHandler implements Client.ExceptionHandler{

        @Override
        public void onException(Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.close();
    }
}

class ConversationAdapter extends RecyclerView.Adapter<ConversationViewHolder>{

    ArrayList<TdApi.Chat> chatList;

    ConversationAdapter(ArrayList<TdApi.Chat> chatList){
        this.chatList = chatList;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_conversation,parent,false);
        return new ConversationViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(final ConversationViewHolder holder, final int position) {
        holder.name.setText(chatList.get(position).title);
        if (chatList.get(position).photo != null) {
            File imgFile = new File(chatList.get(position).photo.small.local.path);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.profile.setImageBitmap(myBitmap);

            }

        }else {
            holder.profile.setImageResource(R.drawable.ic_launcher_background);
        }
        /*holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

}

class ConversationViewHolder extends RecyclerView.ViewHolder{

    LinearLayout layout;
    TextView name;
    ImageView profile;
    public ConversationViewHolder(View itemView) {
        super(itemView);
        layout = (LinearLayout) itemView;
        name = (TextView) itemView.findViewById(R.id.textView_name);
        profile= (ImageView) itemView.findViewById(R.id.dp);
    }
}

