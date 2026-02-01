package ui;

import dao.Payrolldao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class PayrollView extends JFrame {
    private final JTextField projectIdField = new JTextField();
    private final JTextField workerIdField = new JTextField();
    private final JButton generateBtn = new JButton("Generate Payroll");
    private final JButton refreshBtn = new JButton("Refresh List");
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);

    private final Payrolldao payrollDao = new Payrolldao();

    public PayrollView() {
        super("Payroll");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(580, 420);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(2,4,8,8));
        top.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        top.add(new JLabel("Project ID"));
        top.add(projectIdField);
        top.add(new JLabel("Worker ID"));
        top.add(workerIdField);
        top.add(generateBtn);
        top.add(new JLabel());
        top.add(refreshBtn);
        top.add(new JLabel());

        generateBtn.addActionListener(this::onGenerate);
        refreshBtn.addActionListener(this::onRefresh);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        onRefresh(null);
    }

    private void onGenerate(ActionEvent e) {
        try {
            int pid = Integer.parseInt(projectIdField.getText().trim());
            int wid = Integer.parseInt(workerIdField.getText().trim());
            double total = payrollDao.calculateAndPersistPayroll(wid, pid);
            JOptionPane.showMessageDialog(this, total > 0 
                    ? "Payroll generated: " + total 
                    : "No hours/rate found for the pair");
            onRefresh(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void onRefresh(ActionEvent e) {
        model.clear();
        try {
            List<String> rows = payrollDao.listPayrollRows();
            if (rows.isEmpty()) {
                model.addElement("(No payroll rows yet)");
            } else {
                rows.forEach(model::addElement);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            model.addElement("Error loading payroll: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PayrollView().setVisible(true));
    }
}
