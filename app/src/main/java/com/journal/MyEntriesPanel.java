package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyEntriesPanel extends JPanel {
    private SQLiteDatabase database;
    private JPanel entriesPanel;

    public MyEntriesPanel() {
        database = new SQLiteDatabase();
        setLayout(new BorderLayout());

        entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(entriesPanel), BorderLayout.CENTER);
    }
    public void refreshEntries() {
        entriesPanel.removeAll();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
    
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centering container
        wrapperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        List<Entry> entries = database.getAllEntries();
    
        for (Entry entry : entries) {
            JPanel entryPanel = new JPanel();
            entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
            entryPanel.setPreferredSize(new Dimension(300, 150));
            entryPanel.setMaximumSize(new Dimension(300, 150));
    
            JLabel titleLabel = new JLabel(entry.getTitle());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            entryPanel.add(titleLabel);
    
            JLabel subtitleLabel = new JLabel("Published on " + entry.getDatePublished());
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            entryPanel.add(subtitleLabel);
    
            JTextArea entryText = new JTextArea(entry.getEntryText());
            entryText.setFont(new Font("Arial", Font.PLAIN, 12));
            entryText.setLineWrap(true);
            entryText.setWrapStyleWord(true);
            entryText.setEditable(false);
            entryText.setOpaque(false);
    
            JScrollPane scrollPane = new JScrollPane(entryText);
            scrollPane.setPreferredSize(new Dimension(280, 80));
            scrollPane.setMaximumSize(new Dimension(280, 80));
            scrollPane.setBorder(null);
    
            entryPanel.add(scrollPane);
            entryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    
            wrapperPanel.add(entryPanel); // Add to the centering wrapper
        }
    
        entriesPanel.add(wrapperPanel); // Add the wrapper to the main panel
        entriesPanel.revalidate();
        entriesPanel.repaint();
    }
    
    
}