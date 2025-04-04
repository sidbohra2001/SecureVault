package com.sid.securevault.pages;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.sid.securevault.R;
import com.sid.securevault.model.AccountModel;
import com.sid.securevault.service.AccountServicesImpl;
import com.sid.securevault.utils.DeviceFeedback;

public class LoginPage extends AppCompatActivity {

    private CreateAccountPage createAccountPage;
    private EditText mobileNumber, password;
    private TextView createButton;
    private Button loginButton;
    private FirebaseDatabase database;
    private MediaPlayer media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the views
        mobileNumber = findViewById(R.id.id_mobile_number);
        password = findViewById(R.id.id_password);
        loginButton = findViewById(R.id.id_login);
        createButton = findViewById(R.id.id_create);

        // Set an OnClickListener for the createButton
        createButton.setOnClickListener(_ -> {
            DeviceFeedback.clickSound(LoginPage.this);
            DeviceFeedback.hapticFeedBack(LoginPage.this);
            new CreateAccountPage().createAccount(LoginPage.this);
        });

        // Set an OnClickListener for the loginButton
        loginButton.setOnClickListener(_ -> {
            DeviceFeedback.clickSound(LoginPage.this);
            DeviceFeedback.hapticFeedBack(LoginPage.this);
            AccountModel accountModel = AccountModel.builder()
                    .mobileNumber(mobileNumber.getText().toString())
                    .password(password.getText().toString())
                    .build();
            new AccountServicesImpl().login(accountModel, LoginPage.this).whenComplete((result, throwable) -> {
                Toast.makeText(LoginPage.this, "LOGIN "+ (result ? "SUCCESS" : "FAILURE") + " | Mobile: "+accountModel.getMobileNumber(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DeviceFeedback.releaseSound();
    }
}