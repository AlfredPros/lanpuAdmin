package umn.ac.id.lanpuadmin;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TicketViewModel extends ViewModel {
    private static final DatabaseReference activeTicketTableReference = FirebaseDatabase.getInstance().getReference("ActiveTickets");
    private static final DatabaseReference ticketsTableReference = FirebaseDatabase.getInstance().getReference("Tickets");
    private static final DatabaseReference paymentRequestReference = FirebaseDatabase.getInstance().getReference("PaymentRequest");
    private static final UserViewModel userViewModel = new UserViewModel();


    public void createTicket(String userID, String name, String category) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        String strDate = sdf.format(c.getTime());


//        TODO: error handling jika user sudah memiliki active ticket tidak boleh membuat tiket baru

        DatabaseReference newTicketReference = ticketsTableReference.push();
        String ticketID = newTicketReference.getKey();
        String entryTime = strDate;


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

    public DatabaseReference getActiveTicket(String userID) {
        return activeTicketTableReference.child(userID);
    }

    public DatabaseReference getTicket(String ticketID) {
        return ticketsTableReference.child(ticketID);
    }


    public void checkOutTicket(String userID) {
        activeTicketTableReference.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String ticketID = task.getResult().getValue(String.class);
                getTicket(ticketID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Ticket currTicket = task.getResult().getValue(Ticket.class);
                        if (currTicket != null) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                            String strDate = sdf.format(c.getTime());
                            task.getResult().getRef().child("exitTime").setValue(strDate);
                            if (currTicket.category.equals("Motorcycle"))
                                task.getResult().getRef().child("price").setValue(2000);
                            if (currTicket.category.equals("Car"))
                                task.getResult().getRef().child("price").setValue(2000);
                        }

                        DatabaseReference newPaymentRequestReference = paymentRequestReference.child(userID);
                        newPaymentRequestReference.child("ticketID").setValue(ticketID);
                        newPaymentRequestReference.child("ack").setValue(false);
                        newPaymentRequestReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("ack").exists()) {
                                    boolean ack = snapshot.child("ack").getValue(boolean.class);
                                    if (ack) {
                                        // User membayar jika diberika acknowledgement
                                        userViewModel.pay(userID, (int) currTicket.price);
                                        userViewModel.checkOutUser(userID);
                                        // Active Ticket Dihapus
                                        activeTicketTableReference.child(userID).removeValue();
                                        // PaymentRequest Dihapus
                                        snapshot.getRef().getParent().removeValue();
                                        Log.d("PAYMENTREQUEST", "PAYMENTREQUEST REMOVED");
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });
    }

}
