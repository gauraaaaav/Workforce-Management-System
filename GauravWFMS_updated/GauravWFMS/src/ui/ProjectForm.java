package ui;

import dao.Projectdao;
import model.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectForm extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextArea descArea = new JTextArea(4, 20);
    private final JTextField startField = new JTextField("yyyy-MM-dd");
    private final JTextField endField = new JTextField("yyyy-MM-dd");
    private final JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "Active", "Completed", "On Hold"});
    private final JButton saveBtn = new JButton("Create Project");

    private final Projectdao projectDao = new Projectdao();
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public ProjectForm() {
        super("Create Project");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 360);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y=0;
        c.gridx=0; c.gridy=y; form.add(new JLabel("Name"), c);
        c.gridx=1; c.gridy=y++; form.add(nameField, c);

        c.gridx=0; c.gridy=y; form.add(new JLabel("Description"), c);
        c.gridx=1; c.gridy=y++; 
        JScrollPane sp = new JScrollPane(descArea);
        form.add(sp, c);

        c.gridx=0; c.gridy=y; form.add(new JLabel("Start (yyyy-MM-dd)"), c);
        c.gridx=1; c.gridy=y++; form.add(startField, c);

        c.gridx=0; c.gridy=y; form.add(new JLabel("End (yyyy-MM-dd)"), c);
        c.gridx=1; c.gridy=y++; form.add(endField, c);

        c.gridx=0; c.gridy=y; form.add(new JLabel("Status"), c);
        c.gridx=1; c.gridy=y++; form.add(statusBox, c);

        c.gridwidth=2; c.gridx=0; c.gridy=y; 
        saveBtn.addActionListener(this::onSave);
        form.add(saveBtn, c);

        add(form);
    }

    private void onSave(ActionEvent e) {
        try {
            Project p = new Project();
            p.setName(nameField.getText().trim());
            p.setDescription(descArea.getText().trim());
            p.setStartDate(parseDate(startField.getText().trim()));
            p.setEndDate(parseDate(endField.getText().trim()));
            p.setStatus((String) statusBox.getSelectedItem());

            int id = projectDao.createProject(p);
            JOptionPane.showMessageDialog(this, id > 0 ? "Project created with ID: " + id : "Failed to create project");
            if (id > 0) dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    private Date parseDate(String s) {
        if (s == null || s.isEmpty() || s.equalsIgnoreCase("yyyy-MM-dd")) return null;
        try { return df.parse(s); } catch (ParseException ignored) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProjectForm().setVisible(true));
    }
}
