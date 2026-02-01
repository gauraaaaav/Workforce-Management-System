package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import dao.Workerdao;
import model.Worker;

public class EmployeeAdminPanel extends JPanel {

    private JTextField txtName, txtSkill, txtPhone, txtLocation, txtRate, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnRateFilter;
    private JTable tbl;
    private Workerdao dao = new Workerdao();
    private boolean ascending = true;

    public EmployeeAdminPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // ===== Top panel: input fields =====
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        inputPanel.setBackground(new Color(240, 248, 255));

        txtName = new JTextField();
        txtSkill = new JTextField();
        txtPhone = new JTextField();
        txtLocation = new JTextField();
        txtRate = new JTextField();

        inputPanel.add(new JLabel("Name"));
        inputPanel.add(new JLabel("Skill"));
        inputPanel.add(new JLabel("Phone"));
        inputPanel.add(new JLabel("Location"));
        inputPanel.add(new JLabel("Rate"));

        inputPanel.add(txtName);
        inputPanel.add(txtSkill);
        inputPanel.add(txtPhone);
        inputPanel.add(txtLocation);
        inputPanel.add(txtRate);

        add(inputPanel, BorderLayout.NORTH);

        // ===== Middle panel: buttons + search + rate filter =====
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        middlePanel.setBackground(new Color(240, 248, 255));

        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnRateFilter = new JButton("Rate ↑");
        txtSearch = new JTextField(15);

        middlePanel.add(btnAdd);
        middlePanel.add(btnUpdate);
        middlePanel.add(btnDelete);
        middlePanel.add(new JLabel("Search:"));
        middlePanel.add(txtSearch);
        middlePanel.add(btnRefresh);
        middlePanel.add(btnRateFilter);

        add(middlePanel, BorderLayout.CENTER);

        // ===== Table =====
        tbl = new JTable();
        JScrollPane scrollPane = new JScrollPane(tbl);
        add(scrollPane, BorderLayout.SOUTH);

        tbl.setModel(new DefaultTableModel(new Object[]{"ID", "Name", "Skill", "Phone", "Location", "Rate"}, 0));
        loadWorkers();

        // ===== Zebra stripe & center alignment =====
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    setBackground(new Color(135, 206, 250));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                    setForeground(Color.BLACK);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });

        // ===== Live search =====
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterAndSort(); }
            public void removeUpdate(DocumentEvent e) { filterAndSort(); }
            public void changedUpdate(DocumentEvent e) { filterAndSort(); }
        });

        // ===== Refresh =====
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            ascending = true;
            btnRateFilter.setText("Rate ↑");
            loadWorkers();
        });

        // ===== Rate filter =====
        btnRateFilter.addActionListener(e -> {
            ascending = !ascending;
            btnRateFilter.setText(ascending ? "Rate ↑" : "Rate ↓");
            filterAndSort();
        });

        // ===== Table row click: fill input fields =====
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tbl.getSelectedRow();
                if (selectedRow != -1) {
                    txtName.setText(tbl.getValueAt(selectedRow, 1).toString());
                    txtSkill.setText(tbl.getValueAt(selectedRow, 2).toString());
                    txtPhone.setText(tbl.getValueAt(selectedRow, 3).toString());
                    txtLocation.setText(tbl.getValueAt(selectedRow, 4).toString());
                    txtRate.setText(tbl.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        // ===== Add, Update, Delete actions =====
        btnAdd.addActionListener(e -> {
            try {
                Worker w = new Worker(
                        txtName.getText(),
                        txtSkill.getText(),
                        txtPhone.getText(),
                        txtLocation.getText(),
                        Double.parseDouble(txtRate.getText())
                );
                dao.addWorker(w);
                loadWorkers();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding worker: " + ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                int row = tbl.getSelectedRow();
                if (row == -1) return;
                int id = (int) tbl.getValueAt(row, 0);
                Worker w = new Worker(
                        id,
                        txtName.getText(),
                        txtSkill.getText(),
                        txtPhone.getText(),
                        txtLocation.getText(),
                        Double.parseDouble(txtRate.getText())
                );
                dao.updateWorker(w);
                loadWorkers();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating worker: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                int row = tbl.getSelectedRow();
                if (row == -1) return;
                int id = (int) tbl.getValueAt(row, 0);
                dao.deleteWorker(id);
                loadWorkers();
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting worker: " + ex.getMessage());
            }
        });
    }

    private void loadWorkers() {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        model.setRowCount(0);
        try {
            List<Worker> list = dao.getAllWorkers();
            list.sort(Comparator.comparingDouble(Worker::getRate));
            for (Worker w : list) {
                model.addRow(new Object[]{w.getId(), w.getName(), w.getSkill(), w.getPhone(), w.getLocation(), w.getRate()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading workers: " + ex.getMessage());
        }
    }

    private void filterAndSort() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        model.setRowCount(0);

        try {
            List<Worker> list = dao.getAllWorkers();
            List<Worker> filtered = new ArrayList<>();

            for (Worker w : list) {
                if (w.getName().toLowerCase().contains(keyword) ||
                        w.getSkill().toLowerCase().contains(keyword) ||
                        w.getLocation().toLowerCase().contains(keyword)) {
                    filtered.add(w);
                }
            }

            filtered.sort((w1, w2) -> ascending ?
                    Double.compare(w1.getRate(), w2.getRate()) :
                    Double.compare(w2.getRate(), w1.getRate()));

            for (Worker w : filtered) {
                model.addRow(new Object[]{w.getId(), w.getName(), w.getSkill(), w.getPhone(), w.getLocation(), w.getRate()});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error filtering workers: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtSkill.setText("");
        txtPhone.setText("");
        txtLocation.setText("");
        txtRate.setText("");
    }
}
