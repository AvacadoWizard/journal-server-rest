package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsernameLoginPanel extends JPanel {
    private JTextField usernameField;
    private JButton loginButton;
    private Main main;

    public UsernameLoginPanel(Main main) {
        this.main = main;
        setLayout(new BorderLayout());

        // Create the username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout());
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);
        add(usernamePanel, BorderLayout.CENTER);

        // Create the login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        add(loginButton, BorderLayout.SOUTH);
    }

    private void login() {
        String username = usernameField.getText();
        if (!username.isEmpty()) {
            main.setUsername(username);
            main.switchToMainPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a username.");
        }
    }
}