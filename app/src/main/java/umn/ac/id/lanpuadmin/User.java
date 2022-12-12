package umn.ac.id.lanpuadmin;

public class User {
    public String id, name, email, role;
    public long balance;
    public boolean checkedIn;

    public User() {}

    public User(String name, String email, int balance){
//      Create user id based on time';
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.checkedIn = false;
        this.role = "user";
    }

    public User(String id, String name, String email, int balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.checkedIn = false;
        this.role = "user";
    }
}
