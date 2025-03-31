package com.sid.securevault.repository;

import static com.sid.securevault.utils.Constants.LOGIN_SAFE_STORE;
import static com.sid.securevault.utils.Constants.MY_ERROR_TAG;
import static com.sid.securevault.utils.Constants.MY_INFO_TAG;
import static com.sid.securevault.utils.Constants.USER_INFO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sid.securevault.model.CreateAccountModel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ConnectToDatabase {

    private static FirebaseDatabase database;

    // Creates a new user in table USER_INFO and LOGIN_SAFE_STORE
    public static CompletableFuture<Boolean> createAccount(CreateAccountModel createAccountModel, Context context) throws IllegalAccessException, ExecutionException, InterruptedException {
        String mobileNumber = createAccountModel.getMobileNumber();
        if(mobileNumber == null || mobileNumber.isEmpty()) throw new IllegalArgumentException("Mobile Number cannot be empty");
        database = FirebaseDatabase.getInstance();
        DatabaseReference loginSafeStoreRef = database.getReference(LOGIN_SAFE_STORE);
        DatabaseReference userInfoRef = database.getReference(USER_INFO);
        Map<String, Object> map = new HashMap<>();
        for(Field field : createAccountModel.getClass().getDeclaredFields()) {
            if(field.getName().equals("confirmPassword")) continue;
            field.setAccessible(true);
            map.put(field.getName(), field.get(createAccountModel));
        }
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        checkIfUserExists(mobileNumber, loginSafeStoreRef).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult()) {
                Log.i(MY_INFO_TAG, "isSuccessful: " + task.isSuccessful());
                Toast.makeText(context, "Account already exists | Mobile: " + createAccountModel.getMobileNumber(), Toast.LENGTH_SHORT).show();
                result.complete(false);
            } else {
                userInfoRef.child(mobileNumber).setValue(map)
                    .addOnSuccessListener(_ -> {
                        Log.i(MY_INFO_TAG, "USER_INFO | Account Creation: SUCCESS | Mobile: " + mobileNumber);
                        loginSafeStoreRef.child(mobileNumber).setValue(createAccountModel.getPassword())
                            .addOnSuccessListener(_ -> {
                                Log.i(MY_INFO_TAG, "LOGIN_SAFE_STORE | Account Creation: SUCCESS | Mobile: " + mobileNumber);
                                Toast.makeText(context, "Account creation | SUCCESS | Mobile: " + createAccountModel.getMobileNumber(), Toast.LENGTH_SHORT).show();
                                result.complete(true);
                            }).addOnFailureListener(e -> {
                                Log.e(MY_ERROR_TAG, "LOGIN_SAFE_STORE | Account Creation: FAILURE | Mobile: " + mobileNumber, e);
                                userInfoRef.child(mobileNumber).removeValue().addOnSuccessListener(_ -> {
                                    Log.i(MY_INFO_TAG, "USER_INFO | Account Rollback: SUCCESS | Mobile: " + mobileNumber);
                                    Toast.makeText(context, "Account creation | SUCCESS | Mobile: " + createAccountModel.getMobileNumber(), Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e1 -> {
                                    Log.e(MY_ERROR_TAG, "USER_INFO | Account Rollback: FAILURE | Mobile: " + mobileNumber, e1);
                                });
                                result.complete(false);
                            });
                    })
                    .addOnFailureListener(e -> {
                        Log.e(MY_ERROR_TAG, "USER_INFO | Account Creation: FAILURE | Mobile: " + mobileNumber, e);
                        Toast.makeText(context, "Account creation | FAILURE | Mobile: " + createAccountModel.getMobileNumber(), Toast.LENGTH_SHORT).show();
                        result.complete(false);
                    });
            }
        });
        return result;
    }

    // Checks if the user's mobile number exists in the database or not.
    private static Task<Boolean> checkIfUserExists(String mobileNumber, DatabaseReference reference) throws ExecutionException, InterruptedException {
        TaskCompletionSource<Boolean> source = new TaskCompletionSource<>();
        reference.child(mobileNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(MY_INFO_TAG, "checkIfUserExists: " + snapshot.getValue());
                source.setResult(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                source.setException(error.toException());
            }
        });
        return source.getTask();
    }

    public static boolean login(String mobileNumber, String password) {
        database = FirebaseDatabase.getInstance();
        return true;
    }
}
