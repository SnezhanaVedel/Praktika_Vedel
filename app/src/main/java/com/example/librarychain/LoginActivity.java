package com.example.librarychain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private String login;
    private String pass;
    public static boolean userIsAdmin = false;
    private boolean loginSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText loginET = findViewById(R.id.loginET);
        EditText passET = findViewById(R.id.passET);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {
            login = String.valueOf(loginET.getText());
            pass = String.valueOf(passET.getText());

            if (login.equals("root") && pass.equals("root")) {
                loginSuccessful = true;
                userIsAdmin = true;
                Toast.makeText(this, "Вы вошли как: " + login, Toast.LENGTH_LONG).show();
                logIn();
            } else if (login.equals("user") && pass.equals("user")) {
                userIsAdmin = false;
                loginSuccessful = true;
                Toast.makeText(this, "Вы вошли как: " + login, Toast.LENGTH_LONG).show();
                logIn();
            } else {
                userIsAdmin = false;
                loginSuccessful = false;
                Toast.makeText(this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}