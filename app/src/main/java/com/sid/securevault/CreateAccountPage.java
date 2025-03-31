package com.sid.securevault;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.sid.securevault.component.MessageBox;
import com.sid.securevault.model.CreateAccountModel;
import com.sid.securevault.repository.ConnectToDatabase;
import com.sid.securevault.utils.Constants;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class CreateAccountPage {

    public static void createAccount(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View createAccountView = inflater.inflate(R.layout.activity_create_account_page, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MessageBox_Popup_Style);
        builder.setView(createAccountView);
        AlertDialog dialog = builder.create();

        Button createButton = createAccountView.findViewById(R.id.id_ca_createButton);
        EditText fullName = createAccountView.findViewById(R.id.id_ca_fullName);
        EditText mobileNumber = createAccountView.findViewById(R.id.id_ca_mobileNumber);
        EditText email = createAccountView.findViewById(R.id.id_ca_email);
        EditText password = createAccountView.findViewById(R.id.id_ca_password);
        EditText confirmPassword = createAccountView.findViewById(R.id.id_ca_confirmPassword);

        createButton.setOnClickListener(_ -> {
            CreateAccountModel createAccountModel = CreateAccountModel.builder()
                    .fullName(fullName.getText().toString())
                    .mobileNumber(mobileNumber.getText().toString())
                    .emailId(email.getText().toString())
                    .dateOfBirth(LocalDate.now())
                    .password(password.getText().toString())
                    .confirmPassword(confirmPassword.getText().toString())
                    .build();
            try {
                if(
                    !checkIfMandatoryAvailable(createAccountModel, context) ||
                    !checkIfValidPassword(createAccountModel, context)
                ) return;
                ConnectToDatabase.createAccount(createAccountModel, context)
                        .whenComplete((response, _) -> {
                            if(response)
                                dialog.dismiss();
                        });
            } catch (IllegalAccessException | InterruptedException | ExecutionException e) {
                Log.e("ERROR_LOG", "ERROR | While creating account | Error Message: " + e.getMessage(), e);
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.MessageBox_Popup_Style;
        dialog.show();

        DisplayMetrics display = context.getResources().getDisplayMetrics();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = display.widthPixels-50;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);
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
                    .message("Note:\n" +
                            "Password must contain:\n" +
                            "1. At least 8 characters\n" +
                            "2. At least one uppercase letter\n" +
                            "3. At least one lowercase letter\n" +
                            "4. At least one digit\n" +
                            "5. At least one special character (@$!%*?&)")
                    .build(), context);
            return false;
        }
        return true;
    }
}