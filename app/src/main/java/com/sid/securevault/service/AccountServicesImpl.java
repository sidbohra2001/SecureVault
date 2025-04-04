package com.sid.securevault.service;

import android.content.Context;
import android.util.Log;

import com.sid.securevault.components.MessageBox;
import com.sid.securevault.model.AccountModel;
import com.sid.securevault.repository.ConnectToDatabaseImpl;
import com.sid.securevault.utils.Constants;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AccountServicesImpl implements AccountServices {

    @Override
    public CompletableFuture<Boolean> createAccount(AccountModel accountModel, Context context) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        try {
            if(
                !checkIfMandatoryAvailable(accountModel, context, Constants.CREATE_ACCOUNT) ||
                !checkIfValidPassword(accountModel, context, Constants.CREATE_ACCOUNT)
            ) result.complete(false);
            else {
                new ConnectToDatabaseImpl().createAccount(accountModel, context)
                        .whenComplete((response, _) -> {
                            result.complete(response);
                        });
            }
        } catch (IllegalAccessException | InterruptedException | ExecutionException e) {
            Log.e(Constants.MY_ERROR_TAG, "ERROR | While creating account | Error Message: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public CompletableFuture<Boolean> login(AccountModel accountModel, Context context) {

        CompletableFuture<Boolean> result = new CompletableFuture<>();
        try {
            if(
                !checkIfMandatoryAvailable(accountModel, context, Constants.LOGIN) ||
                !checkIfValidPassword(accountModel, context, Constants.LOGIN)
            ) result.complete(false);
            else {
                new ConnectToDatabaseImpl().login(accountModel, context)
                        .whenComplete((response, _) -> {
                            result.complete(response);
                        });
            }
        } catch (IllegalAccessException | InterruptedException | ExecutionException e) {
            Log.e(Constants.MY_ERROR_TAG, "ERROR | While logging in | Error Message: " + e.getMessage(), e);
        }
        return result;
    }

    private static boolean checkIfMandatoryAvailable(AccountModel model, Context context, String requestType) {
        boolean response = false;
        switch(requestType) {
            case Constants.CREATE_ACCOUNT:
                if(
                        model != null &&
                        !model.getFullName().trim().isEmpty() &&
                        !model.getMobileNumber().trim().isEmpty() &&
                        !model.getPassword().trim().isEmpty() &&
                        !model.getConfirmPassword().trim().isEmpty()
                ) {
                    response = true;
                    break;
                }
                MessageBox.showMessageBox(MessageBox.builder()
                        .title("Attention !!!")
                        .salute("Hi " + (model==null || model.getFullName().trim().isEmpty() ? "User" : model.getFullName()) + ",")
                        .description("Mandatory Fields Missing")
                        .message("""
                                Note:
                                The following fields are mandatory to create an account:
                                1. Mobile Number
                                2. Password
                                3. Email Id
                                4. Date of Birth
                                5. Full Name
                                Please fill the above fields to create an account.""")
                        .build(), context);
                break;
            case Constants.LOGIN:
                if(
                        !model.getPassword().trim().isEmpty() &&
                        !model.getMobileNumber().trim().isEmpty()
                ) {
                    response = true;
                    break;
                }
                MessageBox.showMessageBox(MessageBox.builder()
                        .title("Attention !!!")
                        .salute("Hi User")
                        .description("Mandatory Fields Missing")
                        .message("""
                                Note:
                                The following fields are mandatory to login:
                                1. Mobile Number
                                2. Password
                                Please fill the above fields to login.""")
                        .build(), context);
                break;

        }
        return response;
    }

    private static boolean checkIfValidPassword(AccountModel model, Context context, String requestType) {
        if(requestType.equals(Constants.CREATE_ACCOUNT) && !model.getPassword().equals(model.getConfirmPassword())) {
            MessageBox.showMessageBox(MessageBox.builder()
                    .title("Attention !!!")
                    .salute("Hi User,")
                    .description("Invalid Data")
                    .message("Note:\n" +
                            "Passwords entered do not match")
                    .build(), context);
            return false;
        } else if (!model.getPassword().matches(Constants.PASSWORD_REGEX)) {
            MessageBox.showMessageBox(MessageBox.builder()
                    .title("Attention !!!")
                    .salute("Hi " + (requestType.equals(Constants.LOGIN) || model.getFullName().trim().isEmpty()? "User" : model.getFullName()) + ",")
                    .description("Invalid Data")
                    .message("""
                            Note:
                            Password must contain:
                            1. At least 8 characters
                            2. At least one uppercase letter
                            3. At least one lowercase letter
                            4. At least one digit
                            5. At least one special character (@$!%*?&)
                            """)
                    .build(), context);
            return false;
        }
        return true;
    }
}
