package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.time.LocalDate;

public class CreateEntryPanel extends JPanel {
    private JTextField titleField;
    private JTextArea entryField;
    private JButton saveButton;
    private JButton cancelButton;
    private Main main;

    public CreateEntryPanel(Main main) {
        this.main = main;
        setLayout(new BorderLayout());

        // Create the title field
        titleField = new JTextField(20);
        titleField.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("Entry Title:"));
        titlePanel.add(titleField);
        add(titlePanel, BorderLayout.NORTH);

        // Create the entry field
        entryField = new JTextArea(10, 40);
        entryField.setLineWrap(true);
        entryField.setWrapStyleWord(true);
        add(new JScrollPane(entryField), BorderLayout.CENTER);

        // Create the save and cancel buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEntry();
            }
        });
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.switchToMyEntriesPanel();
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add a character limit to the entry field
        entryField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkCharacterLimit();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkCharacterLimit();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkCharacterLimit();
            }

            private void checkCharacterLimit() {
                if (entryField.getText().length() > 250) {
                    entryField.setText(entryField.getText().substring(0, 250));
                }
            }
        });
    }

    private void saveEntry() {
        String title = titleField.getText();
        String entryText = entryField.getText();

        if (!title.isEmpty() && !entryText.isEmpty()) {
            Entry entry = new Entry(title, LocalDate.now().toString(), entryText);
            SQLiteDatabase database = new SQLiteDatabase();
            database.insertEntry(entry);
            database.closeConnection();

            main.switchToMyEntriesPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both a title and entry text.");
        }
    }
}