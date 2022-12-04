package umn.ac.id.lanpuadmin;

public class Ticket {
    public String ticketID, userID, name, category;
    public long entryTime, exitTime, price;

    public Ticket(){}

    public Ticket(String ticketID,String userID, String name, String category, long entryTime){
        this.ticketID = ticketID;
        this.userID = userID;
        this.name = name;
        this.category = category;
        this.entryTime = entryTime;
        this.exitTime = 0;
        this.price = 0;
    }

    public Ticket(String ticketID, String userID, String name, String category, long entryTime, long exitTime, long price) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.name = name;
        this.category = category;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.price = price;
    }
}
