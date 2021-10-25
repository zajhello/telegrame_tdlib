package com.example.tdlib_test;

import android.util.Log;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

import java.util.ArrayList;

/**
 * Created by selva on 7/7/18.
 */

public class ChatsManager implements KulaClient.Callback {

    private Client client;
    private ArrayList<TdApi.Chat> chatList = new ArrayList<>();
    private static ChatsManager chatsManager;

    public static ChatsManager getInstance(){
        if (chatsManager==null){
            chatsManager = new ChatsManager();
        }
        return chatsManager;
    }

    void getChats(){
        KulaClient.getClient(this).send(new TdApi.GetChats(),new Client.ResultHandler(){

            @Override
            public void onResult(TdApi.Object object) {
                switch (object.getConstructor()){
                    case TdApi.Chats.CONSTRUCTOR:
                        long chatIDs[] = ((TdApi.Chats)object).chatIds;
                        for (long chatID : chatIDs){
                            Log.d("lol", "onResult: "+chatID);
                            client.send(new TdApi.GetChat(chatID),this,null);
                        }
                        //Log.d("lol", "onResult: "+chatIDs.toString());
                        break;
                    case TdApi.Chat.CONSTRUCTOR:
                        TdApi.Chat myChat = ((TdApi.Chat)object);
                        chatList.add(myChat);
                        break;
                }
            }
        });
    }

    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()){

            case TdApi.UpdateUser.CONSTRUCTOR:
                TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
                TdApi.User user = updateUser.user;

        }
    }

}
