package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.R;
import com.example.javatea_client.Javatea;
import com.example.javatea_client.viewModels.UserViewModel;
import com.google.android.material.textfield.*;

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

        TextInputEditText userIdEditText = findViewById(R.id.userid_edit);
        TextInputEditText passwordEditText = findViewById(R.id.password_edit);
        TextInputLayout userIdLayout = findViewById((R.id.userid_layout));
        TextInputLayout passwordLayout = findViewById((R.id.password_layout));
        Button loginButton = findViewById(R.id.login_button);
        Button signupButton = findViewById(R.id.signup_button);
        TextView errorText = findViewById(R.id.error_textView);
        ModeBar.setup(this, "ログイン");

        userViewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                switch(errorMsg) {
                    case"ログイン失敗: 400":
                    case"ログイン失敗: 401":
                    case"ログイン失敗: 404":
                        errorText.setText("※IDまたはパスワードが正しくありません");
                        break;

                    case "ログイン失敗: 500":
                        errorText.setText("※サーバーエラーが発生しました");
                        break;

                    default:
                        errorText.setText("※ログインに失敗しました");
                }

                errorText.setVisibility(View.VISIBLE);
                userIdLayout.setBackgroundResource(R.drawable.input_textbox_error);
                passwordLayout.setBackgroundResource(R.drawable.input_textbox_error);
            }
        });

        userViewModel.getToken().observe(this, new Observer<String>() {

            @Override
            public void onChanged(String token) {
                if (token == null) {
                    return;
                }

                if (!token.isEmpty()) {
                    Javatea app = (Javatea) LoginActivity.this.getApplication();
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
            String userId = userIdEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            errorText.setError(null);
            errorText.setVisibility(View.GONE);
            userIdLayout.setBackgroundResource(R.drawable.input_textbox);
            passwordLayout.setBackgroundResource(R.drawable.input_textbox);

            if (userId.isEmpty() && password.isEmpty()) {
                errorText.setText("※ユーザーIDとパスワードを入力してください");
                errorText.setVisibility(View.VISIBLE);
                userIdLayout.setBackgroundResource(R.drawable.input_textbox_error);
                passwordLayout.setBackgroundResource(R.drawable.input_textbox_error);
                return;
            }
            else if (userId.isEmpty()) {
                errorText.setText("※ユーザーIDを入力してください");
                errorText.setVisibility(View.VISIBLE);
                userIdLayout.setBackgroundResource(R.drawable.input_textbox_error);
            }
            else if (password.isEmpty()) {
                errorText.setText("※パスワードを入力してください");
                errorText.setVisibility(View.VISIBLE);
                passwordLayout.setBackgroundResource(R.drawable.input_textbox_error);
            }
            else {
                userViewModel.login(userId, password);
            }
        });

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity1.class);
            startActivity(intent);
        });
    }
}