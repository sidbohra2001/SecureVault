package com.sid.securevault.service;

import android.content.Context;

import com.sid.securevault.model.CreateAccountModel;

import java.util.concurrent.CompletableFuture;

public interface AccountServices {
    public CompletableFuture<Boolean> createAccount(CreateAccountModel createAccountModel, Context context);
}
