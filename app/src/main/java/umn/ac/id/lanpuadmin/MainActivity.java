package umn.ac.id.lanpuadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button gateInButton;
    private Button gateOutButton;
    private Button topUpButton;
    private TextView outputQr;
    private FirebaseAuth mAuth;

    private TicketViewModel ticketViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        View MOdels
        ticketViewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


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

        mAuth = FirebaseAuth.getInstance();
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
                    String finalScannedQR = scannedQR;
                    ticketViewModel.getActiveTicket(scannedQR).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                userViewModel.checkInUser(finalScannedQR);;
                                Task<DataSnapshot> motorcycle = userViewModel.getUserName(finalScannedQR).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        String name = task.getResult().getValue(String.class);
                                        ticketViewModel.createTicket(finalScannedQR, name, "Motorcycle");
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "User telah Checked in.", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    outputQr.setText(result);
                    break;
                }
                case 2: {  // Gate Out
                    String result = "Gate Out: " + scannedQR;
                    String finalScannedQR1 = scannedQR;
                    ticketViewModel.getActiveTicket(scannedQR).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ticketViewModel.checkOutTicket(finalScannedQR1);
                                outputQr.setText(result);
                            }
                            if (!snapshot.exists()) {
                                Toast.makeText(MainActivity.this, "User Tidak Checked In", Toast.LENGTH_LONG);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(MainActivity.this, "User Tidak Checked In", Toast.LENGTH_LONG);
                        }
                    });
                    break;
                }
                case 3: {  // Top Up
                    String result = "Top Up: " + scannedQR;
                    outputQr.setText(result);
                    Intent intentTopUp = new Intent(MainActivity.this, TopUpActivity.class);
                    intentTopUp.putExtra("id", scannedQR);
                    startActivity(intentTopUp);
                    break;
                }
            }
        }
    }
}