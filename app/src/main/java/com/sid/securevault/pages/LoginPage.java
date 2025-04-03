package com.sid.securevault.pages;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.sid.securevault.R;
import com.sid.securevault.repository.ConnectToDatabase;

public class LoginPage extends AppCompatActivity {

    private CreateAccountPage createAccountPage;
    private EditText name, password;
    private TextView createButton;
    private Button loginButton;
    private FirebaseDatabase database;

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
        name = findViewById(R.id.id_name);
        password = findViewById(R.id.id_password);
        loginButton = findViewById(R.id.id_login);
        createButton = findViewById(R.id.id_create);

        // Set an OnClickListener for the createButton
        createButton.setOnClickListener(_ -> {
            new CreateAccountPage().createAccount(LoginPage.this);
        });

        // Set an OnClickListener for the loginButton
        loginButton.setOnClickListener(_ -> {
            boolean result = ConnectToDatabase.login(name.getText().toString(), password.getText().toString());
            Log.i("MY_TAG", "onCreate: result: "+result);
        });
    }
}