package dao;

import model.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Workerdao {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/workforce_db", "root", "");
    }

    // ✅ INSERT new Worker
    // ✅ Insert worker
public boolean addWorker(Worker w) {
    String sql = "INSERT INTO workers (name, skill, phone, location, rate_per_hour) VALUES (?,?,?,?,?)";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, w.getName());
        ps.setString(2, w.getSkill());
        ps.setString(3, w.getPhone());
        ps.setString(4, w.getLocation());

        // ✅ Validate and convert rate safely
        double rateValue = 0.0;
        try {
            rateValue = (w.getRate());
        } catch (NumberFormatException e) {
            System.err.println("⚠ Invalid rate value: " + w.getRate() + ". Defaulting to 0.0");
        }
        ps.setDouble(5, rateValue);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // ✅ SELECT all workers
    public List<Worker> getAllWorkers() {
        List<Worker> list = new ArrayList<>();
        String sql = "SELECT * FROM workers";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("skill"),
                        rs.getString("phone"),
                        rs.getString("location"),
rs.getDouble("rate_per_hour")   // if Worker constructor takes double
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ SEARCH workers
    public List<Worker> searchWorkers(String keyword) {
        List<Worker> list = new ArrayList<>();
        String sql = "SELECT * FROM workers WHERE skill LIKE ? OR location LIKE ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Worker(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("skill"),
                        rs.getString("phone"),
                        rs.getString("location"),
rs.getDouble("rate_per_hour")   // if Worker constructor takes double
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ UPDATE worker
    public boolean updateWorker(Worker w) {
        String sql = "UPDATE workers SET name=?, skill=?, phone=?, location=?, rate_per_hour=? WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, w.getName());
            ps.setString(2, w.getSkill());
            ps.setString(3, w.getPhone());
            ps.setString(4, w.getLocation());
            ps.setDouble(5, (w.getRate()));
            ps.setInt(6, w.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ DELETE worker
    public boolean deleteWorker(int id) {
        String sql = "DELETE FROM workers WHERE id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
