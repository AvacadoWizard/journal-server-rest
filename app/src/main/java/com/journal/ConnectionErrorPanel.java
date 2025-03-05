package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionErrorPanel extends JPanel {
    private Main main;

    public ConnectionErrorPanel(Main main) {
        this.main = main;
        setLayout(new BorderLayout());
        add(new JLabel("Error: Unable to connect to the remote database."), BorderLayout.CENTER);

        // Create the refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.switchToMainPanel();
            }
        });
        add(refreshButton, BorderLayout.SOUTH);
    }
}