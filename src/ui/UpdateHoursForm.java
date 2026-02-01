package ui;

import dao.Workerdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UpdateHoursForm extends JFrame {
    private final JTextField projectIdField = new JTextField();
    private final JTextField workerIdField = new JTextField();
    private final JTextField hoursField = new JTextField();
    private final JButton addBtn = new JButton("Add Hours");

    private final Workerdao workerDao = new Workerdao();

    public UpdateHoursForm() {
        super("Log/Update Hours");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(360, 180);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(4,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        p.add(new JLabel("Project ID")); p.add(projectIdField);
        p.add(new JLabel("Worker ID"));  p.add(workerIdField);
        p.add(new JLabel("Hours (+)"));  p.add(hoursField);
        p.add(new JLabel());             p.add(addBtn);

        addBtn.addActionListener(this::onAdd);
        add(p);
    }

    private void onAdd(ActionEvent e) {
        try {
            int pid = Integer.parseInt(projectIdField.getText().trim());
            int wid = Integer.parseInt(workerIdField.getText().trim());
            double hrs = Double.parseDouble(hoursField.getText().trim());
            workerDao.updateHours(wid, pid, hrs);
            JOptionPane.showMessageDialog(this, "Added " + hrs + " hours (Worker " + wid + ", Project " + pid + ")");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateHoursForm().setVisible(true));
    }
}
