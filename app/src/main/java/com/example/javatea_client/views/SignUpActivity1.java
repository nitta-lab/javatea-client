package com.example.javatea_client.views;

import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.R;
import com.example.javatea_client.viewModels.UserViewModel;

public class SignUpActivity1 extends AppCompatActivity {

    UserViewModel userViewModel;
    private String userId, password, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // viewModelの取得
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // 入力欄の取得
        EditText userIdEditText = findViewById(R.id.editTextUserId);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        EditText nicknameEditText = findViewById(R.id.editTextNickname);
        TextView userIdText = findViewById(R.id.textViewUserId);
        TextView passwordText = findViewById(R.id.textViewPassword);
        TextView nicknameText = findViewById(R.id.textViewNickname);
        Button nextButton = findViewById(R.id.NextButton);
        Button backButton = findViewById(R.id.BackButton);
        Button registerButton = findViewById(R.id.RegisterButton);

        // 次へボタンの処理
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                userId = userIdEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                nickname = nicknameEditText.getText().toString().trim();

                // 空文字チェック
                if (userId.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                    Toast.makeText(SignUpActivity1.this, "ID・パスワード・ニックネームを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }

                userIdText.setText(userId);
                passwordText.setText(password);
                nicknameText.setText(nickname);
                userIdEditText.setVisibility(View.INVISIBLE);
                passwordEditText.setVisibility(View.INVISIBLE);
                nicknameEditText.setVisibility(View.INVISIBLE);
                userIdText.setVisibility(View.VISIBLE);
                passwordText.setVisibility(View.VISIBLE);
                nicknameText.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
            }
        });

        // 修正ボタンの処理
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userIdEditText.setVisibility(View.VISIBLE);
                passwordEditText.setVisibility(View.VISIBLE);
                nicknameEditText.setVisibility(View.VISIBLE);
                userIdText.setVisibility(View.INVISIBLE);
                passwordText.setVisibility(View.INVISIBLE);
                nicknameText.setVisibility(View.INVISIBLE);
                registerButton.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.VISIBLE);
            }
        });

        // 登録ボタンの処理
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}