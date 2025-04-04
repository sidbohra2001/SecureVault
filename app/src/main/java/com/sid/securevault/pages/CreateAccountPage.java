package com.sid.securevault.pages;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.sid.securevault.R;
import com.sid.securevault.model.AccountModel;
import com.sid.securevault.service.AccountServicesImpl;
import com.sid.securevault.utils.DeviceFeedback;

import java.time.LocalDate;

public class CreateAccountPage {

    public void createAccount(Context context) {
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
            DeviceFeedback.clickSound(context);
            DeviceFeedback.hapticFeedBack(context);
            AccountModel accountModel = AccountModel.builder()
                    .fullName(fullName.getText().toString())
                    .mobileNumber(mobileNumber.getText().toString())
                    .emailId(email.getText().toString())
                    .dateOfBirth(LocalDate.now())
                    .password(password.getText().toString())
                    .confirmPassword(confirmPassword.getText().toString())
                    .build();

            new AccountServicesImpl().createAccount(accountModel, context)
                .whenComplete((response, _) -> {
                        if(response)
                            dialog.dismiss();
                    });
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
}