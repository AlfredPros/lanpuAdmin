package umn.ac.id.lanpuadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText inputEmail;
    private EditText inputPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.emailLogin);
        inputPassword = findViewById(R.id.passwordLogin);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            if (email.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "Email and Password must be filled!", Toast.LENGTH_SHORT).show();
            } else {

                // Authentication
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // Jika user berhasil di authentikasi

                        // Apakah user telah di verified?
                        FirebaseUser user = mAuth.getCurrentUser();

                        if (Objects.requireNonNull(user).isEmailVerified()) {
                            // Redirect ke Home Page
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Log.e("LOGIN", "User Found");
                            FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).get().addOnCompleteListener(task1 -> {
                                User loggedUser = task1.getResult().getValue(User.class);
                                assert loggedUser != null;
                                if (loggedUser.role != null && loggedUser.role.equals("admin")) {
                                    // Intent to MainActivity
                                    Intent intentToHomeApps = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intentToHomeApps);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "User forbidden to log in!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "User has not been verified. Please Check email for verification.", Toast.LENGTH_LONG).show();
                        }

                    } else { // User gagal masuk
                        Toast.makeText(LoginActivity.this, "Failed to Login! Please check your credentials.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
