package model;

public class Employee {
    private int id;
    private String name;
    private String skill;
    private String phone;
    private String location;
    private Double ratePerHour;

    public Employee() {}

    public Employee(int id, String name, String skill, String phone, String location, Double ratePerHour) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.phone = phone;
        this.location = location;
        this.ratePerHour = ratePerHour;
    }

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
    public Double getRatePerHour() { return ratePerHour; }
    public void setRatePerHour(Double ratePerHour) { this.ratePerHour = ratePerHour; }

    @Override
    public String toString() {
        return name + " (" + skill + ")";
    }
}
