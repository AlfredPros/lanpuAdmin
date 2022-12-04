package umn.ac.id.lanpuadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TopUpActivity extends AppCompatActivity {

    TextView outputQr;
    EditText editAmount;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        outputQr = findViewById(R.id.outputQR);
        outputQr.setText(id);

        editAmount = findViewById(R.id.editAmount);
        submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editAmount.getText().toString();
                outputQr.setText(input);

                finish();
            }
        });
    }
}