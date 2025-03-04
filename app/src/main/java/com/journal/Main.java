package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton journalEntriesButton;
    private JButton myEntriesButton;
    private JButton createEntryButton;
    private MyEntriesPanel myEntriesPanel;
    private EntriesPanel entriesPanel;
    private CreateEntryPanel createEntryPanel;

    public Main() {
        // Set up the main frame
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Journal Server");

        // Create the main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Create the button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create the buttons
        journalEntriesButton = new JButton("Journal Entries");
        myEntriesButton = new JButton("My Entries");
        createEntryButton = new JButton("Create Entry");

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
        myEntriesPanel = new MyEntriesPanel();
        entriesPanel = new EntriesPanel();
        createEntryPanel = new CreateEntryPanel(this);
        myEntriesPanel.refreshEntries();

        // Add the panels to the main panel
        mainPanel.add(myEntriesPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(mainPanel);

        // Make the frame visible
        setVisible(true);
    }

    private void switchToEntriesPanel() {
        mainPanel.remove(myEntriesPanel);
        mainPanel.remove(createEntryPanel);
        mainPanel.add(entriesPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void switchToMyEntriesPanel() {
        mainPanel.remove(entriesPanel);
        mainPanel.remove(createEntryPanel);
        myEntriesPanel.refreshEntries(); // Refresh the entries panel
        mainPanel.add(myEntriesPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void switchToCreateEntryPanel() {
        mainPanel.remove(myEntriesPanel);
        mainPanel.remove(entriesPanel);
        mainPanel.add(createEntryPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        new Main();
    }
}