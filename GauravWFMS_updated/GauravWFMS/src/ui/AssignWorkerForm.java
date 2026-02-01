package ui;

import dao.Projectdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class AssignWorkerForm extends JFrame {
    private final JTextField projectIdField = new JTextField();
    private final JTextField workerIdField = new JTextField();
    private final JButton assignBtn = new JButton("Assign Worker");
    private final JButton removeBtn = new JButton("Remove Worker");

    private final Projectdao projectDao = new Projectdao();

    public AssignWorkerForm() {
        super("Assign/Remove Worker to Project");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(380, 200);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(3,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        p.add(new JLabel("Project ID")); p.add(projectIdField);
        p.add(new JLabel("Worker ID"));  p.add(workerIdField);
        p.add(assignBtn); p.add(removeBtn);

        assignBtn.addActionListener(this::onAssign);
        removeBtn.addActionListener(this::onRemove);

        add(p);
    }

    private void onAssign(ActionEvent e) {
        try {
            int pid = Integer.parseInt(projectIdField.getText().trim());
            int wid = Integer.parseInt(workerIdField.getText().trim());
            projectDao.assignWorker(pid, wid);
            JOptionPane.showMessageDialog(this, "Assigned worker " + wid + " to project " + pid);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onRemove(ActionEvent e) {
        try {
            int pid = Integer.parseInt(projectIdField.getText().trim());
            int wid = Integer.parseInt(workerIdField.getText().trim());
            projectDao.removeWorker(pid, wid);
            JOptionPane.showMessageDialog(this, "Removed worker " + wid + " from project " + pid);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AssignWorkerForm().setVisible(true));
    }
}
