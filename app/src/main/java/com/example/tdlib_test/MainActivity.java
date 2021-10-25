package com.example.tdlib_test;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import org.drinkless.td.libcore.telegram.TdApi.*;


public class MainActivity extends AppCompatActivity implements KulaClient.Callback {

    private final static String TAG = "MainActivity";
    public Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetAuthorizationState AuthState = new GetAuthorizationState();
        client = KulaClient.getClient(this);
        //client.setUpdatesHandler(new UpdatesHandler());
        client.send(AuthState,this);
    }


        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case UpdateAuthorizationState.CONSTRUCTOR:
                    Log.d(TAG, "onResult: UpdateAuthState");
                    onAuthStateUpdated(((UpdateAuthorizationState) object).authorizationState);
                    break;
                case AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                    Log.d(TAG, "onResult: TDlibParams");
                    TdlibParameters authStateRequest = new TdlibParameters();
                    authStateRequest.apiId = 193316;
                    authStateRequest.apiHash = "69f1baef48f39fc3a966a4d648b4c909";
                    authStateRequest.useMessageDatabase = true;
                    authStateRequest.useSecretChats = true;
                    authStateRequest.systemLanguageCode = "en";
                    authStateRequest.databaseDirectory = getApplicationContext().getFilesDir().getAbsolutePath();
                    //authStateRequest.databaseDirectory = "/storage/emulated/0/kula/";
                    authStateRequest.deviceModel = "Moto";
                    authStateRequest.systemVersion = "7.0";
                    authStateRequest.applicationVersion = "0.1";
                    authStateRequest.enableStorageOptimizer = true;
                    client.send(new SetTdlibParameters(authStateRequest), this);
                    break;
                case AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                    client.send(new CheckDatabaseEncryptionKey(), this);
                    GetAuthorizationState AuthState = new GetAuthorizationState();
                    client.send(AuthState,this);
                    break;
                case AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:
                    client.send(new SetAuthenticationPhoneNumber("+917418189531", null), this);
                    break;
                case AuthorizationStateWaitCode.CONSTRUCTOR:
                    Intent AuthCodeIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(AuthCodeIntent);
                    finish();
                    break;
                case AuthorizationStateReady.CONSTRUCTOR:
                    Intent conversationIntent = new Intent(MainActivity.this, ConversationActivity.class);
                    //conversationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Log.d("authKula", "onResult: Registered...");
                    startActivity(conversationIntent);
                    finish();
            }
        }

        private void onAuthStateUpdated(AuthorizationState authorizationState) {
            switch (authorizationState.getConstructor()) {
                case AuthorizationStateWaitTdlibParameters.CONSTRUCTOR:
                    Log.d(TAG, "onResult: TDlibParams");
                    TdlibParameters authStateRequest = new TdlibParameters();
                    authStateRequest.apiId = 193316;
                    authStateRequest.apiHash = "69f1baef48f39fc3a966a4d648b4c909";
                    authStateRequest.useMessageDatabase = true;
                    authStateRequest.useSecretChats = true;
                    authStateRequest.systemLanguageCode = "en";
                    authStateRequest.databaseDirectory = getApplicationContext().getFilesDir().getAbsolutePath();
                    //authStateRequest.databaseDirectory = "/storage/emulated/0/kula/";
                    authStateRequest.deviceModel = "Moto";
                    authStateRequest.systemVersion = "7.0";
                    authStateRequest.applicationVersion = "0.1";
                    authStateRequest.enableStorageOptimizer = true;
                    client.send(new SetTdlibParameters(authStateRequest), this);
                    break;
                case AuthorizationStateWaitEncryptionKey.CONSTRUCTOR:
                    client.send(new CheckDatabaseEncryptionKey(), this);
                    break;
                case AuthorizationStateWaitPhoneNumber.CONSTRUCTOR:
                    client.send(new SetAuthenticationPhoneNumber("+917418189531", null),this);
                    break;
                case AuthorizationStateWaitCode.CONSTRUCTOR:
                    Intent AuthCodeIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(AuthCodeIntent);
                    finish();
                    break;
                case AuthorizationStateReady.CONSTRUCTOR:
                    Intent conversationIntent = new Intent(MainActivity.this, ConversationActivity.class);
                    //conversationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Log.d("authKula", "onResult: Not Registered...");
                    startActivity(conversationIntent);
                    finish();
            }

    }
}

