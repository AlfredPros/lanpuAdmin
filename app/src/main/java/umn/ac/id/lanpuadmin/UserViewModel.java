package umn.ac.id.lanpuadmin;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserViewModel extends ViewModel {
    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");
    private static final DatabaseReference revenueReference = FirebaseDatabase.getInstance().getReference("revenue");

    public void checkInUser(String userID) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        usersTableReference.child(userID).child("checkedIn").setValue(true);
        usersTableReference.child(userID).child("entryTime").setValue(strDate);
    }

    public void checkOutUser(String userID){
        usersTableReference.child(userID).child("checkedIn").setValue(false);
        usersTableReference.child(userID).child("entryTime").setValue(null);
    }

    public void pay(String userID, int amount) {
        DatabaseReference userReference = usersTableReference.child(userID);
//        userReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                User currUser = task.getResult().getValue(User.class);
//                userReference.child("balance").setValue(currUser.balance - amount);
//            }
//        });
        this.topUP(userID, -amount);
        Log.d("PAY", "balance called");
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
