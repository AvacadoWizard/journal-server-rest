package com.journal;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EntriesPanel extends JPanel {
    private Main main; // necessary to switch in

    public EntriesPanel(Main main) {
        this.main = main;
        setLayout(new BorderLayout());

        // creates panel for entries
        JPanel entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));

        // makes entries panel visible
        add(new JScrollPane(entriesPanel), BorderLayout.CENTER);

        refreshEntries(entriesPanel);
    }

    public void refreshEntries(JPanel entriesPanel) {
        entriesPanel.removeAll();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
    
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:3000/entries"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String responseBody = response.body();
                List<Map<String, String>> dictionaries = parseJson(responseBody);
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

    // necessary to parse string response into json!
    private List<Map<String, String>> parseJson(String jsonString) {
        List<Map<String, String>> dictionaries = new ArrayList<>();
        jsonString = jsonString.substring(1, jsonString.length() - 1);
        String[] entries = jsonString.split("\\}\\s*,\\s*\\{");
        for (String entry : entries) {
            Map<String, String> dict = new HashMap<>();
            entry = entry.replaceAll("\\{", "").replaceAll("\\}", "");
            String[] fields = entry.split(",");
            for (String field : fields) {
                int colonIndex = field.indexOf(":");
                if (colonIndex != -1) {
                    String key = field.substring(0, colonIndex).trim().replaceAll("\"", "");
                    String value = field.substring(colonIndex + 1).trim().replaceAll("\"", "");
                    dict.put(key, value);
                }
            }
            dictionaries.add(dict);
        }
        return dictionaries;
    }
}