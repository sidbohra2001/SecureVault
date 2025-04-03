package com.sid.securevault.service;

import android.content.Context;

import com.sid.securevault.model.AccountModel;

import java.util.concurrent.CompletableFuture;

public interface AccountServices {
    CompletableFuture<Boolean> createAccount(AccountModel accountModel, Context context);
    CompletableFuture<Boolean> login(AccountModel accountModel, Context context);
}
