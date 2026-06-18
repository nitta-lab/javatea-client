package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.R;
//import com.example.javatea_client.Javatea;
import com.example.javatea_client.viewModels.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        EditText userIdEditText = findViewById(R.id.userid_edit);
        EditText passwordEditText = findViewById(R.id.password_edit);
        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.signup_button);
        ModeBar.setup(this, "ログイン");

        UserViewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        userViewModel.getToken.observe(this, new Observer<String>() {

            @Override
            public void onChanged(String token) {
                if (token == null) {
                    return;
                }

                if (!token.isEmpty()) {
                    //Javatea app = (Javatea) LoginActivity.this.getApplication();
                    app.setToken(token);

                    Intent intent = new Intent(LoginActivity.this, TimetableActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "ログインに失敗しました", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginButton.setOnClickListener(view -> {
            String userId = userIdEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            userViewModel.login(userId, password);
        });

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity1.class);
            startActivity(intent);
        });
    }
}