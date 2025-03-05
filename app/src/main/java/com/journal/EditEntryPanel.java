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


// this is a panel that user's can edit the text of their panel... and only that
public class EditEntryPanel extends JPanel {
    private Main main;
    private String username;
    private String title;
    private JTextArea entryTextArea;

    public EditEntryPanel(Main main, String username, String title, String entryText) {
        this.main = main;
        this.username = username;
        this.title = title;
        setLayout(new BorderLayout());

        // this is for title fields
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // entry field for editing post!!!
        entryTextArea = new JTextArea(entryText);
        entryTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        entryTextArea.setLineWrap(true);
        entryTextArea.setWrapStyleWord(true);
        add(new JScrollPane(entryTextArea), BorderLayout.CENTER);

        // Create the save and cancel buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEntry();
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.switchFromEdit(EditEntryPanel.this);
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // this acutally saves to the database
    private void saveEntry() {
        String entryText = entryTextArea.getText();
        System.out.println("Entry text: " + entryText);
    
        String encodedTitle = title.replace(" ", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/entries/" + username + "/" + encodedTitle))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString("{\"entryText\":\"" + entryText + "\"}"))
                .build();
    
        System.out.println("Request URI: " + request.uri());
        System.out.println("Request body: " + request.bodyPublisher().get());
    
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            if (response.statusCode() == 200) {
                System.out.println("Switching to My Entries panel");
                main.switchFromEdit(this);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save entry" + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this, "Failed to save entry");
        }
    }
}