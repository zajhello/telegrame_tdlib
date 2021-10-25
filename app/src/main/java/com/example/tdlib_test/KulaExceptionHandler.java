package com.example.tdlib_test;

import org.drinkless.td.libcore.telegram.Client;

public class KulaExceptionHandler implements Client.ExceptionHandler {

    @Override
    public void onException(Throwable e) {
        e.printStackTrace();
    }
}
