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
import model.Worker;
import dao.Workerdao;

public class EmployeeSearchPanel extends JPanel {

    private JTextField txtSearch;
    private JButton btnRefresh, btnRateFilter;
    private JTable tbl;
    private Workerdao dao = new Workerdao();
    private boolean ascending = true; // for rate sorting

    public EmployeeSearchPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // ===== Top panel: search + buttons =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(240, 248, 255));

        txtSearch = new JTextField(20);
        btnRefresh = new JButton("Refresh");
        btnRateFilter = new JButton("Rate ↑");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(txtSearch);
        topPanel.add(btnRefresh);
        topPanel.add(btnRateFilter);

        add(topPanel, BorderLayout.NORTH);

        // ===== Table setup =====
        tbl = new JTable();
        JScrollPane scrollPane = new JScrollPane(tbl);
        add(scrollPane, BorderLayout.CENTER);

        tbl.setModel(new DefaultTableModel(new Object[]{"ID", "Name", "Skill", "Phone", "Location", "Rate"}, 0));
        loadWorkers();

        // ===== Custom renderer for zebra striping =====
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    setBackground(new Color(135, 206, 250));
                    setForeground(Color.BLACK);
                } else {
                    if (row % 2 == 0) {
                        setBackground(new Color(245, 245, 245));
                    } else {
                        setBackground(Color.WHITE);
                    }
                    setForeground(Color.BLACK);
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });

        // ===== Search field live update =====
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterAndSort(); }
            public void removeUpdate(DocumentEvent e) { filterAndSort(); }
            public void changedUpdate(DocumentEvent e) { filterAndSort(); }
        });

        // ===== Refresh button =====
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            ascending = true;
            btnRateFilter.setText("Rate ↑");
            loadWorkers();
        });

        // ===== Rate filter button =====
        btnRateFilter.addActionListener(e -> {
            ascending = !ascending;
            btnRateFilter.setText(ascending ? "Rate ↑" : "Rate ↓");
            filterAndSort();
        });
    }

    // ===== Load all workers =====
    private void loadWorkers() {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        model.setRowCount(0);
        try {
            List<Worker> list = dao.getAllWorkers();
            list.sort(Comparator.comparingDouble(Worker::getRate)); // default ascending
            for (Worker w : list) {
                model.addRow(new Object[]{w.getId(), w.getName(), w.getSkill(), w.getPhone(), w.getLocation(), w.getRate()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading workers: " + ex.getMessage());
        }
    }

    // ===== Filter + sort =====
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

            // Sort by rate
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
}
