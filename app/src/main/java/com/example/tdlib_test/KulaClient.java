package com.example.tdlib_test;

import android.app.Activity;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

/**
 * Created by Selva on 3/27/18.
 * Made with ❤ in Selvasoft.
 */

public class KulaClient {
    private static Client client;

    private KulaClient(){

    }

    public static Client getClient(Callback activity){
        if (client==null){
            client = Client.create(activity,null,null);
        } else {
//            client.setUpdatesHandler(activity);
        }
        return client;
    }

    public interface Callback extends Client.ResultHandler{
        @Override
        void onResult(TdApi.Object object);
    }
}
