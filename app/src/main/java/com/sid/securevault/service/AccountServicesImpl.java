package com.sid.securevault.service;

import android.content.Context;
import android.util.Log;

import com.sid.securevault.components.MessageBox;
import com.sid.securevault.model.CreateAccountModel;
import com.sid.securevault.repository.ConnectToDatabase;
import com.sid.securevault.utils.Constants;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AccountServicesImpl implements AccountServices {

    @Override
    public CompletableFuture<Boolean> createAccount(CreateAccountModel createAccountModel, Context context) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        try {
            if(
                !checkIfMandatoryAvailable(createAccountModel, context) ||
                !checkIfValidPassword(createAccountModel, context)
            ) result.complete(false);
            else {
                ConnectToDatabase.createAccount(createAccountModel, context)
                        .whenComplete((response, _) -> {
                            result.complete(response);
                        });
            }
        } catch (IllegalAccessException | InterruptedException | ExecutionException e) {
            Log.e("ERROR_LOG", "ERROR | While creating account | Error Message: " + e.getMessage(), e);
        }
        return result;
    }

    private static boolean checkIfMandatoryAvailable(CreateAccountModel model, Context context) {
        if(model != null &&
                !model.getFullName().trim().isEmpty() &&
                !model.getMobileNumber().trim().isEmpty() &&
                !model.getPassword().trim().isEmpty() &&
                !model.getConfirmPassword().trim().isEmpty()
        ) return true;
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
        return false;
    }

    private static boolean checkIfValidPassword(CreateAccountModel model, Context context) {
        if(!model.getPassword().equals(model.getConfirmPassword())) {
            MessageBox.showMessageBox(MessageBox.builder()
                    .title("Attention !!!")
                    .salute("Hi " + (model.getFullName().trim().isEmpty()? "User" : model.getFullName()) + ",")
                    .description("Invalid Data")
                    .message("Note:\n" +
                            "Passwords entered do not match")
                    .build(), context);
            return false;
        } else if (!model.getPassword().matches(Constants.PASSWORD_REGEX)) {
            MessageBox.showMessageBox(MessageBox.builder()
                    .title("Attention !!!")
                    .salute("Hi " + (model.getFullName().trim().isEmpty()? "User" : model.getFullName()) + ",")
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
