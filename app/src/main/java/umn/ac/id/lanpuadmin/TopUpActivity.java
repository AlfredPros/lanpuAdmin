package umn.ac.id.lanpuadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class TopUpActivity extends AppCompatActivity {

    TextView outputQr;
    EditText editAmount;
    Button submit;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        outputQr = findViewById(R.id.outputQR);
        outputQr.setText("Top Up: " + id);

        editAmount = findViewById(R.id.editAmount);
        submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(view -> {
            String input = editAmount.getText().toString();
            if (input.equals("")) {
                input = "10000";
            }
            outputQr.setText(input);
            userViewModel.topUP(id, Integer.parseInt(editAmount.getText().toString()));
            finish();
        });
    }
}