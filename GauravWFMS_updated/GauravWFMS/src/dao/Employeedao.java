package dao;

import model.Employee;
import db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employeedao {

    public void create(Employee e) throws SQLException {
        String sql = "INSERT INTO employees(name, skill, phone, location, rate_per_hour) VALUES(?,?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getSkill());
            ps.setString(3, e.getPhone());
            ps.setString(4, e.getLocation());
            if (e.getRatePerHour() == null) ps.setNull(5, Types.REAL);
            else ps.setDouble(5, e.getRatePerHour());
            ps.executeUpdate();
        }
    }

    public void update(Employee e) throws SQLException {
        String sql = "UPDATE employees SET name=?, skill=?, phone=?, location=?, rate_per_hour=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getSkill());
            ps.setString(3, e.getPhone());
            ps.setString(4, e.getLocation());
            if (e.getRatePerHour() == null) ps.setNull(5, Types.REAL);
            else ps.setDouble(5, e.getRatePerHour());
            ps.setInt(6, e.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Employee findById(int id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Employee> search(String keyword) throws SQLException {
        String like = "%" + keyword.toLowerCase() + "%";
        String sql = "SELECT * FROM employees WHERE lower(name) LIKE ? OR lower(skill) LIKE ? OR lower(location) LIKE ?";
        List<Employee> out = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        }
        return out;
    }

    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT * FROM employees ORDER BY id DESC";
        List<Employee> out = new ArrayList<>();
        try (Connection c = Database.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    private Employee map(ResultSet rs) throws SQLException {
        return new Employee(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("skill"),
            rs.getString("phone"),
            rs.getString("location"),
            (rs.getObject("rate_per_hour") == null) ? null : rs.getDouble("rate_per_hour")
        );
    }
}
