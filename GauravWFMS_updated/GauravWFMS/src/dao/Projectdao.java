package dao;

import db.Database;
import model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Projectdao {

    // Create project
    public boolean addProject(Project p) {
        String sql = "INSERT INTO projects (name, description, start_date, end_date) VALUES (?,?,?,?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDate(3, new java.sql.Date(p.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(p.getEndDate().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Update project
    public boolean updateProject(Project p) {
        String sql = "UPDATE projects SET name=?, description=?, start_date=?, end_date=? WHERE project_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDate(3, new java.sql.Date(p.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(p.getEndDate().getTime()));
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Delete project
    public boolean deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE project_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Get all projects
    public List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY project_id DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Project p = new Project(
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date")
                );
                list.add(p);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Assign worker to project
    public boolean assignWorkerToProject(int projectId, int workerId) {
        String sql = "INSERT INTO project_workers (project_id, worker_id, hours_worked) VALUES (?,?,0) " +
                     "ON DUPLICATE KEY UPDATE project_id=project_id";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, workerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
