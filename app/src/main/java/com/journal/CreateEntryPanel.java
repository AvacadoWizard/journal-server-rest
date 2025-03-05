package com.journal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateEntryPanel extends JPanel {
    private JTextField titleField;
    private JTextArea entryField;
    private JButton saveButton;
    private JButton cancelButton;
    private Main main;
    private String username;
    private JCheckBox publicCheckBox;

    public CreateEntryPanel(Main main, String username) {
        this.main = main;
        this.username = username;
        setLayout(new BorderLayout());

        // prompt user to enter title
        titleField = new JTextField(20);
        titleField.setFont(new Font("Arial", Font.PLAIN, 12));
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("Entry Title:"));
        titlePanel.add(titleField);
        add(titlePanel, BorderLayout.NORTH);

        // allow user's to input their title
        entryField = new JTextArea(10, 40);
        entryField.setLineWrap(true);
        entryField.setWrapStyleWord(true);
        add(new JScrollPane(entryField), BorderLayout.CENTER);

        // create the public checkbox
        publicCheckBox = new JCheckBox("Make this post public");
        publicCheckBox.setSelected(false); // default to private!
        add(publicCheckBox, BorderLayout.EAST);

        // create buttons to save or cancel the editing process
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
    }

    private void saveEntry() {
        String title = titleField.getText();
        String entryText = entryField.getText();
        boolean isPublic = publicCheckBox.isSelected();
    
        if (!title.isEmpty() && !entryText.isEmpty()) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:3000/entries/" + username + "/" + title.replace(" ", "%20")))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(this, "An entry with this title already exists.");
                } else {
                    HttpRequest postRequest = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:3000/entries"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"" + title + "\",\"datePublished\":\"" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\",\"entryText\":\"" + entryText + "\",\"username\":\"" + username + "\",\"isPublic\":" + isPublic + "}"))
                            .build();
                    HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
                    if (postResponse.statusCode() == 200) {
                        main.switchToMyEntriesPanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to save entry" + postResponse.statusCode());
                        main.switchToConnectionErrorPanel();
                    }
                }
            } catch (IOException | InterruptedException e) {
                main.switchToConnectionErrorPanel();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both a title and entry text.");
        }
    }
}