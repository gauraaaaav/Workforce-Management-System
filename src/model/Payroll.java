package model;

public class Payroll {
    private int id;
    private int workerId;
    private int projectId;
    private double hoursWorked;
    private double totalPay;

    public Payroll() {}

    public Payroll(int id, int workerId, int projectId, double hoursWorked, double totalPay) {
        this.id = id;
        this.workerId = workerId;
        this.projectId = projectId;
        this.hoursWorked = hoursWorked;
        this.totalPay = totalPay;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getWorkerId() { return workerId; }
    public void setWorkerId(int workerId) { this.workerId = workerId; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public double getTotalPay() { return totalPay; }
    public void setTotalPay(double totalPay) { this.totalPay = totalPay; }
}
