package com.example.tdlib_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

public class AuthActivity extends AppCompatActivity implements KulaClient.Callback {

    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        client = KulaClient.getClient(this);
//        client.setUpdatesHandler(this);//Client.create(AuthActivity.this,null,null);
        final EditText authCode = (EditText) findViewById(R.id.authCode);
        Button checkBtn = (Button) findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = authCode.getText().toString();
                if (!code.equals("")) {
                    TdApi.CheckAuthenticationCode authCode = new TdApi.CheckAuthenticationCode(code);
                    //client = Client.create(AuthActivity.this,null,null);
                    client.send(authCode,AuthActivity.this);
                }
            }
        });
    }

    @Override
    public void onResult(TdApi.Object object) {
        switch (object.getConstructor()){
            case TdApi.UpdateAuthorizationState.CONSTRUCTOR:
                switch (((TdApi.UpdateAuthorizationState)object).authorizationState.getConstructor()){
                    case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                        Log.d("AuthActivity", "onResult: TDlibParams");
                        TdApi.TdlibParameters authStateRequest = new TdApi.TdlibParameters();
                        authStateRequest.apiId = 193316;
                        authStateRequest.apiHash = "69f1baef48f39fc3a966a4d648b4c909";
                        authStateRequest.useMessageDatabase = true;
                        authStateRequest.useSecretChats = true;
                        authStateRequest.systemLanguageCode = "en";
                        authStateRequest.databaseDirectory = getApplicationContext().getFilesDir().getAbsolutePath();
                        authStateRequest.deviceModel = "Moto";
                        authStateRequest.systemVersion = "7.0";
                        authStateRequest.applicationVersion = "0.1";
                        authStateRequest.enableStorageOptimizer = true;
                        client.send(new TdApi.SetTdlibParameters(authStateRequest), this);
                        break;
                    case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                        client.send(new TdApi.CheckDatabaseEncryptionKey(),this);
                        break;
                    case TdApi.AuthorizationStateReady.CONSTRUCTOR:
                        Intent conversationIntent = new Intent(AuthActivity.this,ConversationActivity.class);
                        conversationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(conversationIntent);
                        //client.send(new TdApi.UpdateAuthorizationState(new TdApi.AuthorizationStateReady()),null,null);
                        //finish();
                }
            case TdApi.UpdateConnectionState.CONSTRUCTOR:
                switch (((TdApi.UpdateConnectionState)object).state.getConstructor()) {
                    case TdApi.ConnectionStateReady.CONSTRUCTOR:
                        Log.d("AuthActivity", "onResult: ConnectionStateReady");
                        //Toast.makeText(AuthActivity.this, "Successfully loginned!", Toast.LENGTH_SHORT).show();
                        break;
                }
        }
    }
}
