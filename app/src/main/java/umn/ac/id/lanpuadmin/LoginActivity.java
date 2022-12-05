package umn.ac.id.lanpuadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        inputEmail = findViewById(R.id.emailLogin);
        inputPassword = findViewById(R.id.passwordLogin);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Email and Password must be filled!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intentToHomeApps = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentToHomeApps);
                    finish();
                }
            }
        });
    }
}
