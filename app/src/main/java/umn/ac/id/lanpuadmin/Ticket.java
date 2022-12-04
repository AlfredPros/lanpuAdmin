package umn.ac.id.lanpuadmin;

public class Ticket {
    public String ticketID;
    public String userID;
    public String name;
    public String category;
    public String entryTime;
    public String exitTime;
    public long  price;

    public Ticket(){}

    public Ticket(String ticketID, String userID, String name, String category, String entryTime){
        this.ticketID = ticketID;
        this.userID = userID;
        this.name = name;
        this.category = category;
        this.entryTime = entryTime;
        this.exitTime = null;
        this.price = 0;
    }

    public Ticket(String ticketID, String userID, String name, String category, String entryTime, int exitTime, long price) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.name = name;
        this.category = category;
        this.entryTime = entryTime;
        this.exitTime = null;
        this.price = price;
    }
}
