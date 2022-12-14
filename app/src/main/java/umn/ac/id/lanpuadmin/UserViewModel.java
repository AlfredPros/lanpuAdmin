package umn.ac.id.lanpuadmin;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class UserViewModel extends ViewModel {
    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");

    public void checkInUser(String userID) {
        Calendar c = Calendar.getInstance();
        @SuppressLint({"SimpleDateFormat", "WeekBasedYear"}) SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        usersTableReference.child(userID).child("checkedIn").setValue(true);
        usersTableReference.child(userID).child("entryTime").setValue(strDate);
    }

    public void checkOutUser(String userID) {
        usersTableReference.child(userID).child("checkedIn").setValue(false);
        usersTableReference.child(userID).child("entryTime").setValue(null);
    }

    public void pay(String userID, int amount) {
        DatabaseReference userReference = usersTableReference.child(userID);
        Log.d("PAY", "balance called");

        userReference.child("balance").get().addOnCompleteListener(task -> {
            Integer balance = task.getResult().getValue(Integer.class);
            if (balance != null) {
                Log.d("PAYBALANCE", balance.toString());
            } else {
                Log.d("PAYBALANCE", "null");
            }

        });

        userReference.child("balance").runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer currBalance;
                if (currentData.getValue() != null) {
                    currBalance = currentData.getValue(Integer.class);
                    currentData.setValue(currBalance - amount);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                Log.d("PAYSTATUS", String.valueOf(committed));
                if (error != null) {
                    Log.d("PAYERROR", error.toString());
                }
            }
        });
    }

    public void topUP(String userID, int amount) {
        DatabaseReference userReference = usersTableReference.child(userID);
        userReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer currentBalance = currentData.child("balance").getValue(Integer.class);
                if (currentBalance != null) {
                    currentData.child("balance").setValue(currentBalance + amount);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                assert currentData != null;
                Log.d("Payment", Objects.requireNonNull(currentData.child("balance").getValue(int.class)).toString());
            }
        });
    }


    public DatabaseReference getUserName(String userID) {
        return usersTableReference.child(userID).child("name");
    }
}
