package com.example.javatea_client.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.javatea_client.R;
import com.example.javatea_client.Javatea;
import com.example.javatea_client.viewModels.UserViewModel;

public class SignUpActivity1 extends AppCompatActivity {
    private Javatea javatea;
    UserViewModel userViewModel;
    private String userId, password, nickname;
    private boolean isVisiblePassword = false;

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

        // javateaの取得
        javatea = (Javatea) this.getApplication();
        // viewModelの取得
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // すでにユーザIDが登録済みならログイン画面に移動
        if(!javatea.getUserId().isEmpty() && !javatea.getView().equals("SignUp")) {
            Intent intent = new Intent(SignUpActivity1.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // 入力欄・ボタンの取得
        EditText userIdEditText = findViewById(R.id.editTextUserId);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        EditText nicknameEditText = findViewById(R.id.editTextNickname);
        TextView userIdText = findViewById(R.id.textViewUserId);
        TextView passwordText = findViewById(R.id.textViewPassword);
        TextView nicknameText = findViewById(R.id.textViewNickname);
        ImageButton eyeButton = findViewById(R.id.EyeButton);
        Button nextButton = findViewById(R.id.NextButton);
        Button backButton = findViewById(R.id.BackButton);
        Button registerButton = findViewById(R.id.RegisterButton);

        // 上部の文字
        ModeBar.setup(this, "新規登録");

        // ユーザを監視して登録後、ログインを呼び出す
        userViewModel.getUser().observe(this, user -> {
            if(user != null) {
                // javateaに保存
                javatea.setUserId(userId);
                javatea.setPassword(password);
                String token = user.getToken();
                if(token != null && !token.isEmpty()){
                    // javateaに保存
                    javatea.setToken(token);
                    // 画面遷移
                    Intent intent = new Intent(SignUpActivity1.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity1.this, "トークン取得に失敗しました", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // エラーメッセージを監視してトースト表示
        userViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(SignUpActivity1.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // ユーザID重複フラグを監視して、重複していなければ次へ。そうでなければトースト表示
        userViewModel.isUidDuplication().observe(this, isUidDuplication -> {
            if(!isUidDuplication) {
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
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeButton.setAlpha(1f);
                isVisiblePassword = false;
            }else{
                Toast.makeText(SignUpActivity1.this, "このIDは既に使用されています", Toast.LENGTH_SHORT).show();
            }
        });

        // パスワードの表示非表示ボタンの処理
        eyeButton.setOnClickListener(view -> {
            if (isVisiblePassword) {
                /* パスワード非表示 */
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyeButton.setAlpha(1f);
                isVisiblePassword = false;
            }else{
                /* パスワード表示 */
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eyeButton.setAlpha(0.5f);
                isVisiblePassword = true;
            }
        });

        // 次へボタンの処理
        nextButton.setOnClickListener(view -> {
            userId = userIdEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            nickname = nicknameEditText.getText().toString().trim();

            // 空文字チェック
            if (userId.isEmpty() || password.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(SignUpActivity1.this, "ID・パスワード・ニックネームを入力してください", Toast.LENGTH_SHORT).show();
                return;
            }

            // パスワードチェック
            if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !IsContainsNumber(password)) {
                Toast.makeText(SignUpActivity1.this, "パスワードは大文字、小文字、数字を含め、8文字以上にしてください", Toast.LENGTH_SHORT).show();
                return;
            }

            // userIdの重複チェック・それ以降はobserve
            userViewModel.checkUser(userId);
        });

        // 修正ボタンの処理
        backButton.setOnClickListener(view -> {
            userIdEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);
            nicknameEditText.setVisibility(View.VISIBLE);
            userIdText.setVisibility(View.INVISIBLE);
            passwordText.setVisibility(View.INVISIBLE);
            nicknameText.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.VISIBLE);
        });

        // 登録ボタンの処理
        registerButton.setOnClickListener(view -> userViewModel.createUser(userId, nickname, password));
    }

    // 数字が含まれているかを返す関数
    private boolean IsContainsNumber(String str) {
        if (str == null || str.isEmpty()) return false;

        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // 数字が含まれていればtrue
            }
        }
        return false;
    }
}