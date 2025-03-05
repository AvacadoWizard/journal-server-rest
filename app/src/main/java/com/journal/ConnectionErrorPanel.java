package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// simple pannel! is shown if there is an error with connections.
public class ConnectionErrorPanel extends JPanel {
    private Main main;

    public ConnectionErrorPanel(Main main) {
        this.main = main;
        setLayout(new BorderLayout());
        add(new JLabel("Error: Unable to connect to the remote database."), BorderLayout.CENTER);

        //  refresh button which switches to main panel
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