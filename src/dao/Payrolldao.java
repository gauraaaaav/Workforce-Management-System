package dao;

import db.Database;
import model.Payroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Payrolldao {

    // Calculate and insert payroll for a worker in a project
    public boolean calculatePayroll(int workerId, int projectId) {
        String sql = "INSERT INTO payroll (worker_id, project_id, hours_worked, total_pay) " +
                     "SELECT pw.worker_id, pw.project_id, pw.hours_worked, (pw.hours_worked * w.rate_per_hour) " +
                     "FROM project_workers pw JOIN workers w ON pw.worker_id = w.worker_id " +
                     "WHERE pw.worker_id=? AND pw.project_id=? " +
                     "ON DUPLICATE KEY UPDATE hours_worked=VALUES(hours_worked), total_pay=VALUES(total_pay)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workerId);
            ps.setInt(2, projectId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Get all payroll records
    public List<Payroll> getAllPayrolls() {
        List<Payroll> list = new ArrayList<>();
        String sql = "SELECT * FROM payroll";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Payroll p = new Payroll(
                        rs.getInt("payroll_id"),
                        rs.getInt("worker_id"),
                        rs.getInt("project_id"),
                        rs.getDouble("hours_worked"),
                        rs.getDouble("total_pay")
                );
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
