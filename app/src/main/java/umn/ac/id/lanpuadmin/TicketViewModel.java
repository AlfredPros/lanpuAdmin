package umn.ac.id.lanpuadmin;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TicketViewModel {
    private static final DatabaseReference activeTicketTableReference = FirebaseDatabase.getInstance().getReference("ActiveTickets");
    private static final DatabaseReference ticketsTableReference = FirebaseDatabase.getInstance().getReference("Tickets");
    private static final DatabaseReference usersTableReference = FirebaseDatabase.getInstance().getReference("Users");


    public void createTicket(String userID, String name, String category) {

//        TODO: error handling jika user sudah memiliki active ticket tidak boleh membuat tiket baru

        DatabaseReference newTicketReference = ticketsTableReference.push();
        String ticketID = newTicketReference.getKey();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long entryTime = calendar.get(Calendar.MILLISECOND);
//        public Ticket(String ticketID,String userID, String name, String category, long entryTime){
        Ticket ticket = new Ticket(ticketID, userID, name, category, entryTime);
        newTicketReference.setValue(ticket).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                 Log.d("Add", "Added to Ticket List " + ticketID);
            }
        });

//        Masukkan ke active Ticket List
        activeTicketTableReference.child(userID).setValue(ticketID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Add", "Added to Active Tickets");
            }
        });
    }
}
