package umn.ac.id.lanpuadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button gateInButton;
    private Button gateOutButton;
    private Button topUpButton;
    private TextView outputQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputQr = findViewById(R.id.outputQR);

        logoutButton = findViewById(R.id.logOutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSplash = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intentToSplash);
                finish();
            }
        });

        gateInButton = findViewById(R.id.gateInButton);
        gateInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setRequestCode(1);
                intentIntegrator.initiateScan();
            }
        });

        gateOutButton = findViewById(R.id.gateOutButton);
        gateOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setRequestCode(2);
                intentIntegrator.initiateScan();
            }
        });

        topUpButton = findViewById(R.id.topUpButton);
        topUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setRequestCode(3);
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String scannedQR = null;

        try {
            scannedQR = data.getStringExtra("SCAN_RESULT");
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }

        if (scannedQR != null) {
            switch (requestCode) {
                case 1: {  // Gate In
                    String result = "Gate In: " + scannedQR;
                    outputQr.setText(result);
                    break;
                }
                case 2: {  // Gate Out
                    String result = "Gate Out: " + scannedQR;
                    outputQr.setText(result);
                    break;
                }
                case 3: {  // Top Up
                    String result = "Top Up: " + scannedQR;
                    outputQr.setText(result);
                    break;
                }
            }
        }
    }
}