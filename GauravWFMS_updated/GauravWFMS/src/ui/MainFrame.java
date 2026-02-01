package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import model.User;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private JButton btnAdminPanel, btnSearchPanel;
    private final Color BG_COLOR = new Color(240, 248, 255);

    private EmployeeAdminPanel adminPanel;
    private EmployeeSearchPanel searchPanel;
    private User currentUser;

    public MainFrame(User user) throws SQLException {
        super("Workforce Management System");
        this.currentUser = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        // ===== Top Navigation Bar =====
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(70, 130, 180));

        // Left buttons
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftPanel.setOpaque(false);

        btnSearchPanel = new JButton("Search Panel");
        styleNavButton(btnSearchPanel, new Color(100, 149, 237));
        leftPanel.add(btnSearchPanel);

        if (currentUser.isAdmin()) {
            btnAdminPanel = new JButton("Admin Panel");
            styleNavButton(btnAdminPanel, new Color(100, 149, 237));
            leftPanel.add(btnAdminPanel);
        }

        navBar.add(leftPanel, BorderLayout.WEST);

        // Right buttons: Logout & Exit
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        JButton btnLogout = new JButton("Logout");
        JButton btnExit = new JButton("Exit");
        styleNavButton(btnLogout, new Color(220, 20, 60));
        styleNavButton(btnExit, new Color(178, 34, 34));
        rightPanel.add(btnLogout);
        rightPanel.add(btnExit);
        navBar.add(rightPanel, BorderLayout.EAST);

        // ===== Logout & Exit actions =====
        btnLogout.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
        });
        btnExit.addActionListener(e -> System.exit(0));

        add(navBar, BorderLayout.NORTH);

        // ===== CardLayout Container =====
        container = new JPanel();
        cardLayout = new CardLayout();
        container.setLayout(cardLayout);
        container.setBackground(BG_COLOR);

        searchPanel = new EmployeeSearchPanel();
        container.add(searchPanel, "Search");

        if (currentUser.isAdmin()) {
            adminPanel = new EmployeeAdminPanel();
            container.add(adminPanel, "Admin");
        }

        add(container, BorderLayout.CENTER);

        // ===== Button Actions =====
        btnSearchPanel.addActionListener(e -> cardLayout.show(container, "Search"));
        if (currentUser.isAdmin() && btnAdminPanel != null) {
            btnAdminPanel.addActionListener(e -> cardLayout.show(container, "Admin"));
        }

        setVisible(true);
    }

    // ===== Helper method to style buttons =====
    private void styleNavButton(JButton btn, Color baseColor) {
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBackground(baseColor);

        Color hoverColor = baseColor.brighter();
        Color clickColor = baseColor.darker();

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hoverColor); }
            public void mouseExited(MouseEvent e) { btn.setBackground(baseColor); }
            public void mousePressed(MouseEvent e) { btn.setBackground(clickColor); }
            public void mouseReleased(MouseEvent e) { btn.setBackground(hoverColor); }
        });
    }
}
