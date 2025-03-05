package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    private UsernameLoginPanel usernameLoginPanel;
    private JPanel mainPanel;
    private String username;
    private ConnectionErrorPanel connectionErrorPanel;
    private MyEntriesPanel myEntriesPanel;
    private EntriesPanel entriesPanel;
    private CreateEntryPanel createEntryPanel;

    public Main() {
        // Set up the main frame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Journal Server");

        // Create the username login panel
        usernameLoginPanel = new UsernameLoginPanel(this);
        add(usernameLoginPanel);
        setVisible(true);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void switchToMainPanel() {
        remove(usernameLoginPanel);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create the button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create the buttons
        JButton journalEntriesButton = new JButton("Journal Entries");
        JButton myEntriesButton = new JButton("My Entries");
        JButton createEntryButton = new JButton("Create Entry");

        // Add action listeners to the buttons
        journalEntriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToEntriesPanel();
            }
        });

        myEntriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToMyEntriesPanel();
            }
        });

        createEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToCreateEntryPanel();
            }
        });

        // Add the buttons to the button panel
        buttonPanel.add(journalEntriesButton);
        buttonPanel.add(myEntriesButton);
        buttonPanel.add(createEntryButton);

        // Create the panels
        myEntriesPanel = new MyEntriesPanel(this, username);
        entriesPanel = new EntriesPanel(this);
        createEntryPanel = new CreateEntryPanel(this, username);

        // Add the panels to the main panel
        mainPanel.add(myEntriesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);
        revalidate();
        repaint();
    }

    public void switchToMyEntriesPanel() {
        mainPanel.remove(entriesPanel);
        mainPanel.remove(createEntryPanel);
        myEntriesPanel.refreshEntries(); // Refresh the entries panel
        mainPanel.add(myEntriesPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void switchFromEdit(EditEntryPanel editEntryPanel) {
        mainPanel.remove(entriesPanel);
        mainPanel.remove(createEntryPanel);
        mainPanel.remove(editEntryPanel);
        myEntriesPanel.refreshEntries();
        mainPanel.add(myEntriesPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void switchToEntriesPanel() {
        mainPanel.remove(myEntriesPanel);
        mainPanel.remove(createEntryPanel);
        entriesPanel.refreshEntries(entriesPanel);
        mainPanel.add(entriesPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void switchToEditEntryPanel(EditEntryPanel editEntryPanel) {
        mainPanel.remove(myEntriesPanel);
        mainPanel.remove(entriesPanel);
        mainPanel.add(editEntryPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    public void switchToCreateEntryPanel() {
        mainPanel.remove(myEntriesPanel);
        mainPanel.remove(entriesPanel);
        mainPanel.add(createEntryPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void switchToConnectionErrorPanel() {
        remove(mainPanel);
        connectionErrorPanel = new ConnectionErrorPanel(this);
        add(connectionErrorPanel);
        revalidate();
        repaint();
    }
    public static void main(String[] args) {
        new Main();
    }
}