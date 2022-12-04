package umn.ac.id.lanpuadmin;

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

public class UserViewModel extends ViewModel {
    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");

    public void checkInUser(String userID) {
        usersTableReference.child(userID).child("checkedIn").setValue(true);
    }

    public void checkOutUser(String userID){
        usersTableReference.child(userID).child("checkedIn").setValue(false );
    }

    public void pay(String userID, int amount) {
        DatabaseReference userReference = usersTableReference.child(userID);
        userReference.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer currentBalance = currentData.child("balance").getValue(int.class);
                if (currentBalance != null) {
                    currentData.child("balance").setValue(currentBalance - amount);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                Log.d("Payment", "Payment Complete");
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
                Log.d("Payment", currentData.child("balance").getValue(int.class).toString());
            }
        });
    }


    public DatabaseReference getUserName(String userID) {
        return usersTableReference.child(userID).child("name");
    }
}