package com.sid.securevault.repository;

import android.content.Context;

import com.sid.securevault.model.AccountModel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface ConnectToDatabase {
    CompletableFuture<Boolean> createAccount(AccountModel accountModel, Context context) throws IllegalAccessException, ExecutionException, InterruptedException;
    CompletableFuture<Boolean> login(AccountModel accountModel, Context context) throws IllegalAccessException, ExecutionException, InterruptedException;
}
