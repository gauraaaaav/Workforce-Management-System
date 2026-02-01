package model;

public class Worker {
    private int id;
    private String name;
    private String skill;
    private String phone;
    private String location;
    private double rate;   // ✅ should be double not String

    // Constructor for inserting new worker (no ID yet)
    public Worker(String name, String skill, String phone, String location, double rate) {
        this.name = name;
        this.skill = skill;
        this.phone = phone;
        this.location = location;
        this.rate = rate;
    }

    // Constructor for fetching from DB (with ID)
    public Worker(int id, String name, String skill, String phone, String location, double rate) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.phone = phone;
        this.location = location;
        this.rate = rate;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSkill() { return skill; }
    public void setSkill(String skill) { this.skill = skill; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
}
