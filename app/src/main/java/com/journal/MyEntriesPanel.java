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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyEntriesPanel extends JPanel {
    private Main main;
    private String username;
    private JPanel headerPanel;
    private JPanel entriesPanel;

    public MyEntriesPanel(Main main, String username) {
        this.main = main;
        this.username = username;
        setLayout(new BorderLayout());

        //  creates header to describe panel
        headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.decode("#F7F7F7"));
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#CCCCCC")));

        JLabel headerLabel = new JLabel("Your Entries");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);

        // initialize the main entry panel panel
        entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));

        // add a header to describe page
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(entriesPanel), BorderLayout.CENTER);

        refreshEntries();
    }

    // function to add all of user's entries
    public void refreshEntries() {
        entriesPanel.removeAll();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3000/entries/" + username))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                System.out.println(responseBody);
                List<Map<String, String>> dictionaries = parseJson(responseBody);
                System.out.println(dictionaries);
                for (Map<String, String> dict : dictionaries) {
                    String title = dict.get("title");
                    String datePublished = dict.get("date_published");
                    String entryText = dict.get("entry_text");
                    String username = dict.get("username");

                    JPanel entryPanel = new JPanel();
                    entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
                    entryPanel.setPreferredSize(new Dimension(300, 150));
                    entryPanel.setMaximumSize(new Dimension(300, 150));

                    JLabel titleLabel = new JLabel(title);
                    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
                    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    entryPanel.add(titleLabel);

                    JLabel subtitleLabel = new JLabel("Published on " + datePublished + " by " + username);
                    subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                    subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    entryPanel.add(subtitleLabel);

                    JTextArea entryTextArea = new JTextArea(entryText);
                    entryTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
                    entryTextArea.setLineWrap(true);
                    entryTextArea.setWrapStyleWord(true);
                    entryTextArea.setEditable(false);
                    entryTextArea.setOpaque(false);

                    JScrollPane scrollPane = new JScrollPane(entryTextArea);
                    scrollPane.setPreferredSize(new Dimension(280, 80));
                    scrollPane.setMaximumSize(new Dimension(280, 80));
                    scrollPane.setBorder(null);

                    entryPanel.add(scrollPane);
                    entryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    JButton editButton = new JButton("Edit");
                    editButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EditEntryPanel editEntryPanel = new EditEntryPanel(main, username, title, entryText);
                            main.switchToEditEntryPanel(editEntryPanel);
                        }
                    });
                    entryPanel.add(editButton);

                    entriesPanel.add(entryPanel);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to retrieve entries");
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(this, "Failed to retrieve entries");
        }

        entriesPanel.revalidate();
        entriesPanel.repaint();
    }

    // parse's the string received from server to json
    private List<Map<String, String>> parseJson(String jsonString) {
        List<Map<String, String>> dictionaries = new ArrayList<>();
        if (jsonString.equals("[]")) {
            return dictionaries; // in cease list is empty, return empty so it doesnt bug out entry panels
        }
        jsonString = jsonString.substring(1, jsonString.length() - 1); // reomves the outerbrackets
        String[] entries = jsonString.split("\\}\\s*,\\s*\\{"); // splits each thing into an individual entry
        for (String entry : entries) {
            Map<String, String> dict = new HashMap<>();
            entry = entry.replaceAll("\\{", "").replaceAll("\\}", ""); // remove the curly brackets
            String[] fields = entry.split(","); // split each entry into an individual thing, key and item
            for (String field : fields) {
                int colonIndex = field.indexOf(":");
                if (colonIndex != -1) {
                    String key = field.substring(0, colonIndex).trim().replaceAll("\"", ""); // remove quotes so it's visible
                    String value = field.substring(colonIndex + 1).trim().replaceAll("\"", ""); // once again remove quotes
                    dict.put(key, value);
                }
            }
            dictionaries.add(dict);
        }
        return dictionaries;
    }
}